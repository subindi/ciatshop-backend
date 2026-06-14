package com.ciatshop.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EventItemRequest(
        @NotNull(message = "판매이벤트SEQ는 필수입니다.") Integer salesEventSeq,
        @NotBlank(message = "상품코드는 필수입니다.") String itemCd,
        @Min(value = 0, message = "입고수량은 0 이상이어야 합니다.") int inqnty,
        Integer sellPrice,
        String useYn
) {}
