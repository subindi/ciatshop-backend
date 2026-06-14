package com.ciatshop.api.dto;

import com.ciatshop.api.domain.SalesEvent;

public record SalesEventResponse(
        Integer salesEventSeq,
        String salesEventNm,
        String salesEventStrYmd,
        String salesEventEndYmd,
        Integer profitAmt,
        String profitSeCd,
        String profitVatYn
) {
    public static SalesEventResponse from(SalesEvent event) {
        return new SalesEventResponse(
                event.getSalesEventSeq(),
                event.getSalesEventNm(),
                event.getSalesEventStrYmd(),
                event.getSalesEventEndYmd(),
                event.getProfitAmt(),
                event.getProfitSeCd(),
                event.getProfitVatYn()
        );
    }
}
