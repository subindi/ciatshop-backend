# CiatShop 멀티 프로젝트 정책서

> **작성일:** 2026-06-13  
> **구성:** ciatshop.api / ciatshop.mng / ciatshop.front

---

## 1. 프로젝트 전체 구조

```
D:\99_DEV\claude\
├── ciatshop\          → ciatshop.api   (REST API 서버)
├── ciatshop-mng\      → ciatshop.mng   (관리자 웹 — Spring Boot + Thymeleaf)
└── ciatshop-front\    → ciatshop.front (홈페이지 — React + TypeScript + Vite)
```

### 역할 분리

| 프로젝트 | 역할 | 포트 | 주요 사용자 |
|---|---|---|---|
| ciatshop.api | 순수 REST API 서버. DB 접근, 비즈니스 로직 담당 | 8080 | (내부) |
| ciatshop.mng | 관리자 화면. ciatshop.api를 RestClient로 호출 | 8081 | 운영자 |
| ciatshop.front | 홈페이지. ciatshop.api를 fetch로 직접 호출 | 5173 | 고객 |

### 호출 흐름

```
[브라우저: mng 8081]
  └─ 페이지 요청 (SSR) ──► ciatshop.mng (Thymeleaf)
       └─ RestClient ──────► ciatshop.api :8080
  └─ CUD 요청 (JS fetch) ──► ciatshop.api :8080  ← CORS 허용

[브라우저: front 5173]
  └─ fetch /api/* ─────────► ciatshop.api :8080  ← Vite proxy or CORS
```

---

## 2. ciatshop.api

### 기술 스택

| 항목 | 버전 |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| Build | Gradle Kotlin DSL |
| DB 접근 | MyBatis Spring Boot Starter 3.0.3 |
| DB | MySQL 9.x |
| API 문서 | SpringDoc OpenAPI (Swagger UI) 2.6.0 |

### 패키지 구조

```
com.ciatshop.api
├── global/
│   ├── ApiResponse.java
│   ├── BusinessException.java
│   ├── ErrorCode.java
│   ├── GlobalExceptionHandler.java
│   └── WebConfig.java          ← CORS 설정
├── domain/                     ← 순수 POJO
│   ├── Item.java
│   ├── SalesEvent.java
│   └── Sales.java
├── mapper/                     ← MyBatis @Mapper
│   ├── ItemMapper.java
│   ├── SalesEventMapper.java
│   └── SalesMapper.java
├── dto/
│   ├── ItemRequest/Response.java
│   ├── SalesEventRequest/Response.java
│   └── SalesRequest/Response.java
├── service/
│   ├── ItemService.java
│   ├── SalesEventService.java
│   └── SalesService.java
└── controller/
    ├── ItemController.java
    ├── SalesEventController.java
    └── SalesController.java

resources/mapper/
    ├── ItemMapper.xml
    ├── SalesEventMapper.xml
    └── SalesMapper.xml
```

### CORS 허용 Origin

```java
// global/WebConfig.java
.allowedOrigins(
    "http://localhost:8081",   // ciatshop.mng
    "http://localhost:5173",   // ciatshop.front (Vite dev)
    "http://localhost:3000"    // ciatshop.front (fallback)
)
```

### DB 설정 (application-local.yml)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/ciatshop?serverTimezone=Asia/Seoul&characterEncoding=UTF-8
    username: ciatshop
    password: ciat0601
    driver-class-name: com.mysql.cj.jdbc.Driver
