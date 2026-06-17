package com.ciatshop.api.dto;

public record SalesAdjustRequest(
        String itemCd,
        String salesYmd,
        Integer salesEventSeq,
        int delta
) {}
