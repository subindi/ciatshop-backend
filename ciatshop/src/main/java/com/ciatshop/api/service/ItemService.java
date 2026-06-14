package com.ciatshop.api.service;

import com.ciatshop.api.domain.Item;
import com.ciatshop.api.dto.ItemRequest;
import com.ciatshop.api.dto.ItemResponse;
import com.ciatshop.api.global.BusinessException;
import com.ciatshop.api.global.ErrorCode;
import com.ciatshop.api.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemMapper itemMapper;

    public List<ItemResponse> getItems() {
        return itemMapper.selectAll().stream()
                .map(ItemResponse::from)
                .toList();
    }

    public ItemResponse getItem(String itemCd) {
        return ItemResponse.from(findItemOrThrow(itemCd));
    }

    @Transactional
    public ItemResponse createItem(ItemRequest request) {
        Item item = Item.builder()
                .itemCd(request.itemCd())
                .itemNm(request.itemNm())
                .itemQnty(request.itemQnty())
                .unitPrice(request.unitPrice())
                .sellPrice(request.sellPrice())
                .itemSeCd(request.itemSeCd())
                .itemCateCd(request.itemCateCd())
                .unitPriceNet(request.unitPriceNet())
                .unitPriceVat(request.unitPriceVat())
                .sellPriceNet(request.sellPriceNet())
                .sellPriceVat(request.sellPriceVat())
                .build();
        itemMapper.insert(item);
        return ItemResponse.from(item);
    }

    @Transactional
    public ItemResponse updateItem(String itemCd, ItemRequest request) {
        Item item = findItemOrThrow(itemCd);
        item.setItemNm(request.itemNm());
        item.setItemQnty(request.itemQnty());
        item.setUnitPrice(request.unitPrice());
        item.setSellPrice(request.sellPrice());
        item.setItemSeCd(request.itemSeCd());
        item.setItemCateCd(request.itemCateCd());
        item.setUnitPriceNet(request.unitPriceNet());
        item.setUnitPriceVat(request.unitPriceVat());
        item.setSellPriceNet(request.sellPriceNet());
        item.setSellPriceVat(request.sellPriceVat());
        itemMapper.update(item);
        return ItemResponse.from(item);
    }

    @Transactional
    public void deleteItem(String itemCd) {
        findItemOrThrow(itemCd);
        itemMapper.delete(itemCd);
    }

    public Item findItemOrThrow(String itemCd) {
        Item item = itemMapper.selectByItemCd(itemCd);
        if (item == null) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }
        return item;
    }
}
