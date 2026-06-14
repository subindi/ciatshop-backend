package com.ciatshop.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sales {

    private Integer salesSeq;
    private String salesYmd;
    private String itemCd;
    private String itemNm;
    private int salesQnty;
    private LocalDateTime regDt;
    private LocalDateTime updtDt;
    private Integer regrSeq;
    private Integer updrSeq;
    private String orderSeCd;
    private String salesSeCd;
    private Integer salesPrice;
    private String salesPlaceSeCd;
    private Integer salesEventSeq;
}
