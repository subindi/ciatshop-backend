# ciatshop 프로젝트 컨텍스트

## 프로젝트 개요
건어물 판매 관리 시스템. 이벤트(행사)별 상품 판매 수량을 카운팅하고 관리하는 앱.

## 프로젝트 구성

| 디렉토리 | 역할 | 포트 |
|---|---|---|
| `ciatshop/` | Spring Boot API 서버 | 8080 |
| `ciatshop-mng/` | Thymeleaf 관리자 화면 | 8081 |
| `ciatshop-front/` | React + TypeScript 판매 카운터 | 5173 (dev) |
| `docker-compose.yml` | 전체 서비스 컨테이너 구성 | - |
| `docs/` | DB 설계 정책서 및 마이그레이션 SQL | - |

## 기술 스택

- **API**: Java 17, Spring Boot 3.3.5, MyBatis 3.0.3, Gradle
- **DB**: MySQL 9.x (Docker), DB명: `ciatshop`, 계정: `ciatshop`/`ciat0601`
- **MNG**: Spring Boot 3.3.5, Thymeleaf 3.1, RestClient (API 호출)
- **Front**: React 18, TypeScript, Vite 5

## DB 구조

```
TB_ITEM          - 상품 마스터 (ITEM_CD PK, VARCHAR)
TB_SALES         - 판매 (SALES_SEQ PK, ITEM_CD FK, SALES_EVENT_SEQ FK, SVC_YN)
TB_SALES_EVENT   - 판매 이벤트 (SALES_EVENT_SEQ PK)
TB_EVENT_ITEM    - 이벤트별 판매 상품 (EVENT_ITEM_SEQ PK, INQNTY, SELL_PRICE, USE_YN)
TB_VENDOR        - 거래처 (VENDOR_SEQ PK)
TB_INBOUND       - 입고 (INBOUND_SEQ PK, VENDOR_SEQ FK, INBOUND_YMD)
```

- 날짜: `VARCHAR(8)` YYYYMMDD (DATE 타입 미사용)
- 금액/수량: `INT`
- Y/N 플래그: `CHAR(1)`
- 공통 컬럼: `REG_DT`, `UPDT_DT`, `REGR_SEQ`, `UPDR_SEQ`
- DB 설계 정책: `docs/db-design-policy.md` 참고

## API 엔드포인트

```
GET  /api/v1/items
POST /api/v1/items
PUT  /api/v1/items/{itemCd}
DELETE /api/v1/items/{itemCd}

GET  /api/v1/sales?salesYmd=&salesEventSeq=
POST /api/v1/sales/adjust   { itemCd, salesYmd, salesEventSeq, delta }

GET  /api/v1/sales-events
POST /api/v1/sales-events
PUT  /api/v1/sales-events/{seq}

GET  /api/v1/event-items?salesEventSeq=
POST /api/v1/event-items
PUT  /api/v1/event-items/{eventItemSeq}
DELETE /api/v1/event-items/{eventItemSeq}
```

## 주요 구현 패턴

### UPSERT (판매 수량 조정)
`(ITEM_CD, SALES_YMD, SALES_EVENT_SEQ)` 조합 존재 시 UPDATE, 없으면 INSERT  
`SALES_QNTY = GREATEST(0, SALES_QNTY + delta)`

### 디바운스 (Front)
버튼 연타 시 300ms 후 단일 API 호출. `pendingDeltaRef`에 누적 후 flush.

### CORS 설정
```java
registry.addMapping("/api/**")
    .allowedOriginPatterns("*")
    .allowedMethods("GET","POST","PUT","DELETE","OPTIONS")
    .allowedHeaders("*")
    .allowCredentials(true);
```

### MNG API 호출
RestClient 사용. `API_BASE_URL`은 `application.yml`의 `ciatshop.api.base-url`에서 주입.  
Thymeleaf에서 `th:inline="javascript"`로 프론트에 전달.

## 로컬 개발 환경 세팅

```powershell
# MySQL Docker 실행 (바인드 마운트: D:\docker\mysql)
docker run -d --name mysql `
  -v "D:\docker\mysql:/var/lib/mysql" `
  -p 3306:3306 `
  -e MYSQL_ROOT_PASSWORD=root `
  mysql:latest

# DB 복원 (최초 1회)
docker exec -i mysql mysql --default-character-set=utf8mb4 -u ciatshop -pciat0601 ciatshop < docs/migration/ciatshop_dump.sql
```

### IntelliJ VM options (T: 드라이브 오류 방지)
```
-Djava.io.tmpdir=C:\tmp
```

## 관리자 화면 URL
- 상품관리: http://localhost:8081/view/items
- 판매이벤트: http://localhost:8081/view/sales-events
- 이벤트 상품: http://localhost:8081/view/event-items
- 판매내역: http://localhost:8081/view/sales

## 배포 계획
Oracle Cloud Free Tier (ARM) 또는 Google Cloud로 Docker Compose 배포 예정.  
`docker-compose.yml` 참고. `.env` 파일에 서버 IP / DB 패스워드 설정 필요 (Git 미포함).

## GitHub
https://github.com/subindi/ciatshop-backend
