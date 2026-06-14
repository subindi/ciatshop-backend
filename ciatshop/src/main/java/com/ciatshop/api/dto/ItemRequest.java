package com.ciatshop.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ItemRequest(
        @NotBlank(message = "상품 코드는 필수입니다.") String itemCd,
        @NotBlank(message = "상품명은 필수입니다.") String itemNm,
        @Min(value = 0, message = "재고 수량은 0 이상이어야 합니다.") int itemQnty,
        @Min(value = 0, message = "매입 단가는 0 이상이어야 합니다.") int unitPrice,
        @Min(value = 0, message = "판매가는 0 이상이어야 합니다.") int sellPrice,
        String itemSeCd,
        String itemCateCd,
        Integer unitPriceNet,
        Integer unitPriceVat,
        Integer sellPriceNet,
        Integer sellPriceVat
) {}
