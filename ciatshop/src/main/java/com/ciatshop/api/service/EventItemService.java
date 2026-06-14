package com.ciatshop.api.service;

import com.ciatshop.api.domain.EventItem;
import com.ciatshop.api.dto.EventItemRequest;
import com.ciatshop.api.dto.EventItemResponse;
import com.ciatshop.api.global.BusinessException;
import com.ciatshop.api.global.ErrorCode;
import com.ciatshop.api.mapper.EventItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventItemService {

    private final EventItemMapper eventItemMapper;

    public List<EventItemResponse> getEventItems(Integer salesEventSeq) {
        return eventItemMapper.selectByEvent(salesEventSeq).stream()
                .map(EventItemResponse::from)
                .toList();
    }

    @Transactional
    public EventItemResponse addEventItem(EventItemRequest request) {
        if (eventItemMapper.existsByEventAndItem(request.salesEventSeq(), request.itemCd())) {
            throw new BusinessException(ErrorCode.DUPLICATE_EVENT_ITEM);
        }
        EventItem eventItem = EventItem.builder()
                .salesEventSeq(request.salesEventSeq())
                .itemCd(request.itemCd())
                .inqnty(request.inqnty())
                .sellPrice(request.sellPrice())
                .useYn(request.useYn() != null ? request.useYn() : "Y")
                .build();
        eventItemMapper.insert(eventItem);
        return EventItemResponse.from(eventItemMapper.selectBySeq(eventItem.getEventItemSeq()));
    }

    @Transactional
    public EventItemResponse updateEventItem(Integer eventItemSeq, EventItemRequest request) {
        EventItem eventItem = findOrThrow(eventItemSeq);
        eventItem.setInqnty(request.inqnty());
        eventItem.setSellPrice(request.sellPrice());
        eventItem.setUseYn(request.useYn() != null ? request.useYn() : eventItem.getUseYn());
        eventItemMapper.update(eventItem);
        return EventItemResponse.from(eventItemMapper.selectBySeq(eventItemSeq));
    }

    @Transactional
    public void deleteEventItem(Integer eventItemSeq) {
        findOrThrow(eventItemSeq);
        eventItemMapper.delete(eventItemSeq);
    }

    private EventItem findOrThrow(Integer eventItemSeq) {
        EventItem ei = eventItemMapper.selectBySeq(eventItemSeq);
        if (ei == null) throw new BusinessException(ErrorCode.EVENT_ITEM_NOT_FOUND);
        return ei;
    }
}
