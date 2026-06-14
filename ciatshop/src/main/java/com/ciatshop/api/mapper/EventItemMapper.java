package com.ciatshop.api.mapper;

import com.ciatshop.api.domain.EventItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EventItemMapper {

    List<EventItem> selectByEvent(@Param("salesEventSeq") Integer salesEventSeq);

    EventItem selectBySeq(@Param("eventItemSeq") Integer eventItemSeq);

    boolean existsByEventAndItem(@Param("salesEventSeq") Integer salesEventSeq,
                                 @Param("itemCd") String itemCd);

    int insert(EventItem eventItem);

    int update(EventItem eventItem);

    int delete(@Param("eventItemSeq") Integer eventItemSeq);
}
