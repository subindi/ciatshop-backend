package com.ciatshop.api.global;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    // 공통
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 아이템
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    ITEM_OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고가 부족합니다."),

    // 판매
    SALES_NOT_FOUND(HttpStatus.NOT_FOUND, "판매 내역을 찾을 수 없습니다."),

    // 판매 이벤트
    SALES_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "판매 이벤트를 찾을 수 없습니다."),

    // 이벤트 상품
    EVENT_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "이벤트 상품을 찾을 수 없습니다."),
    DUPLICATE_EVENT_ITEM(HttpStatus.CONFLICT, "이미 해당 이벤트에 등록된 상품입니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() { return status; }
    public String getMessage() { return message; }
}