mybatis:
  mapper-locations: classpath:mapper/*.xml
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### API 명세 요약

| 리소스 | 엔드포인트 | 메서드 |
|---|---|---|
| 상품 | `/api/v1/items` | GET, POST |
| 상품 단건 | `/api/v1/items/{itemCd}` | GET, PUT, DELETE |
| 판매이벤트 | `/api/v1/sales-events` | GET, POST |
| 판매이벤트 단건 | `/api/v1/sales-events/{eventSeq}` | GET, PUT, DELETE |
| 판매 (날짜) | `/api/v1/sales?salesYmd=YYYYMMDD` | GET |
| 판매 단건 | `/api/v1/sales/{salesSeq}` | GET, DELETE |
| 판매 (이벤트별) | `/api/v1/sales/events/{eventSeq}` | GET |
| 판매 등록 | `/api/v1/sales` | POST |

공통 응답: `{ "success": boolean, "message": string?, "data": T? }`

### 예외 코드

| 코드 | HTTP | 메시지 |
|---|---|---|
| ITEM_NOT_FOUND | 404 | 상품을 찾을 수 없습니다. |
| ITEM_OUT_OF_STOCK | 400 | 재고가 부족합니다. |
| SALES_NOT_FOUND | 404 | 판매 내역을 찾을 수 없습니다. |
| SALES_EVENT_NOT_FOUND | 404 | 판매 이벤트를 찾을 수 없습니다. |

---

## 3. ciatshop.mng

### 기술 스택

| 항목 | 버전 |
|---|---|
| Java | 17 |
| Spring Boot | 3.3.5 |
| 뷰 엔진 | Thymeleaf |
| API 호출 | Spring RestClient (내장) |

### 패키지 구조

```
com.ciatshop.mng
├── CiatshopMngApplication.java
├── config/
│   └── RestClientConfig.java   ← api base-url Bean 등록
├── client/                     ← ciatshop.api 호출
│   ├── ItemApiClient.java
│   ├── SalesEventApiClient.java
│   └── SalesApiClient.java
├── dto/
│   ├── ApiResponse.java
│   ├── ItemDto.java
│   ├── SalesEventDto.java
│   └── SalesDto.java
└── controller/
    ├── ItemViewController.java
    ├── SalesEventViewController.java
    └── SalesViewController.java

resources/templates/
    ├── layout.html              ← API_BASE JS 변수 주입
    ├── item/ (list, detail, form)
    ├── sales-event/ (list, detail, form)
    └── sales/ (list, detail, form)
```

### 설정 (application.yml)

```yaml
server:
  port: 8081
ciatshop:
  api:
    base-url: http://localhost:8080
```

### API 호출 방식

- **페이지 렌더링(GET)**: 뷰 컨트롤러 → `XxxApiClient` → RestClient → ciatshop.api  
- **데이터 변경(CUD)**: Thymeleaf 템플릿 내 JavaScript `fetch(API_BASE + '/api/v1/...')` → ciatshop.api 직접 호출

`API_BASE`는 `layout.html`의 `<head>` 프래그먼트에서 `application.yml`의 `ciatshop.api.base-url` 값을 Thymeleaf로 주입:

```html
<script th:inline="javascript">
  const API_BASE = /*[[${@environment.getProperty('ciatshop.api.base-url')}]]*/ 'http://localhost:8080';
