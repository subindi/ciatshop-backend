package com.ciatshop.api.controller;

import com.ciatshop.api.dto.SalesAdjustRequest;
import com.ciatshop.api.dto.SalesRequest;
import com.ciatshop.api.dto.SalesResponse;
import com.ciatshop.api.global.ApiResponse;
import com.ciatshop.api.service.SalesService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SalesResponse>>> getSalesByDate(
            @RequestParam String salesYmd,
            @RequestParam(required = false) Integer salesEventSeq) {
        return ResponseEntity.ok(ApiResponse.ok(salesService.getSalesByDate(salesYmd, salesEventSeq)));
    }

    @GetMapping("/{salesSeq}")
    public ResponseEntity<ApiResponse<SalesResponse>> getSales(@PathVariable Integer salesSeq) {
        return ResponseEntity.ok(ApiResponse.ok(salesService.getSales(salesSeq)));
    }

    @GetMapping("/events/{eventSeq}")
    public ResponseEntity<ApiResponse<List<SalesResponse>>> getSalesByEvent(@PathVariable Integer eventSeq) {
        return ResponseEntity.ok(ApiResponse.ok(salesService.getSalesByEvent(eventSeq)));
    }

    @GetMapping("/years")
    public ResponseEntity<ApiResponse<List<String>>> getSalesYears() {
        return ResponseEntity.ok(ApiResponse.ok(salesService.getSalesYears()));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SalesResponse>>> searchSales(
            @RequestParam(required = false) String salesYear,
            @RequestParam(required = false) Integer salesEventSeq) {
        return ResponseEntity.ok(ApiResponse.ok(salesService.searchSales(salesYear, salesEventSeq)));
    }

    @PostMapping("/adjust")
    public ResponseEntity<ApiResponse<SalesResponse>> adjustSales(@RequestBody SalesAdjustRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(salesService.adjustSales(request)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SalesResponse>> createSales(@Valid @RequestBody SalesRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("판매 내역이 등록되었습니다.", salesService.createSales(request)));
    }

    @DeleteMapping("/{salesSeq}")
    public ResponseEntity<ApiResponse<Void>> deleteSales(@PathVariable Integer salesSeq) {
        salesService.deleteSales(salesSeq);
        return ResponseEntity.ok(ApiResponse.ok("판매 내역이 삭제되었습니다.", null));
    }
}
