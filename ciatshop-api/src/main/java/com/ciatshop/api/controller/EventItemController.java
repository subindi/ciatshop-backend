package com.ciatshop.api.controller;

import com.ciatshop.api.dto.EventItemRequest;
import com.ciatshop.api.dto.EventItemResponse;
import com.ciatshop.api.global.ApiResponse;
import com.ciatshop.api.service.EventItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-items")
@RequiredArgsConstructor
public class EventItemController {

    private final EventItemService eventItemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EventItemResponse>>> getEventItems(
            @RequestParam Integer salesEventSeq) {
        return ResponseEntity.ok(ApiResponse.ok(eventItemService.getEventItems(salesEventSeq)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EventItemResponse>> addEventItem(
            @Valid @RequestBody EventItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("이벤트 상품이 추가되었습니다.", eventItemService.addEventItem(request)));
    }

    @PutMapping("/{eventItemSeq}")
    public ResponseEntity<ApiResponse<EventItemResponse>> updateEventItem(
            @PathVariable Integer eventItemSeq,
            @Valid @RequestBody EventItemRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("이벤트 상품이 수정되었습니다.",
                eventItemService.updateEventItem(eventItemSeq, request)));
    }

    @DeleteMapping("/{eventItemSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteEventItem(@PathVariable Integer eventItemSeq) {
        eventItemService.deleteEventItem(eventItemSeq);
        return ResponseEntity.ok(ApiResponse.ok("이벤트 상품이 삭제되었습니다.", null));
    }
}
