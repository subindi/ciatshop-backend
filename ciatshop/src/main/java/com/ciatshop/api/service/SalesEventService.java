package com.ciatshop.api.service;

import com.ciatshop.api.domain.SalesEvent;
import com.ciatshop.api.dto.SalesEventRequest;
import com.ciatshop.api.dto.SalesEventResponse;
import com.ciatshop.api.global.BusinessException;
import com.ciatshop.api.global.ErrorCode;
import com.ciatshop.api.mapper.SalesEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesEventService {

    private final SalesEventMapper salesEventMapper;

    public List<SalesEventResponse> getSalesEvents() {
        return salesEventMapper.selectAll().stream()
                .map(SalesEventResponse::from)
                .toList();
    }

    public List<SalesEventResponse> getSalesEventsByYear(String salesYear) {
        return salesEventMapper.selectByYear(salesYear).stream()
                .map(SalesEventResponse::from)
                .toList();
    }

    public SalesEventResponse getSalesEvent(Integer salesEventSeq) {
        return SalesEventResponse.from(findEventOrThrow(salesEventSeq));
    }

    @Transactional
    public SalesEventResponse createSalesEvent(SalesEventRequest request) {
        SalesEvent event = SalesEvent.builder()
                .salesEventNm(request.salesEventNm())
                .salesEventStrYmd(request.salesEventStrYmd())
                .salesEventEndYmd(request.salesEventEndYmd())
                .profitAmt(request.profitAmt())
                .profitSeCd(request.profitSeCd())
                .profitVatYn(request.profitVatYn())
                .build();
        salesEventMapper.insert(event);
        return SalesEventResponse.from(event);
    }

    @Transactional
    public SalesEventResponse updateSalesEvent(Integer salesEventSeq, SalesEventRequest request) {
        SalesEvent event = findEventOrThrow(salesEventSeq);
        event.setSalesEventNm(request.salesEventNm());
        event.setSalesEventStrYmd(request.salesEventStrYmd());
        event.setSalesEventEndYmd(request.salesEventEndYmd());
        event.setProfitAmt(request.profitAmt());
        event.setProfitSeCd(request.profitSeCd());
        event.setProfitVatYn(request.profitVatYn());
        salesEventMapper.update(event);
        return SalesEventResponse.from(event);
    }

    @Transactional
    public void deleteSalesEvent(Integer salesEventSeq) {
        findEventOrThrow(salesEventSeq);
        salesEventMapper.delete(salesEventSeq);
    }

    public SalesEvent findEventOrThrow(Integer salesEventSeq) {
        SalesEvent event = salesEventMapper.selectBySeq(salesEventSeq);
        if (event == null) {
            throw new BusinessException(ErrorCode.SALES_EVENT_NOT_FOUND);
        }
        return event;
    }
}
