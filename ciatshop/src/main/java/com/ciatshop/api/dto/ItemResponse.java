package com.ciatshop.api.dto;

import com.ciatshop.api.domain.Item;

public record ItemResponse(
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
) {
    public static ItemResponse from(Item item) {
        return new ItemResponse(
                item.getItemCd(),
                item.getItemNm(),
                item.getItemQnty(),
                item.getUnitPrice(),
                item.getSellPrice(),
                item.getItemSeCd(),
                item.getItemCateCd(),
                item.getUnitPriceNet(),
                item.getUnitPriceVat(),
                item.getSellPriceNet(),
                item.getSellPriceVat()
        );
    }
}
