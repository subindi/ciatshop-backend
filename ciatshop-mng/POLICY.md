# ciatshop-mng 정책서

> 프로젝트: `ciatshop-mng` (관리자 화면)  
> 포트: 9090  
> ciatshop API(8080) 실행 후 기동할 것

---

## 1. 기술 스택

| 항목 | 내용 |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| 뷰 엔진 | Thymeleaf 3.1 |
| API 호출 | Spring RestClient (내장) |
| Build | Gradle Kotlin DSL |

---

## 2. 패키지 구조

```
com.ciatshop.mng
├── CiatshopMngApplication.java
├── config/
│   └── RestClientConfig.java     RestClient Bean — api base-url 주입
├── client/                       ciatshop API 호출 클라이언트
│   ├── ItemApiClient.java
│   ├── SalesEventApiClient.java
│   ├── SalesApiClient.java
│   └── EventItemApiClient.java
├── dto/                          @JsonIgnoreProperties(ignoreUnknown=true) Record
│   ├── ApiResponse.java
│   ├── ItemDto.java
│   ├── SalesEventDto.java
│   ├── SalesDto.java
│   └── EventItemDto.java
└── controller/
    ├── ItemViewController.java
    ├── SalesEventViewController.java
    ├── SalesViewController.java
    └── EventItemViewController.java

resources/templates/
    ├── layout.html               공통 HEAD·NAV fragment
    ├── item/          (list, detail, form)
    ├── sales-event/   (list, detail, form)
    ├── sales/         (list, detail, form)
    └── event-item/    (list)
```

---

## 3. API 호출 방식

| 동작 | 호출 주체 | 방식 |
|---|---|---|
| 페이지 렌더링 (GET) | 뷰 컨트롤러 → `XxxApiClient` | RestClient (서버 사이드) |
| 데이터 변경 (CUD) | Thymeleaf 내 JavaScript | `fetch(API_BASE + '/api/v1/...')` (브라우저 사이드) |

`API_BASE`는 `layout.html` `<head>` fragment에서 주입:
```html
<script th:inline="javascript">
  const API_BASE = /*[[${@environment.getProperty('ciatshop.api.base-url')}]]*/ 'http://localhost:8080';
</script>
```

---

## 4. 화면 목록

### 상품 관리

| URL | 화면 |
|---|---|
| `GET /view/items` | 상품 목록 |
| `GET /view/items/{itemCd}` | 상품 상세 |
| `GET /view/items/new` | 상품 등록 폼 |
| `GET /view/items/{itemCd}/edit` | 상품 수정 폼 |

### 판매 이벤트 관리

| URL | 화면 |
|---|---|
| `GET /view/sales-events` | 이벤트 목록 |
| `GET /view/sales-events/{seq}` | 이벤트 상세 |
| `GET /view/sales-events/new` | 이벤트 등록 폼 |
| `GET /view/sales-events/{seq}/edit` | 이벤트 수정 폼 |

### 이벤트 상품 관리

| URL | 화면 |
|---|---|
| `GET /view/event-items?salesEventSeq=` | 이벤트별 판매 상품 목록 |

- 이벤트 선택 시 해당 이벤트 상품 목록 표시
- 행 인라인 편집: 입고수량·이벤트판매가·사용여부 수정 후 저장
- 상품 추가 모달: 미등록 상품만 필터링해서 표시
- 삭제: 행 단위 삭제 (confirm 후 API 호출)

### 판매내역 관리

| URL | 화면 |
|---|---|
| `GET /view/sales?salesYear=&salesEventSeq=` | 판매내역 조회 (년도·이벤트 필터) |
| `GET /view/sales/{salesSeq}` | 판매내역 상세 |
| `GET /view/sales/new` | 판매 등록 폼 |

**검색 조건 조합**

| 년도 | 이벤트 | 동작 |
|---|---|---|
| 미선택 | 미선택 | 결과 미표시 |
| 선택 | 미선택 | 해당 년도 전체 조회 |
| 미선택 | 선택 | 해당 이벤트 전체 조회 |
| 선택 | 선택 | 년도 + 이벤트 조회 |

**판매 등록 시 재고 차감 규칙**
- API에서 `TB_ITEM.ITEM_QNTY -= salesQnty` 원자적 처리
- 재고 부족 시 `ITEM_OUT_OF_STOCK(400)` 반환 → 폼에 오류 메시지 표시

---

## 5. 레이아웃 규칙

- 모든 페이지: `<head th:replace="~{layout :: head}">`, `<nav th:replace="~{layout :: nav('active-key')}">` 사용
- CSS: `layout.html` `<head>` fragment에 인라인 정의 (외부 CSS 파일 없음)
- 버튼 클래스: `btn-primary` / `btn-success` / `btn-warning` / `btn-danger` / `btn-secondary`

---

## 6. 설정

```yaml
# application.yml
server:
  port: 9090
ciatshop:
  api:
    base-url: http://localhost:8080
```

---

## 7. 실행

```
IntelliJ → CiatshopMngApplication
VM options: -Djava.io.tmpdir=C:\tmp
접속: http://localhost:9090/view/items
※ ciatshop API(8080)가 먼저 실행되어야 함
```
