package com.ciatshop.api.dto;

import com.ciatshop.api.domain.Sales;

public record SalesResponse(
        Integer salesSeq,
        String salesYmd,
        String itemCd,
        String itemNm,
        int salesQnty,
        Integer salesPrice,
        String orderSeCd,
        String salesSeCd,
        String salesPlaceSeCd,
        Integer salesEventSeq
) {
    public static SalesResponse from(Sales sales) {
        return new SalesResponse(
                sales.getSalesSeq(),
                sales.getSalesYmd(),
                sales.getItemCd(),
                sales.getItemNm(),
                sales.getSalesQnty(),
                sales.getSalesPrice(),
                sales.getOrderSeCd(),
                sales.getSalesSeCd(),
                sales.getSalesPlaceSeCd(),
                sales.getSalesEventSeq()
        );
    }
}
