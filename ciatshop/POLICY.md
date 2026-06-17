# ciatshop API 정책서

> 프로젝트: `ciatshop` (REST API 서버)  
> 포트: 8080

---

## 1. 기술 스택

| 항목 | 내용 |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| Build | Gradle Kotlin DSL |
| DB 접근 | MyBatis Spring Boot Starter 3.0.3 |
| DB | MySQL 9.x |
| API 문서 | SpringDoc OpenAPI (Swagger UI) 2.6.0 |

---

## 2. 패키지 구조

```
com.ciatshop.api
├── global/
│   ├── ApiResponse.java          공통 응답 래퍼
│   ├── BusinessException.java    비즈니스 예외
│   ├── ErrorCode.java            에러 코드 열거형
│   ├── GlobalExceptionHandler.java
│   └── WebConfig.java            CORS 설정
├── domain/                       순수 POJO (Lombok)
│   ├── Item.java
│   ├── SalesEvent.java
│   ├── Sales.java
│   └── EventItem.java
├── mapper/                       MyBatis @Mapper 인터페이스
│   ├── ItemMapper.java
│   ├── SalesEventMapper.java
│   ├── SalesMapper.java
│   └── EventItemMapper.java
├── dto/                          Java Record (불변)
│   ├── ItemRequest/Response.java
│   ├── SalesEventRequest/Response.java
│   ├── SalesRequest/Response.java
│   ├── SalesAdjustRequest.java
│   └── EventItemRequest/Response.java
├── service/
│   ├── ItemService.java
│   ├── SalesEventService.java
│   ├── SalesService.java
│   └── EventItemService.java
└── controller/
    ├── ItemController.java
    ├── SalesEventController.java
    ├── SalesController.java
    └── EventItemController.java

resources/mapper/
    ├── ItemMapper.xml
    ├── SalesEventMapper.xml
    ├── SalesMapper.xml
    └── EventItemMapper.xml
```

---

## 3. 공통 응답 형식

```json
{ "success": true, "message": "...", "data": { } }
```

- `message`: 성공 시 생략 가능 (`@JsonInclude(NON_NULL)`)
- 모든 컨트롤러는 `ApiResponse<T>`로 감싸 반환

---

## 4. API 엔드포인트

### 상품 (`/api/v1/items`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/api/v1/items` | 전체 목록 |
| GET | `/api/v1/items/{itemCd}` | 단건 조회 |
| POST | `/api/v1/items` | 등록 |
| PUT | `/api/v1/items/{itemCd}` | 수정 |
| DELETE | `/api/v1/items/{itemCd}` | 삭제 |

### 판매 이벤트 (`/api/v1/sales-events`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/api/v1/sales-events` | 전체 목록 |
| GET | `/api/v1/sales-events/{seq}` | 단건 조회 |
| POST | `/api/v1/sales-events` | 등록 |
| PUT | `/api/v1/sales-events/{seq}` | 수정 |
| DELETE | `/api/v1/sales-events/{seq}` | 삭제 |

### 판매 (`/api/v1/sales`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/api/v1/sales?salesYmd=&salesEventSeq=` | 날짜·이벤트 조회 |
| GET | `/api/v1/sales/{salesSeq}` | 단건 조회 |
| POST | `/api/v1/sales` | 등록 |
| DELETE | `/api/v1/sales/{salesSeq}` | 삭제 |
| POST | `/api/v1/sales/adjust` | 수량 조정 (카운터용) |

**adjust 요청 body**
```json
{ "itemCd": "1001", "salesYmd": "20260614", "salesEventSeq": 1, "delta": 1 }
```

### 이벤트 상품 (`/api/v1/event-items`)

| 메서드 | URL | 설명 |
|---|---|---|
| GET | `/api/v1/event-items?salesEventSeq=` | 이벤트별 상품 목록 |
| POST | `/api/v1/event-items` | 이벤트에 상품 추가 |
| PUT | `/api/v1/event-items/{eventItemSeq}` | 입고수량·판매가·사용여부 수정 |
| DELETE | `/api/v1/event-items/{eventItemSeq}` | 이벤트에서 상품 제거 |

---

## 5. UPSERT 정책 (판매 수량 조정)

- `(ITEM_CD, SALES_YMD, SALES_EVENT_SEQ)` 조합 존재 시 → `SALES_QNTY` 누적 UPDATE
- 존재하지 않고 delta > 0 시 → INSERT
- 음수 방지: `GREATEST(0, SALES_QNTY + delta)`

---

## 6. CORS 설정

```java
// global/WebConfig.java
registry.addMapping("/api/**")
    .allowedOriginPatterns("*")   // 핸드폰 WiFi 접속 허용
    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
    .allowedHeaders("*")
    .allowCredentials(true);
```

---

## 7. 에러 코드

| 코드 | HTTP | 메시지 |
|---|---|---|
| `INVALID_INPUT` | 400 | 입력값이 올바르지 않습니다. |
| `ITEM_NOT_FOUND` | 404 | 상품을 찾을 수 없습니다. |
| `ITEM_OUT_OF_STOCK` | 400 | 재고가 부족합니다. |
| `SALES_NOT_FOUND` | 404 | 판매 내역을 찾을 수 없습니다. |
| `SALES_EVENT_NOT_FOUND` | 404 | 판매 이벤트를 찾을 수 없습니다. |
| `EVENT_ITEM_NOT_FOUND` | 404 | 이벤트 상품을 찾을 수 없습니다. |
| `DUPLICATE_EVENT_ITEM` | 409 | 이미 해당 이벤트에 등록된 상품입니다. |

---

## 8. DB 설정

```yaml
# application-local.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ciatshop?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ciatshop
    password: ciat0601
mybatis:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

- MyBatis `map-underscore-to-camel-case: true` → 컬럼명 자동 매핑
- 복잡한 JOIN 결과는 `resultMap` 명시 사용

---

## 9. 실행

```
IntelliJ → CiatshopApplication
Active Profile: local
VM options: -Djava.io.tmpdir=C:\tmp
접속: http://localhost:8080/swagger-ui.html
```
