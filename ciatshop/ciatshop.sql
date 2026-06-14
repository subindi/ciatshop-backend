-- ciat.TB_CODE definition

CREATE TABLE `TB_CODE` (
  `CODE_SEQ` bigint NOT NULL AUTO_INCREMENT,
  `CODE_CD` varchar(20) NOT NULL,
  `CODE_NM` varchar(200) NOT NULL,
  `CODE_SORT` bigint NOT NULL,
  `CODE_SE_CD` varchar(20) NOT NULL DEFAULT '01',
  `UP_CODE_CD` varchar(20) DEFAULT NULL,
  `UP_CODE_SEQ` bigint DEFAULT NULL,
  `SITE_CD` varchar(20) NOT NULL DEFAULT 'CM',
  `USE_YN` varchar(1) NOT NULL,
  `REG_DT` datetime NOT NULL,
  `REGR_SN` varchar(20) NOT NULL,
  `UPDT_DT` datetime NOT NULL,
  `UPDR_SN` varchar(20) NOT NULL,
  `DEL_YN` varchar(1) NOT NULL DEFAULT 'N',
  `DEL_DT` datetime DEFAULT NULL,
  `DELR_SN` varchar(20) DEFAULT NULL,
  `FILTER_STR` varchar(100) DEFAULT NULL,
  `CODE_VIEW_NM` varchar(200) NOT NULL,
  PRIMARY KEY (`CODE_SEQ`),
  UNIQUE KEY `PK_TB_CODE` (`CODE_SEQ`),
  KEY `IDX_TB_CODE_01` (`CODE_CD`,`UP_CODE_CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ciat.TB_EXCEL definition

CREATE TABLE `TB_EXCEL` (
  `EXL_SEQ` bigint NOT NULL AUTO_INCREMENT,
  `TASK_SE_CD` varchar(20) DEFAULT NULL,
  `TASK_YM` varchar(6) DEFAULT NULL,
  `TITLE` varchar(200) DEFAULT NULL,
  `DISABL_SE_CD` varchar(20) DEFAULT NULL,
  `RACER_CNT` bigint DEFAULT NULL,
  `LADR_CNT` bigint DEFAULT NULL,
  `DPCN_EXCL_CNT` bigint DEFAULT NULL,
  `FILE_PATH` varchar(200) DEFAULT NULL,
  `FILE_VIEW_NM` varchar(200) DEFAULT NULL,
  `FILE_REAL_NM` varchar(100) DEFAULT NULL,
  `DPCN_EXCL_ROW` varchar(2000) DEFAULT NULL,
  `DEL_EXL_ROW` varchar(2000) DEFAULT NULL,
  `DEL_EXL_ROW_CNT` bigint DEFAULT NULL,
  `REG_DT` datetime NOT NULL,
  `REGR_SN` varchar(20) NOT NULL,
  `REG_IP` varchar(20) DEFAULT NULL,
  `EXL_TOT_CNT` bigint DEFAULT NULL,
  `BIZ_YEAR` varchar(4) DEFAULT NULL,
  `SD_CD` varchar(20) DEFAULT NULL,
  `EXL_TASK_SE_CD` varchar(20) DEFAULT NULL,
  `HFYR_CD` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EXL_SEQ`),
  UNIQUE KEY `PK_TB_EXCEL` (`EXL_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ciat.TB_EXPENSE definition

CREATE TABLE `TB_EXPENSE` (
  `EXPNS_SEQ` int NOT NULL AUTO_INCREMENT COMMENT '고유값',
  `PMT_YMD` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지급일자',
  `PMT_SE_CD` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지급구분',
  `PMT_PLACE` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지급처',
  `PMT_USE` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '지급용도',
  `PMT_AMT` int DEFAULT NULL COMMENT '지급금액',
  `PMT_REGR` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '결제자',
  `RMK` varchar(500) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '비고',
  PRIMARY KEY (`EXPNS_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='비용';


-- ciat.TB_FILE definition

CREATE TABLE `TB_FILE` (
  `FILE_SEQ` bigint NOT NULL AUTO_INCREMENT,
  `REF_TB` varchar(30) NOT NULL,
  `REF_SN` varchar(20) NOT NULL,
  `FILE_SE_CD` varchar(20) DEFAULT NULL,
  `FILE_PATH` varchar(200) NOT NULL,
  `FILE_ORGIN_NM` varchar(100) NOT NULL,
  `FILE_REAL_NM` varchar(100) NOT NULL,
  `FILE_REAL_RESIZE_NM` varchar(100) DEFAULT NULL,
  `REG_DT` datetime NOT NULL,
  `REGR_SN` varchar(20) NOT NULL,
  `DEL_YN` varchar(1) NOT NULL DEFAULT 'N',
  `DEL_DT` datetime DEFAULT NULL,
  `DELR_SN` varchar(20) DEFAULT NULL,
  `RMK` varchar(1000) DEFAULT NULL,
  `TASK_DCMT_SN` varchar(20) DEFAULT NULL,
  `UPDT_DT` datetime DEFAULT NULL,
  `UPDR_SN` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`FILE_SEQ`),
  UNIQUE KEY `PK_TB_FILE` (`FILE_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ciat.TB_INVENTORY definition

CREATE TABLE `TB_INVENTORY` (
  `IVTY_SEQ` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ciat.TB_ITEM definition

CREATE TABLE `TB_ITEM` (
  `ITEM_CD` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ITEM_NM` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `ITEM_QNTY` int DEFAULT '0',
  `REG_DT` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `ITEM_SE_CD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '아이템구분코드(01:건어물)',
  `ITEM_CATE_CD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '아이템카테고리코드(01:판매용,99:시식용)',
  `UNIT_PRICE` int DEFAULT NULL COMMENT '단가',
  `SELL_PRICE` int DEFAULT NULL COMMENT '판매가',
  `UNIT_PRICE_NET` int DEFAULT NULL COMMENT '단가_공급가',
  `UNIT_PRICE_VAT` int DEFAULT NULL COMMENT '단가_부가세',
  `SELL_PRICE_NET` int DEFAULT NULL COMMENT '판매가_공급가',
  `SELL_PRICE_VAT` int DEFAULT NULL COMMENT '판매가_부가세',
  PRIMARY KEY (`ITEM_CD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ciat.TB_MENU definition

CREATE TABLE `TB_MENU` (
  `MENU_SEQ` bigint NOT NULL AUTO_INCREMENT,
  `MENU_NM` varchar(100) DEFAULT NULL,
  `MENU_LAYOUT` varchar(50) DEFAULT NULL,
  `MENU_SORT` bigint DEFAULT NULL,
  `MENU_TYPE` varchar(20) DEFAULT NULL,
  `MENU_SE_CD` varchar(20) DEFAULT NULL,
  `MENU_URL` varchar(1000) DEFAULT NULL,
  `MENU_LINK_TGT` varchar(20) DEFAULT NULL,
  `UP_MENU_SEQ` bigint DEFAULT NULL,
  `USE_YN` varchar(1) DEFAULT NULL,
  `SITE_CD` varchar(20) DEFAULT NULL,
  `REG_DT` datetime NOT NULL,
  `REGR_SN` varchar(20) NOT NULL,
  `UPDT_DT` datetime NOT NULL,
  `UPDR_SN` varchar(20) NOT NULL,
  `DEL_YN` varchar(1) NOT NULL DEFAULT 'N',
  `DEL_DT` datetime DEFAULT NULL,
  `DELR_SN` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`MENU_SEQ`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- ciat.TB_SALES definition

CREATE TABLE `TB_SALES` (
  `SALES_SEQ` int NOT NULL AUTO_INCREMENT,
  `SALES_YMD` varchar(8) COLLATE utf8mb4_unicode_ci NOT NULL,
  `ITEM_CD` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `SALES_QNTY` int NOT NULL,
  `REG_DT` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `UPDT_DT` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `REGR_SEQ` int DEFAULT NULL,
  `UPDR_SEQ` int DEFAULT NULL COMMENT '수정자순번',
  `ORDER_SE_CD` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '주문구분코드(F:플리마켓,D:당근,I:인스타)',
  `SALES_SE_CD` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '판매구분코드(N:정가,D:할인,C:원가)',
  `SALES_PRICE` int DEFAULT NULL COMMENT '판매가격',
  `SALES_PLACE_SE_CD` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '판매장소코드',
  `SALES_EVENT_SEQ` int DEFAULT NULL COMMENT '판매이벤트고유값',
  PRIMARY KEY (`SALES_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=1302 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ciat.TB_SALES_DAY definition

CREATE TABLE `TB_SALES_DAY` (
  `SALES_YMD` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '판매일자',
  `SALES_PLACE` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '판매처',
  `SALES_AMT` int DEFAULT NULL COMMENT '매출금액',
  `SALES_EVENT_SEQ` int DEFAULT NULL COMMENT '이벤트순번'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;


-- ciat.TB_SALES_EVENT definition

CREATE TABLE `TB_SALES_EVENT` (
  `SALES_EVENT_SEQ` int NOT NULL AUTO_INCREMENT COMMENT '판매이벤트순번',
  `SALES_EVENT_NM` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '판매이벤트명',
  `SALES_EVENT_STR_YMD` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '이벤트시작일',
  `SALES_EVENT_END_YMD` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '이벤트종료일',
  `PROFIT_AMT` int DEFAULT NULL COMMENT '수수료',
  `PROFIT_SE_CD` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '수수료구분',
  `PROFIT_VAT_YN` varchar(1) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '수수료부가세여부',
  PRIMARY KEY (`SALES_EVENT_SEQ`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;