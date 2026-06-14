package com.ciatshop.mng.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EventItemDto(
        Integer eventItemSeq,
        Integer salesEventSeq,
        String itemCd,
        String itemNm,
        Integer itemSellPrice,
        int inqnty,
        Integer sellPrice,
        String useYn
) {}
