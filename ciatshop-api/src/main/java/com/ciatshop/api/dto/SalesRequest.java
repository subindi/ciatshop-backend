package com.ciatshop.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SalesRequest(
        @NotBlank(message = "판매 일자는 필수입니다.")
        @Pattern(regexp = "\\d{8}", message = "판매 일자는 8자리 숫자여야 합니다. (예: 20240101)")
        String salesYmd,

        @NotBlank(message = "상품 코드는 필수입니다.") String itemCd,

        @Min(value = 1, message = "판매 수량은 1 이상이어야 합니다.") int salesQnty,

        @Min(value = 0, message = "판매 가격은 0 이상이어야 합니다.") int salesPrice,

        String orderSeCd,
        String salesSeCd,
        String salesPlaceSeCd,
        Integer salesEventSeq
) {}
