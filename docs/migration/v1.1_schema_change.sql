-- ================================================================
-- ciatshop DB 스키마 변경
-- 버전  : v1.1
-- 작성일 : 2026-06-14
-- 내용   :
--   1. TB_EVENT_ITEM  생성 - 이벤트별 판매 상품 (입고수량/가격/사용여부)
--   2. TB_VENDOR      생성 - 거래처 마스터
--   3. TB_INBOUND     생성 - 거래처별 날짜별 입고
--   4. TB_SALES       변경 - SVC_YN 컬럼 추가 (서비스 제공 여부)
-- ================================================================

USE ciatshop;

-- ================================================================
-- 1. TB_EVENT_ITEM : 이벤트별 판매 상품
--    요건:
--      - 이벤트별 판매 제품 설정 (어떤 제품을 팔지)
--      - 이벤트별 제품 입고 수량 설정
--      - 이벤트별 판매 제품 사용 유무 (default: Y)
--      - 이벤트별 판매 제품 가격 설정
-- ================================================================
CREATE TABLE IF NOT EXISTS TB_EVENT_ITEM (
    EVENT_ITEM_SEQ    INT           NOT NULL AUTO_INCREMENT    COMMENT '이벤트상품SEQ',
    SALES_EVENT_SEQ   INT           NOT NULL                   COMMENT '판매이벤트SEQ (FK→TB_SALES_EVENT)',
    ITEM_CD           VARCHAR(20)   NOT NULL                   COMMENT '상품코드 (FK→TB_ITEM)',
    INQNTY            INT           NOT NULL DEFAULT 0         COMMENT '입고수량',
    SELL_PRICE        INT           NULL                       COMMENT '이벤트판매가 (NULL이면 TB_ITEM.SELL_PRICE 사용)',
    USE_YN            CHAR(1)       NOT NULL DEFAULT 'Y'       COMMENT '사용여부 (Y:사용 / N:미사용)',
    REG_DT            DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    UPDT_DT           DATETIME      NULL                       COMMENT '수정일시',
    REGR_SEQ          INT           NULL                       COMMENT '등록자SEQ',
    UPDR_SEQ          INT           NULL                       COMMENT '수정자SEQ',
    PRIMARY KEY (EVENT_ITEM_SEQ),
    UNIQUE KEY UNQ_TB_EVENT_ITEM (SALES_EVENT_SEQ, ITEM_CD)
) COMMENT = '이벤트별 판매 상품';

CREATE INDEX IDX_TB_EVENT_ITEM_EVENT ON TB_EVENT_ITEM (SALES_EVENT_SEQ);
CREATE INDEX IDX_TB_EVENT_ITEM_ITEM  ON TB_EVENT_ITEM (ITEM_CD);


-- ================================================================
-- 2. TB_VENDOR : 거래처
--    요건:
--      - 거래처별 날짜별 입고 관리를 위한 거래처 마스터
-- ================================================================
CREATE TABLE IF NOT EXISTS TB_VENDOR (
    VENDOR_SEQ   INT           NOT NULL AUTO_INCREMENT    COMMENT '거래처SEQ',
    VENDOR_CD    VARCHAR(20)   NOT NULL                   COMMENT '거래처코드',
    VENDOR_NM    VARCHAR(100)  NOT NULL                   COMMENT '거래처명',
    USE_YN       CHAR(1)       NOT NULL DEFAULT 'Y'       COMMENT '사용여부 (Y:사용 / N:미사용)',
    REG_DT       DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    UPDT_DT      DATETIME      NULL                       COMMENT '수정일시',
    REGR_SEQ     INT           NULL                       COMMENT '등록자SEQ',
    UPDR_SEQ     INT           NULL                       COMMENT '수정자SEQ',
    PRIMARY KEY (VENDOR_SEQ),
    UNIQUE KEY UNQ_TB_VENDOR_CD (VENDOR_CD)
) COMMENT = '거래처';


-- ================================================================
-- 3. TB_INBOUND : 입고
--    요건:
--      - 거래처별 날짜별 제품 입고 수량 관리
-- ================================================================
CREATE TABLE IF NOT EXISTS TB_INBOUND (
    INBOUND_SEQ      INT           NOT NULL AUTO_INCREMENT    COMMENT '입고SEQ',
    INBOUND_YMD      VARCHAR(8)    NOT NULL                   COMMENT '입고일자 (YYYYMMDD)',
    VENDOR_SEQ       INT           NOT NULL                   COMMENT '거래처SEQ (FK→TB_VENDOR)',
    ITEM_CD          VARCHAR(20)   NOT NULL                   COMMENT '상품코드 (FK→TB_ITEM)',
    INBOUND_QNTY     INT           NOT NULL DEFAULT 0         COMMENT '입고수량',
    SALES_EVENT_SEQ  INT           NULL                       COMMENT '판매이벤트SEQ (FK→TB_SALES_EVENT, NULL이면 이벤트 미지정)',
    UNIT_PRICE       INT           NULL                       COMMENT '입고단가',
    REMARK           VARCHAR(200)  NULL                       COMMENT '비고',
    REG_DT           DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록일시',
    UPDT_DT          DATETIME      NULL                       COMMENT '수정일시',
    REGR_SEQ         INT           NULL                       COMMENT '등록자SEQ',
    UPDR_SEQ         INT           NULL                       COMMENT '수정자SEQ',
    PRIMARY KEY (INBOUND_SEQ)
) COMMENT = '입고';

CREATE INDEX IDX_TB_INBOUND_YMD    ON TB_INBOUND (INBOUND_YMD);
CREATE INDEX IDX_TB_INBOUND_VENDOR ON TB_INBOUND (VENDOR_SEQ);
CREATE INDEX IDX_TB_INBOUND_ITEM   ON TB_INBOUND (ITEM_CD);
CREATE INDEX IDX_TB_INBOUND_EVENT  ON TB_INBOUND (SALES_EVENT_SEQ);


-- ================================================================
-- 4. TB_SALES 컬럼 추가
--    요건:
--      - 제품 판매시 서비스 제공 여부 (default: N)
-- ================================================================
ALTER TABLE TB_SALES
    ADD COLUMN SVC_YN CHAR(1) NOT NULL DEFAULT 'N' COMMENT '서비스제공여부 (Y:제공 / N:미제공)'
    AFTER SALES_EVENT_SEQ;


-- ================================================================
-- 변경 이력: 정책서 v1.1 반영 완료
-- ================================================================
