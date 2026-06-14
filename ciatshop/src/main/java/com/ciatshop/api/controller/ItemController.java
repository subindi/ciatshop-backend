package com.ciatshop.api.controller;

import com.ciatshop.api.dto.ItemRequest;
import com.ciatshop.api.dto.ItemResponse;
import com.ciatshop.api.global.ApiResponse;
import com.ciatshop.api.service.ItemService;
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
@RequestMapping("/api/v1/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ItemResponse>>> getItems() {
        return ResponseEntity.ok(ApiResponse.ok(itemService.getItems()));
    }

    @GetMapping("/{itemCd}")
    public ResponseEntity<ApiResponse<ItemResponse>> getItem(@PathVariable String itemCd) {
        return ResponseEntity.ok(ApiResponse.ok(itemService.getItem(itemCd)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ItemResponse>> createItem(@Valid @RequestBody ItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("상품이 등록되었습니다.", itemService.createItem(request)));
    }

    @PutMapping("/{itemCd}")
    public ResponseEntity<ApiResponse<ItemResponse>> updateItem(
            @PathVariable String itemCd,
            @Valid @RequestBody ItemRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("상품 정보가 수정되었습니다.", itemService.updateItem(itemCd, request)));
    }

    @DeleteMapping("/{itemCd}")
    public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable String itemCd) {
        itemService.deleteItem(itemCd);
        return ResponseEntity.ok(ApiResponse.ok("상품이 삭제되었습니다.", null));
    }
}
