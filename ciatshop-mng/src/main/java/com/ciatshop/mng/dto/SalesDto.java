package com.ciatshop.mng.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SalesDto(
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
) {}