</script>
```

### 화면 목록

| URL | 화면 |
|---|---|
| `/view/items` | 상품 목록 |
| `/view/items/{itemCd}` | 상품 상세 |
| `/view/items/new` | 상품 등록 |
| `/view/items/{itemCd}/edit` | 상품 수정 |
| `/view/sales-events` | 판매이벤트 목록 |
| `/view/sales-events/{seq}` | 판매이벤트 상세 |
| `/view/sales-events/new` | 판매이벤트 등록 |
| `/view/sales-events/{seq}/edit` | 판매이벤트 수정 |
| `/view/sales` | 판매내역 목록 (날짜 조회) |
| `/view/sales/{seq}` | 판매내역 상세 |
| `/view/sales/new` | 판매 등록 |

---

## 4. ciatshop.front

### 기술 스택

| 항목 | 버전 |
|---|---|
| 언어 | TypeScript 5.x |
| 프레임워크 | React 18 |
| 빌드 도구 | Vite 5.x |
| 라우터 | React Router DOM 6.x |

### 프로젝트 구조

```
ciatshop-front/
├── index.html
├── package.json
├── vite.config.ts              ← /api/* → localhost:8080 프록시
├── tsconfig.json
└── src/
    ├── main.tsx
    ├── App.tsx                 ← React Router 설정
    ├── api/
    │   └── client.ts           ← 타입 정의 + fetch 래퍼
    ├── pages/
    │   ├── ItemListPage.tsx
    │   └── ItemDetailPage.tsx
    └── components/             ← 공통 컴포넌트 (추가 예정)
```

### API 호출 방식

개발 환경: Vite proxy가 `/api/*` → `http://localhost:8080` 으로 포워딩 (CORS 불필요)

```typescript
// vite.config.ts
proxy: { '/api': { target: 'http://localhost:8080', changeOrigin: true } }
```

운영 환경: nginx 등 리버스 프록시 또는 ciatshop.api CORS 허용으로 처리

### 라우트

| 경로 | 페이지 |
|---|---|
| `/` | `/items` 리다이렉트 |
| `/items` | 상품 목록 |
| `/items/:itemCd` | 상품 상세 |

---

## 5. 도메인 모델 (ciatshop.api 기준)

### TB_ITEM

| 컬럼 | 타입 | Java 필드 |
|---|---|---|
| ITEM_CD | VARCHAR(100) PK | itemCd |
| ITEM_NM | VARCHAR(100) | itemNm |
| ITEM_QNTY | INT | itemQnty |
| REG_DT | TIMESTAMP | regDt |
| ITEM_SE_CD | VARCHAR(100) | itemSeCd |
| ITEM_CATE_CD | VARCHAR(100) | itemCateCd |
| UNIT_PRICE | INT | unitPrice |
| SELL_PRICE | INT | sellPrice |
| UNIT_PRICE_NET | INT | unitPriceNet |
| UNIT_PRICE_VAT | INT | unitPriceVat |
| SELL_PRICE_NET | INT | sellPriceNet |
| SELL_PRICE_VAT | INT | sellPriceVat |

### TB_SALES_EVENT

| 컬럼 | 타입 | Java 필드 |
|---|---|---|
| SALES_EVENT_SEQ | INT AUTO_INCREMENT PK | salesEventSeq |
| SALES_EVENT_NM | VARCHAR(100) | salesEventNm |
| SALES_EVENT_STR_YMD | VARCHAR(8) | salesEventStrYmd |
| SALES_EVENT_END_YMD | VARCHAR(8) | salesEventEndYmd |
| PROFIT_AMT | INT | profitAmt |
| PROFIT_SE_CD | VARCHAR(100) | profitSeCd |
| PROFIT_VAT_YN | VARCHAR(1) | profitVatYn |

### TB_SALES

| 컬럼 | 타입 | Java 필드 |
|---|---|---|
| SALES_SEQ | INT AUTO_INCREMENT PK | salesSeq |
| SALES_YMD | VARCHAR(8) | salesYmd |
| ITEM_CD | VARCHAR(100) FK | itemCd |
| SALES_QNTY | INT | salesQnty |
| REG_DT | TIMESTAMP | regDt |
| UPDT_DT | TIMESTAMP | updtDt |
| REGR_SEQ | INT | regrSeq |
| UPDR_SEQ | INT | updrSeq |
| ORDER_SE_CD | VARCHAR(100) | orderSeCd |
| SALES_SE_CD | VARCHAR(100) | salesSeCd |
| SALES_PRICE | INT | salesPrice |
| SALES_PLACE_SE_CD | VARCHAR(100) | salesPlaceSeCd |
| SALES_EVENT_SEQ | INT FK | salesEventSeq |

---

## 6. 실행 방법

### ciatshop.api

```
IntelliJ → CiatshopApplication → Active Profile: local → Run
접속: http://localhost:8080/swagger-ui.html
```

### ciatshop.mng

```
IntelliJ → CiatshopMngApplication → Run
접속: http://localhost:8081/view/items
※ ciatshop.api가 먼저 실행되어야 함
```

### ciatshop.front

```bash
cd ciatshop-front
npm install
npm run dev
접속: http://localhost:5173
※ ciatshop.api가 먼저 실행되어야 함
```

---

## 7. MySQL Docker 설정

```sql
-- 컨테이너: mysql, root 비밀번호: 1124
CREATE USER IF NOT EXISTS 'ciatshop'@'%' IDENTIFIED BY 'ciat0601';
GRANT ALL PRIVILEGES ON ciatshop.* TO 'ciatshop'@'%';
FLUSH PRIVILEGES;
```

DB: `ciatshop` / 포트: `3306` / 스키마 기준: `ciatshop.sql`

---

## 8. 향후 작업 예정

- [ ] ciatshop.front 페이지 추가 (SalesEvent, Sales 조회)
- [ ] JWT 인증/인가 (api) + 로그인 화면 (mng, front)
- [ ] 운영 프로파일 분리 + nginx 리버스 프록시 구성
- [ ] ciatshop.mng Gradle Wrapper 초기화 (`gradle wrapper` 실행 필요)
