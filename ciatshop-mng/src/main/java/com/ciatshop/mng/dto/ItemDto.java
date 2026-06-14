package com.ciatshop.mng.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDto(
        String itemCd,
        String itemNm,
        int itemQnty,
        Integer unitPrice,
        Integer sellPrice,
        String itemSeCd,
        String itemCateCd,
        Integer unitPriceNet,
        Integer unitPriceVat,
        Integer sellPriceNet,
        Integer sellPriceVat
) {}
