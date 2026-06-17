package com.ciatshop.api.mapper;

import com.ciatshop.api.domain.Sales;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesMapper {

    List<Sales> selectByYmd(@Param("salesYmd") String salesYmd,
                            @Param("salesEventSeq") Integer salesEventSeq);

    Sales selectBySeq(Integer salesSeq);

    List<Sales> selectByEventSeq(Integer salesEventSeq);

    List<Sales> selectByFilter(@Param("salesYear") String salesYear,
                               @Param("salesEventSeq") Integer salesEventSeq);

    List<String> selectDistinctYears();

    Sales selectByItemDateEvent(@Param("itemCd") String itemCd,
                                @Param("salesYmd") String salesYmd,
                                @Param("salesEventSeq") Integer salesEventSeq);

    int updateQuantity(@Param("salesSeq") Integer salesSeq, @Param("delta") int delta);

    int insert(Sales sales);

    int delete(Integer salesSeq);
}
