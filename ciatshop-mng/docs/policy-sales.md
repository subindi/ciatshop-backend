# 판매내역 화면 정책서

> 프로젝트: ciatshop.mng (관리자)  
> 대상 화면: `/view/sales`, `/view/sales/{seq}`, `/view/sales/new`  
> 작성일: 2026-06-13

---

## 1. 화면 목록

| 화면명 | URL | 설명 |
|--------|-----|------|
| 판매내역 조회 | `GET /view/sales` | 년도·이벤트 조건 검색 및 목록 표시 |
| 판매내역 상세 | `GET /view/sales/{salesSeq}` | 단건 상세 조회 및 삭제 |
| 판매 등록 | `GET /view/sales/new` | 신규 판매내역 입력 폼 |

---

## 2. 판매내역 조회 (`/view/sales`)

### 2-1. 검색 패널

| 항목 | 유형 | 설명 |
|------|------|------|
| 년도 | SELECT | TB_SALES.SALES_YMD 기준 DISTINCT 연도 목록. "-- 전체 --" 포함 |
| 이벤트 | SELECT | 선택된 년도에 판매 실적이 있는 이벤트 목록. 년도 미선택 시 전체 이벤트 표시 |
| 조회 버튼 | SUBMIT | 폼 GET 제출 → 검색 실행 |
| 초기화 버튼 | LINK | `/view/sales` 이동 (파라미터 없음) |
| + 판매 등록 버튼 | LINK | `/view/sales/new` 이동 |

#### 년도-이벤트 연동 규칙
- 년도 콤보 변경 시 → `GET /api/v1/sales-events/by-year/{year}` 호출
- 응답 이벤트 목록으로 이벤트 콤보를 동적 교체
- 년도를 "-- 전체 --"로 변경 시 → 이벤트 콤보를 "-- 전체 --"만 남기고 초기화
- 페이지 로드 시 `selectedYear`가 있으면 자동으로 이벤트 콤보 필터 적용 (선택값 유지)

#### 검색 조건 조합

| 년도 | 이벤트 | 동작 |
|------|--------|------|
| 미선택 | 미선택 | 검색 결과 미표시 ("조회하세요" 안내) |
| 선택 | 미선택 | 해당 년도 전체 판매 조회 |
| 미선택 | 선택 | 해당 이벤트 전체 판매 조회 |
| 선택 | 선택 | 해당 년도 + 해당 이벤트 판매 조회 |

### 2-2. 요약 바 (검색 결과 있을 때만 표시)

| 항목 | 산출 방식 |
|------|-----------|
| 검색결과 N 건 | salesList.size() |
| 총 판매수량 | salesList.salesQnty 합계 |
| 총 판매금액 | salesList.salesPrice 합계 (null → 0 처리) |

### 2-3. 결과 테이블 컬럼

