package com.ciatshop.mng.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {}
