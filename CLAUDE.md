# ciatshop 프로젝트 컨텍스트

> 건어물 판매 관리 시스템. 이벤트(행사)별 상품 판매 수량 카운팅 및 관리.

---

## 프로젝트 구성

| 디렉토리 | 역할 | 포트 | 정책서 |
|---|---|---|---|
| `ciatshop-api/` | Spring Boot REST API 서버 | 8080 | `ciatshop-api/POLICY.md` |
| `ciatshop-mng/` | Thymeleaf 관리자 화면 | 8081 | `ciatshop-mng/POLICY.md` |
| `ciatshop-front/` | React + TypeScript 판매 카운터 | 5173 (dev) | `ciatshop-front/POLICY.md` |
| `docker-compose.yml` | 전체 서비스 컨테이너 구성 | — | — |
| `docs/` | DB 설계 정책서 및 마이그레이션 SQL | — | `docs/db-design-policy.md` |

## 호출 흐름

```
[핸드폰 브라우저]
  └─ fetch /api/* ──────────────────► ciatshop (API :8080)
                                            │
[관리자 브라우저]                            │
  └─ 페이지 요청(SSR) ──► ciatshop-mng     │
       └─ RestClient ──────────────────────►│
  └─ CUD(JS fetch) ──────────────────────► │
```

## 기술 스택

| 항목 | 내용 |
|---|---|
| API | Java 17, Spring Boot 3.3.5, MyBatis 3.0.3, Gradle |
| MNG | Java 17, Spring Boot 3.3.5, Thymeleaf 3.1, RestClient |
| Front | React 18, TypeScript 5, Vite 5, React Router 6 |
| DB | MySQL 9.x (Docker), DB: `ciatshop`, 계정: `ciatshop`/`ciat0601` |

## DB 테이블 요약

```
TB_ITEM          상품 마스터          (ITEM_CD PK — VARCHAR, 수동 코드)
TB_SALES_EVENT   판매 이벤트          (SALES_EVENT_SEQ PK)
TB_SALES         판매                 (SALES_SEQ PK, SVC_YN default 'N')
TB_EVENT_ITEM    이벤트별 판매 상품   (EVENT_ITEM_SEQ PK, USE_YN default 'Y')
TB_VENDOR        거래처               (VENDOR_SEQ PK)
TB_INBOUND       입고                 (INBOUND_SEQ PK, INBOUND_YMD VARCHAR(8))
```

- 날짜: `VARCHAR(8)` YYYYMMDD (DATE 타입 미사용)
- 금액·수량: `INT` / Y/N 플래그: `CHAR(1)`
- 공통 컬럼: `REG_DT`, `UPDT_DT`, `REGR_SEQ`, `UPDR_SEQ`
- 상세 정책: `docs/db-design-policy.md`

## 로컬 개발 환경

```powershell
# MySQL — 바인드 마운트 방식
docker run -d --name mysql `
  -v "D:\docker\mysql:/var/lib/mysql" `
  -p 3306:3306 -e MYSQL_ROOT_PASSWORD=root mysql:latest

# 최초 DB 복원
docker exec -i mysql mysql --default-character-set=utf8mb4 `
  -u ciatshop -pciat0601 ciatshop < docs/migration/ciatshop_dump.sql
```

> **IntelliJ VM options** (T: 드라이브 오류 방지): `-Djava.io.tmpdir=C:\tmp`

## 실행 순서

1. MySQL Docker 기동
2. `ciatshop-api` (API) 실행 — IntelliJ, profile: `local`
3. `ciatshop-mng` 실행 — IntelliJ
4. `ciatshop-front` 실행 — `npm run dev`

## 주요 URL

| 서비스 | URL |
|---|---|
| API Swagger | http://localhost:8080/swagger-ui.html |
| 관리자 — 상품 | http://localhost:8081/view/items |
| 관리자 — 이벤트 | http://localhost:8081/view/sales-events |
| 관리자 — 이벤트 상품 | http://localhost:8081/view/event-items |
| 관리자 — 판매내역 | http://localhost:8081/view/sales |
| 판매 카운터 | http://localhost:5173 |

## 배포 계획

Oracle Cloud Free Tier(ARM) 또는 Google Cloud에 Docker Compose로 배포 예정.  
`.env` 파일에 서버 IP / DB 패스워드 설정 (Git 미포함 — `.gitignore` 처리됨).

## GitHub

https://github.com/subindi/ciatshop-backend
