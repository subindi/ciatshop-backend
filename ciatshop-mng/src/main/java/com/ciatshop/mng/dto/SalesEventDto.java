package com.ciatshop.mng.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SalesEventDto(
        Integer salesEventSeq,
        String salesEventNm,
        String salesEventStrYmd,
        String salesEventEndYmd,
        Integer profitAmt,
        String profitSeCd,
        String profitVatYn
) {}
