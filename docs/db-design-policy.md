# DB 설계 정책서

| 항목 | 내용 |
|---|---|
| 프로젝트 | ciatshop |
| DB | MySQL 9.x |
| 작성일 | 2026-06-14 |
| 버전 | v1.0 |

---

## 목차

1. [목적 및 범위](#1-목적-및-범위)
2. [네이밍 규칙](#2-네이밍-규칙)
3. [공통 컬럼](#3-공통-컬럼)
4. [데이터 타입 표준](#4-데이터-타입-표준)
5. [테이블 설계 규칙](#5-테이블-설계-규칙)
6. [인덱스 규칙](#6-인덱스-규칙)
7. [테이블 목록](#7-테이블-목록)
8. [테이블 상세 정의](#8-테이블-상세-정의)
9. [ERD](#9-erd)

---

## 1. 목적 및 범위

- 이 문서는 ciatshop 서비스의 데이터베이스 설계 기준을 정의한다.
- 모든 테이블/컬럼 설계는 이 정책을 따른다.
- 적용 DB: `ciatshop` 스키마

---

## 2. 네이밍 규칙

### 2-1. 공통 원칙

| 원칙 | 설명 |
|---|---|
| 언어 | 영문 대문자 사용 |
| 구분자 | 단어 사이 언더스코어(`_`) |
| 약어 | 사전 정의된 약어만 사용 |
| 최대 길이 | 30자 이내 |

### 2-2. 테이블

```
TB_{업무구분}_{엔티티명}

예) TB_ITEM          → 상품
    TB_SALES         → 판매
    TB_SALES_EVENT   → 판매 이벤트
```

### 2-3. 컬럼

```
{엔티티약어}_{의미}_{유형접미사}

예) ITEM_CD          → 상품코드  (코드: _CD)
    ITEM_NM          → 상품명    (명칭: _NM)
    SALES_YMD        → 판매일자  (일자: _YMD)
    SALES_QNTY       → 판매수량  (수량: _QNTY)
    PROFIT_AMT       → 이익금액  (금액: _AMT)
    PROFIT_VAT_YN    → 부가세여부 (여부: _YN)
    ORDER_SE_CD      → 주문구분코드 (구분코드: _SE_CD)
```

### 2-4. 접미사 표준

| 접미사 | 의미 | 예시 |
|---|---|---|
| `_SEQ` | 일련번호 (PK) | `ITEM_SEQ`, `SALES_SEQ` |
| `_CD` | 코드 | `ITEM_CD`, `ORDER_SE_CD` |
| `_NM` | 명칭 | `ITEM_NM`, `SALES_EVENT_NM` |
| `_YMD` | 일자 (YYYYMMDD) | `SALES_YMD`, `STR_YMD` |
| `_DT` | 일시 (DATETIME) | `REG_DT`, `UPDT_DT` |
| `_AMT` | 금액 | `PROFIT_AMT`, `SALES_PRICE` |
| `_QNTY` | 수량 | `ITEM_QNTY`, `SALES_QNTY` |
| `_YN` | Y/N 여부 | `PROFIT_VAT_YN` |
| `_SE_CD` | 구분코드 | `ORDER_SE_CD`, `SALES_SE_CD` |
| `_URL` | URL | `IMG_URL` |
| `_CNT` | 건수 | `ORDER_CNT` |

### 2-5. 인덱스

```
IDX_{테이블명}_{컬럼약어조합}

예) IDX_TB_SALES_YMD          → TB_SALES.SALES_YMD 인덱스
    IDX_TB_SALES_EVENT_SEQ    → TB_SALES.SALES_EVENT_SEQ 인덱스
```

---

## 3. 공통 컬럼

모든 테이블에 아래 컬럼을 포함한다.

| 컬럼명 | 타입 | NULL | 기본값 | 설명 |
|---|---|---|---|---|
| `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | 등록일시 |
| `UPDT_DT` | DATETIME | NULL | NULL | 수정일시 |
| `REGR_SEQ` | INT | NULL | NULL | 등록자 SEQ |
| `UPDR_SEQ` | INT | NULL | NULL | 수정자 SEQ |

> **규칙**: 수정 시 반드시 `UPDT_DT = CURRENT_TIMESTAMP` 명시적 업데이트

---

## 4. 데이터 타입 표준

| 용도 | 타입 | 비고 |
|---|---|---|
| 일련번호 PK | `INT AUTO_INCREMENT` | |
| 코드값 (짧은) | `VARCHAR(10)` | |
| 코드값 (일반) | `VARCHAR(20)` | |
| 명칭/이름 | `VARCHAR(100)` | |
| 긴 텍스트 | `TEXT` | |
| 일자 (YYYYMMDD) | `VARCHAR(8)` | DATE 타입 미사용 |
| 일시 | `DATETIME` | |
| 금액 | `INT` | 원 단위, 소수점 미사용 |
| 수량 | `INT` | |
| Y/N 여부 | `CHAR(1)` | 'Y' / 'N' |
| 비율/세율 | `DECIMAL(5,2)` | 예: 10.00 |

> **규칙**: 일자는 `DATE` 대신 `VARCHAR(8)` 사용 (YYYYMMDD 포맷, 검색 용이)

---

## 5. 테이블 설계 규칙

### 5-1. PK

- 모든 테이블은 단일 `INT AUTO_INCREMENT` PK를 가진다.
- PK 컬럼명: `{엔티티약어}_SEQ`

```sql
-- 예시
SALES_SEQ INT NOT NULL AUTO_INCREMENT PRIMARY KEY
```

### 5-2. 외래키 (FK)

- FK 제약조건은 **선택적으로** 적용한다.
- FK 컬럼명은 참조 테이블의 PK명과 동일하게 한다.

```sql
-- 예시: TB_SALES.SALES_EVENT_SEQ → TB_SALES_EVENT.SALES_EVENT_SEQ
SALES_EVENT_SEQ INT NULL   -- NULL 허용 (이벤트 없는 판매 가능)
```

### 5-3. 기본값

- 문자열: 기본값 없음 (NULL 또는 어플리케이션에서 처리)
- 수량/금액: `DEFAULT 0`
- 일시: `DEFAULT CURRENT_TIMESTAMP`

### 5-4. NULL 정책

| 상황 | NULL 허용 |
|---|---|
| 필수 입력값 (코드, 날짜 등) | NOT NULL |
| 선택 입력값 | NULL 허용 |
| 참조 FK (선택적 관계) | NULL 허용 |
| 공통 컬럼 UPDT_DT | NULL 허용 |

---

## 6. 인덱스 규칙

- PK는 자동 인덱스
- 조회 조건에 자주 쓰이는 컬럼에 인덱스 추가
- 카디널리티가 낮은 컬럼 (`YN`, `SE_CD` 등) 단독 인덱스 지양

```sql
-- 예시: 판매일자, 이벤트 기준 조회가 많으므로 인덱스 추가
CREATE INDEX IDX_TB_SALES_YMD       ON TB_SALES (SALES_YMD);
CREATE INDEX IDX_TB_SALES_EVENT_SEQ ON TB_SALES (SALES_EVENT_SEQ);
```

---

## 7. 테이블 목록

| # | 테이블명 | 한글명 | 설명 |
|---|---|---|---|
| 1 | `TB_ITEM` | 상품 | 판매 상품 마스터 |
| 2 | `TB_SALES` | 판매 | 일자별 상품 판매 수량 |
| 3 | `TB_SALES_EVENT` | 판매 이벤트 | 행사/이벤트 기간 관리 |
| 4 | `TB_EVENT_ITEM` | 이벤트 판매 상품 | 이벤트별 판매 상품 설정 (입고수량/가격/사용여부) |
| 5 | `TB_VENDOR` | 거래처 | 거래처 마스터 |
| 6 | `TB_INBOUND` | 입고 | 거래처별 날짜별 제품 입고 관리 |

---

## 8. 테이블 상세 정의

### TB_ITEM — 상품

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `ITEM_CD` | VARCHAR(20) | NOT NULL | — | PK | 상품코드 |
| 2 | `ITEM_NM` | VARCHAR(100) | NOT NULL | — | | 상품명 |
| 3 | `ITEM_QNTY` | INT | NOT NULL | 0 | | 재고수량 |
| 4 | `ITEM_SE_CD` | VARCHAR(20) | NULL | — | | 상품구분코드 |
| 5 | `ITEM_CATE_CD` | VARCHAR(20) | NULL | — | | 카테고리코드 |
| 6 | `UNIT_PRICE` | INT | NULL | — | | 단가 (원가) |
| 7 | `UNIT_PRICE_NET` | INT | NULL | — | | 단가 공급가 |
| 8 | `UNIT_PRICE_VAT` | INT | NULL | — | | 단가 부가세 |
| 9 | `SELL_PRICE` | INT | NULL | — | | 판매가 |
| 10 | `SELL_PRICE_NET` | INT | NULL | — | | 판매가 공급가 |
| 11 | `SELL_PRICE_VAT` | INT | NULL | — | | 판매가 부가세 |
| 12 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |

> **비고**: ITEM_CD는 AUTO_INCREMENT 미사용. 수동 코드 체계 적용 (예: 1001, 1002…)

---

### TB_SALES — 판매

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `SALES_SEQ` | INT | NOT NULL | AUTO_INCREMENT | PK | 판매일련번호 |
| 2 | `SALES_YMD` | VARCHAR(8) | NOT NULL | — | | 판매일자 (YYYYMMDD) |
| 3 | `ITEM_CD` | VARCHAR(20) | NOT NULL | — | FK→TB_ITEM | 상품코드 |
| 4 | `SALES_QNTY` | INT | NOT NULL | 0 | | 판매수량 |
| 5 | `SALES_PRICE` | INT | NULL | — | | 판매금액 |
| 6 | `ORDER_SE_CD` | VARCHAR(20) | NULL | — | | 주문구분코드 |
| 7 | `SALES_SE_CD` | VARCHAR(20) | NULL | — | | 판매구분코드 |
| 8 | `SALES_PLACE_SE_CD` | VARCHAR(20) | NULL | — | | 판매장소구분코드 |
| 9 | `SALES_EVENT_SEQ` | INT | NULL | — | FK→TB_SALES_EVENT | 판매이벤트SEQ |
| 10 | `SVC_YN` | CHAR(1) | NOT NULL | 'N' | | 서비스제공여부 (Y/N) |
| 11 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |
| 12 | `UPDT_DT` | DATETIME | NULL | — | | 수정일시 |
| 13 | `REGR_SEQ` | INT | NULL | — | | 등록자SEQ |
| 14 | `UPDR_SEQ` | INT | NULL | — | | 수정자SEQ |

**인덱스**
```sql
IDX_TB_SALES_YMD        (SALES_YMD)
IDX_TB_SALES_EVENT_SEQ  (SALES_EVENT_SEQ)
```

**UPSERT 정책**
- `(ITEM_CD, SALES_YMD, SALES_EVENT_SEQ)` 조합이 유일해야 한다.
- 동일 조합 존재 시 `SALES_QNTY` 누적 업데이트, `UPDT_DT` 갱신.
- 존재하지 않고 delta > 0 이면 INSERT.

---

### TB_SALES_EVENT — 판매 이벤트

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `SALES_EVENT_SEQ` | INT | NOT NULL | AUTO_INCREMENT | PK | 이벤트일련번호 |
| 2 | `SALES_EVENT_NM` | VARCHAR(100) | NOT NULL | — | | 이벤트명 |
| 3 | `SALES_EVENT_STR_YMD` | VARCHAR(8) | NOT NULL | — | | 이벤트시작일 (YYYYMMDD) |
| 4 | `SALES_EVENT_END_YMD` | VARCHAR(8) | NOT NULL | — | | 이벤트종료일 (YYYYMMDD) |
| 5 | `PROFIT_AMT` | INT | NULL | — | | 목표이익금액 |
| 6 | `PROFIT_SE_CD` | VARCHAR(20) | NULL | — | | 이익구분코드 |
| 7 | `PROFIT_VAT_YN` | CHAR(1) | NULL | — | | 부가세포함여부 (Y/N) |
| 8 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |
| 9 | `UPDT_DT` | DATETIME | NULL | — | | 수정일시 |

---

### TB_EVENT_ITEM — 이벤트별 판매 상품 (v1.1 신규)

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `EVENT_ITEM_SEQ` | INT | NOT NULL | AUTO_INCREMENT | PK | 이벤트상품SEQ |
| 2 | `SALES_EVENT_SEQ` | INT | NOT NULL | — | FK→TB_SALES_EVENT | 판매이벤트SEQ |
| 3 | `ITEM_CD` | VARCHAR(20) | NOT NULL | — | FK→TB_ITEM | 상품코드 |
| 4 | `INQNTY` | INT | NOT NULL | 0 | | 입고수량 |
| 5 | `SELL_PRICE` | INT | NULL | — | | 이벤트판매가 (NULL이면 TB_ITEM.SELL_PRICE 사용) |
| 6 | `USE_YN` | CHAR(1) | NOT NULL | 'Y' | | 사용여부 (Y/N) |
| 7 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |
| 8 | `UPDT_DT` | DATETIME | NULL | — | | 수정일시 |
| 9 | `REGR_SEQ` | INT | NULL | — | | 등록자SEQ |
| 10 | `UPDR_SEQ` | INT | NULL | — | | 수정자SEQ |

**인덱스**
```sql
UNIQUE KEY UNQ_TB_EVENT_ITEM (SALES_EVENT_SEQ, ITEM_CD)
IDX_TB_EVENT_ITEM_EVENT  (SALES_EVENT_SEQ)
IDX_TB_EVENT_ITEM_ITEM   (ITEM_CD)
```

---

### TB_VENDOR — 거래처 (v1.1 신규)

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `VENDOR_SEQ` | INT | NOT NULL | AUTO_INCREMENT | PK | 거래처SEQ |
| 2 | `VENDOR_CD` | VARCHAR(20) | NOT NULL | — | | 거래처코드 |
| 3 | `VENDOR_NM` | VARCHAR(100) | NOT NULL | — | | 거래처명 |
| 4 | `USE_YN` | CHAR(1) | NOT NULL | 'Y' | | 사용여부 (Y/N) |
| 5 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |
| 6 | `UPDT_DT` | DATETIME | NULL | — | | 수정일시 |
| 7 | `REGR_SEQ` | INT | NULL | — | | 등록자SEQ |
| 8 | `UPDR_SEQ` | INT | NULL | — | | 수정자SEQ |

**인덱스**
```sql
UNIQUE KEY UNQ_TB_VENDOR_CD (VENDOR_CD)
```

---

### TB_INBOUND — 입고 (v1.1 신규)

| # | 컬럼명 | 타입 | NULL | 기본값 | PK/FK | 설명 |
|---|---|---|---|---|---|---|
| 1 | `INBOUND_SEQ` | INT | NOT NULL | AUTO_INCREMENT | PK | 입고SEQ |
| 2 | `INBOUND_YMD` | VARCHAR(8) | NOT NULL | — | | 입고일자 (YYYYMMDD) |
| 3 | `VENDOR_SEQ` | INT | NOT NULL | — | FK→TB_VENDOR | 거래처SEQ |
| 4 | `ITEM_CD` | VARCHAR(20) | NOT NULL | — | FK→TB_ITEM | 상품코드 |
| 5 | `INBOUND_QNTY` | INT | NOT NULL | 0 | | 입고수량 |
| 6 | `SALES_EVENT_SEQ` | INT | NULL | — | FK→TB_SALES_EVENT | 판매이벤트SEQ (이벤트 지정 시) |
| 7 | `UNIT_PRICE` | INT | NULL | — | | 입고단가 |
| 8 | `REMARK` | VARCHAR(200) | NULL | — | | 비고 |
| 9 | `REG_DT` | DATETIME | NOT NULL | CURRENT_TIMESTAMP | | 등록일시 |
| 10 | `UPDT_DT` | DATETIME | NULL | — | | 수정일시 |
| 11 | `REGR_SEQ` | INT | NULL | — | | 등록자SEQ |
| 12 | `UPDR_SEQ` | INT | NULL | — | | 수정자SEQ |

**인덱스**
```sql
IDX_TB_INBOUND_YMD    (INBOUND_YMD)
IDX_TB_INBOUND_VENDOR (VENDOR_SEQ)
IDX_TB_INBOUND_ITEM   (ITEM_CD)
IDX_TB_INBOUND_EVENT  (SALES_EVENT_SEQ)
```

---

## 9. ERD

```
TB_SALES_EVENT
┌──────────────────────┐
│ SALES_EVENT_SEQ (PK) │◀────────────────────────────────────────┐
│ SALES_EVENT_NM       │◀──────────────────────┐                 │
│ SALES_EVENT_STR_YMD  │                       │                 │
│ SALES_EVENT_END_YMD  │                       │                 │
│ PROFIT_AMT           │                       │                 │
└──────────┬───────────┘                       │                 │
           │ 1:N (NULL 허용)                   │                 │
           ▼                                   │                 │
┌──────────────────────┐     ┌─────────────────────────┐         │
│ TB_SALES             │     │ TB_EVENT_ITEM            │         │
│ SALES_SEQ (PK)       │     │ EVENT_ITEM_SEQ (PK)      │         │
│ SALES_YMD            │     │ SALES_EVENT_SEQ (FK) ───▶│         │
│ ITEM_CD (FK) ────────┼─┐   │ ITEM_CD (FK) ────────────┼──┐      │
│ SALES_QNTY           │ │   │ INQNTY                   │  │      │
│ SVC_YN               │ │   │ SELL_PRICE               │  │      │
│ SALES_EVENT_SEQ (FK)─┼─┼──▶│ USE_YN                   │  │      │
└──────────────────────┘ │   └──────────────────────────┘  │      │
                         │                                  │      │
                         │   ┌──────────────────────────┐  │      │
                         └──▶│ TB_ITEM                  │◀─┘      │
                             │ ITEM_CD (PK)             │         │
                             │ ITEM_NM                  │         │
                             │ SELL_PRICE               │         │
                             └──────────────────────────┘         │
                                        ▲                         │
TB_VENDOR                               │                         │
┌──────────────────────┐                │                         │
│ VENDOR_SEQ (PK)      │                │                         │
│ VENDOR_CD            │                │                         │
│ VENDOR_NM            │                │                         │
└──────────┬───────────┘                │                         │
           │ 1:N                        │                         │
           ▼                            │                         │
┌──────────────────────┐                │                         │
│ TB_INBOUND           │                │                         │
│ INBOUND_SEQ (PK)     │                │                         │
│ INBOUND_YMD          │                │                         │
│ VENDOR_SEQ (FK) ─────┘                │                         │
│ ITEM_CD (FK) ─────────────────────────┘                         │
│ INBOUND_QNTY                                                    │
│ SALES_EVENT_SEQ (FK) ───────────────────────────────────────────┘
│ UNIT_PRICE           │
└──────────────────────┘
```

---

## 변경 이력

| 버전 | 날짜 | 작성자 | 변경내용 |
|---|---|---|---|
| v1.0 | 2026-06-14 | — | 최초 작성 |
| v1.1 | 2026-06-14 | — | TB_EVENT_ITEM/TB_VENDOR/TB_INBOUND 추가, TB_SALES.SVC_YN 추가 |
