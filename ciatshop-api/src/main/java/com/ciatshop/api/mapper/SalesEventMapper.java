package com.ciatshop.api.mapper;

import com.ciatshop.api.domain.SalesEvent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SalesEventMapper {

    List<SalesEvent> selectAll();

    List<SalesEvent> selectByYear(String salesYear);

    SalesEvent selectBySeq(Integer salesEventSeq);

    int insert(SalesEvent salesEvent);

    int update(SalesEvent salesEvent);

    int delete(Integer salesEventSeq);
}
