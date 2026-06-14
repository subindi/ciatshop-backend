package com.ciatshop.api.controller;

import com.ciatshop.api.dto.SalesEventRequest;
import com.ciatshop.api.dto.SalesEventResponse;
import com.ciatshop.api.global.ApiResponse;
import com.ciatshop.api.service.SalesEventService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales-events")
@RequiredArgsConstructor
public class SalesEventController {

    private final SalesEventService salesEventService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesEventResponse>>> getSalesEvents() {
        return ResponseEntity.ok(ApiResponse.ok(salesEventService.getSalesEvents()));
    }

    @GetMapping("/by-year/{year}")
    public ResponseEntity<ApiResponse<List<SalesEventResponse>>> getSalesEventsByYear(@PathVariable String year) {
        return ResponseEntity.ok(ApiResponse.ok(salesEventService.getSalesEventsByYear(year)));
    }

    @GetMapping("/{eventSeq}")
    public ResponseEntity<ApiResponse<SalesEventResponse>> getSalesEvent(@PathVariable Integer eventSeq) {
        return ResponseEntity.ok(ApiResponse.ok(salesEventService.getSalesEvent(eventSeq)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesEventResponse>> createSalesEvent(@Valid @RequestBody SalesEventRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("판매 이벤트가 등록되었습니다.", salesEventService.createSalesEvent(request)));
    }

    @PutMapping("/{eventSeq}")
    public ResponseEntity<ApiResponse<SalesEventResponse>> updateSalesEvent(
            @PathVariable Integer eventSeq,
            @Valid @RequestBody SalesEventRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("판매 이벤트가 수정되었습니다.", salesEventService.updateSalesEvent(eventSeq, request)));
    }

    @DeleteMapping("/{eventSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteSalesEvent(@PathVariable Integer eventSeq) {
        salesEventService.deleteSalesEvent(eventSeq);
        return ResponseEntity.ok(ApiResponse.ok("판매 이벤트가 삭제되었습니다.", null));
    }
}