| 컬럼 | 데이터 | 비고 |
|------|--------|------|
| 순번 | SALES_SEQ | 클릭 시 상세 화면 이동 (링크) |
| 판매일자 | SALES_YMD | YYYYMMDD 그대로 표시 |
| 상품코드 | ITEM_CD | 회색 소형 폰트 |
| 상품명 | TB_ITEM.ITEM_NM (JOIN) | |
| 수량 | SALES_QNTY | 천 단위 쉼표, 우측 정렬 |
| 판매가 | SALES_PRICE | 천 단위 쉼표 + "원", null 시 "-" |
| 주문구분 | ORDER_SE_CD | 파란 뱃지, null 시 미표시 |
| 판매구분 | SALES_SE_CD | 파란 뱃지, null 시 미표시 |
| 판매장소 | SALES_PLACE_SE_CD | 소형 폰트 |
| 이벤트 | SALES_EVENT_SEQ | 초록 뱃지 (#번호), null 시 "-" |
| 관리 | 삭제 버튼 | 클릭 시 삭제 처리 (아래 참조) |

### 2-4. 삭제 (목록에서)
- 삭제 버튼 클릭 → confirm 다이얼로그
- 확인 시 `DELETE /api/v1/sales/{salesSeq}` 호출
- 성공 시 `location.reload()`
- 실패 시 alert(message)

---

## 3. 판매내역 상세 (`/view/sales/{salesSeq}`)

### 3-1. 표시 항목

| 항목 | 데이터 | 비고 |
|------|--------|------|
| 판매 순번 | SALES_SEQ | |
| 판매일자 | SALES_YMD | |
| 상품코드 | ITEM_CD | |
| 상품명 | TB_ITEM.ITEM_NM (JOIN) | |
| 판매수량 | SALES_QNTY | 천 단위 쉼표 + "개" |
| 판매가격 | SALES_PRICE | 천 단위 쉼표 + "원", null 시 "-", 빨간 굵은 폰트 |
| 주문구분코드 | ORDER_SE_CD | null 시 "-" |
| 판매구분코드 | SALES_SE_CD | null 시 "-" |
| 판매장소코드 | SALES_PLACE_SE_CD | null 시 "-" |
| 이벤트 순번 | SALES_EVENT_SEQ | 초록 뱃지, null 시 "없음" |

### 3-2. 액션 버튼

| 버튼 | 동작 |
|------|------|
| 목록 | `/view/sales` 이동 |
| 삭제 | confirm → `DELETE /api/v1/sales/{salesSeq}` → 성공 시 `/view/sales` 이동 |

---

## 4. 판매 등록 (`/view/sales/new`)

### 4-1. 입력 항목

| 항목 | ID | 필수 | 유효성 | 비고 |
|------|----|------|--------|------|
| 판매일자 | salesYmd | Y | 8자리 숫자 (YYYYMMDD) | |
| 상품 | itemCd | Y | 선택 필수 | 코드 + 상품명 + 재고 표시 |
| 판매수량 | salesQnty | Y | 1 이상 정수 | |
| 판매가격 | salesPrice | Y | 0 이상 정수 | |
| 주문구분코드 | orderSeCd | N | - | 미입력 시 null 전송 |
| 판매구분코드 | salesSeCd | N | - | 미입력 시 null 전송 |
| 판매장소코드 | salesPlaceSeCd | N | - | 미입력 시 null 전송 |
| 판매이벤트 | salesEventSeq | N | - | 미선택 시 null 전송 |

### 4-2. 저장 처리
- 유효성 검사 실패 시 화면 내 오류 메시지 표시 (페이지 이동 없음)
- `POST /api/v1/sales` 호출 (JSON body)
- 성공 시 등록된 판매내역 상세 화면으로 이동 (`/view/sales/{salesSeq}`)
- 실패 시 오류 메시지 표시

### 4-3. 재고 차감 규칙
- 저장 시 ciatshop.api에서 `TB_ITEM.ITEM_QNTY -= salesQnty` 원자적 처리
- `WHERE ITEM_QNTY >= salesQnty` 조건으로 재고 부족 시 0 rows → `ITEM_OUT_OF_STOCK` 오류 반환
- 오류 수신 시 폼에 재고 부족 메시지 표시

---

## 5. API 연동 정보

| 화면 | 호출 API | 용도 |
|------|----------|------|
| 조회 (서버) | `GET /api/v1/sales/years` | 년도 콤보 데이터 |
| 조회 (서버) | `GET /api/v1/sales-events` | 이벤트 콤보 초기 데이터 |
| 조회 (서버) | `GET /api/v1/sales/search?salesYear=&salesEventSeq=` | 검색 결과 |
| 조회 (JS) | `GET /api/v1/sales-events/by-year/{year}` | 년도별 이벤트 필터링 |
| 목록·상세 | `DELETE /api/v1/sales/{salesSeq}` | 판매내역 삭제 |
| 등록 | `POST /api/v1/sales` | 판매내역 등록 |

---

## 6. 제약 사항

- 판매내역 **수정 기능 없음** (등록·삭제만 지원)
- 검색 결과 **페이징 없음** (전체 조회)
- 삭제 시 **복구 불가** (소프트 딜리트 미적용)
- 재고보다 많은 수량은 등록 불가 (API에서 차단)
