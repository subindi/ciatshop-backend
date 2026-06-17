package com.ciatshop.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record SalesEventRequest(
        @NotBlank(message = "이벤트명은 필수입니다.") String salesEventNm,

        @NotBlank(message = "이벤트 시작일은 필수입니다.")
        @Pattern(regexp = "\\d{8}", message = "시작일은 8자리 숫자여야 합니다. (예: 20240101)")
        String salesEventStrYmd,

        @NotBlank(message = "이벤트 종료일은 필수입니다.")
        @Pattern(regexp = "\\d{8}", message = "종료일은 8자리 숫자여야 합니다. (예: 20240131)")
        String salesEventEndYmd,

        Integer profitAmt,
        String profitSeCd,
        String profitVatYn
) {}
