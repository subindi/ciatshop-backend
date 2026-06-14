package com.ciatshop.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalesEvent {

    private Integer salesEventSeq;
    private String salesEventNm;
    private String salesEventStrYmd;
    private String salesEventEndYmd;
    private Integer profitAmt;
    private String profitSeCd;
    private String profitVatYn;
}
