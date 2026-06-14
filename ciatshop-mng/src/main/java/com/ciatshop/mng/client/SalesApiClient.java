package com.ciatshop.mng.client;

import com.ciatshop.mng.dto.ApiResponse;
import com.ciatshop.mng.dto.SalesDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SalesApiClient {

    private final RestClient restClient;

    public List<SalesDto> getSalesByDate(String salesYmd) {
        ApiResponse<List<SalesDto>> response = restClient.get()
                .uri("/api/v1/sales?salesYmd={ymd}", salesYmd)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null && response.data() != null ? response.data() : List.of();
    }

    public SalesDto getSales(Integer salesSeq) {
        ApiResponse<SalesDto> response = restClient.get()
                .uri("/api/v1/sales/{seq}", salesSeq)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : null;
    }

    public List<SalesDto> searchSales(String salesYear, Integer salesEventSeq) {
        ApiResponse<List<SalesDto>> response = restClient.get()
                .uri(b -> {
                    b.path("/api/v1/sales/search");
                    if (salesYear != null && !salesYear.isBlank()) b.queryParam("salesYear", salesYear);
                    if (salesEventSeq != null) b.queryParam("salesEventSeq", salesEventSeq);
                    return b.build();
                })
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null && response.data() != null ? response.data() : List.of();
    }

    public List<String> getYears() {
        ApiResponse<List<String>> response = restClient.get()
                .uri("/api/v1/sales/years")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null && response.data() != null ? response.data() : List.of();
    }
}
