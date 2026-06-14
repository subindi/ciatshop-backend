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
public class Item {

    private String itemCd;
    private String itemNm;
    private int itemQnty;
    private LocalDateTime regDt;
    private String itemSeCd;
    private String itemCateCd;
    private Integer unitPrice;
    private Integer sellPrice;
    private Integer unitPriceNet;
    private Integer unitPriceVat;
    private Integer sellPriceNet;
    private Integer sellPriceVat;
}
