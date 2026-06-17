package com.ciatshop.api.mapper;

import com.ciatshop.api.domain.Item;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ItemMapper {

    List<Item> selectAll();

    Item selectByItemCd(String itemCd);

    int insert(Item item);

    int update(Item item);

    int delete(String itemCd);

    int deductStock(@Param("itemCd") String itemCd, @Param("qty") int qty);
}
