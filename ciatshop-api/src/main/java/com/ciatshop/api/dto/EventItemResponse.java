package com.ciatshop.api.dto;

import com.ciatshop.api.domain.EventItem;

public record EventItemResponse(
        Integer eventItemSeq,
        Integer salesEventSeq,
        String itemCd,
        String itemNm,
        Integer itemSellPrice,
        int inqnty,
        Integer sellPrice,
        String useYn
) {
    public static EventItemResponse from(EventItem ei) {
        return new EventItemResponse(
                ei.getEventItemSeq(),
                ei.getSalesEventSeq(),
                ei.getItemCd(),
                ei.getItemNm(),
                ei.getItemSellPrice(),
                ei.getInqnty(),
                ei.getSellPrice(),
                ei.getUseYn()
        );
    }
}
