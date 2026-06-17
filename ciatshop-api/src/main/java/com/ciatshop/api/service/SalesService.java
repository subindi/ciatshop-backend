package com.ciatshop.api.service;

import com.ciatshop.api.domain.Sales;
import com.ciatshop.api.dto.SalesAdjustRequest;
import com.ciatshop.api.dto.SalesRequest;
import com.ciatshop.api.dto.SalesResponse;
import com.ciatshop.api.global.BusinessException;
import com.ciatshop.api.global.ErrorCode;
import com.ciatshop.api.mapper.ItemMapper;
import com.ciatshop.api.mapper.SalesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesService {

    private final SalesMapper salesMapper;
    private final ItemMapper itemMapper;

    public List<SalesResponse> getSalesByDate(String salesYmd, Integer salesEventSeq) {
        return salesMapper.selectByYmd(salesYmd, salesEventSeq).stream()
                .map(SalesResponse::from)
                .toList();
    }

    public SalesResponse getSales(Integer salesSeq) {
        return SalesResponse.from(findSalesOrThrow(salesSeq));
    }

    public List<SalesResponse> getSalesByEvent(Integer salesEventSeq) {
        return salesMapper.selectByEventSeq(salesEventSeq).stream()
                .map(SalesResponse::from)
                .toList();
    }

    public List<SalesResponse> searchSales(String salesYear, Integer salesEventSeq) {
        return salesMapper.selectByFilter(salesYear, salesEventSeq).stream()
                .map(SalesResponse::from)
                .toList();
    }

    public List<String> getSalesYears() {
        return salesMapper.selectDistinctYears();
    }

    @Transactional
    public SalesResponse createSales(SalesRequest request) {
        // 상품 존재 여부 확인
        if (itemMapper.selectByItemCd(request.itemCd()) == null) {
            throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
        }

        itemMapper.deductStock(request.itemCd(), request.salesQnty());

        Sales sales = Sales.builder()
                .salesYmd(request.salesYmd())
                .itemCd(request.itemCd())
                .salesQnty(request.salesQnty())
                .salesPrice(request.salesPrice())
                .orderSeCd(request.orderSeCd())
                .salesSeCd(request.salesSeCd())
                .salesPlaceSeCd(request.salesPlaceSeCd())
                .salesEventSeq(request.salesEventSeq())
                .build();

        salesMapper.insert(sales);

        // 등록 후 JOIN 포함 전체 데이터 재조회
        return SalesResponse.from(salesMapper.selectBySeq(sales.getSalesSeq()));
    }

    @Transactional
    public SalesResponse adjustSales(SalesAdjustRequest request) {
        Sales existing = salesMapper.selectByItemDateEvent(
                request.itemCd(), request.salesYmd(), request.salesEventSeq());

        if (existing != null) {
            salesMapper.updateQuantity(existing.getSalesSeq(), request.delta());
            return SalesResponse.from(salesMapper.selectBySeq(existing.getSalesSeq()));
        } else if (request.delta() > 0) {
            if (itemMapper.selectByItemCd(request.itemCd()) == null) {
                throw new BusinessException(ErrorCode.ITEM_NOT_FOUND);
            }
            Sales sales = Sales.builder()
                    .salesYmd(request.salesYmd())
                    .itemCd(request.itemCd())
                    .salesQnty(request.delta())
                    .salesEventSeq(request.salesEventSeq())
                    .build();
            salesMapper.insert(sales);
            return SalesResponse.from(salesMapper.selectBySeq(sales.getSalesSeq()));
        }
        return null;
    }

    @Transactional
    public void deleteSales(Integer salesSeq) {
        findSalesOrThrow(salesSeq);
        salesMapper.delete(salesSeq);
    }

    private Sales findSalesOrThrow(Integer salesSeq) {
        Sales sales = salesMapper.selectBySeq(salesSeq);
        if (sales == null) {
            throw new BusinessException(ErrorCode.SALES_NOT_FOUND);
        }
        return sales;
    }
}
