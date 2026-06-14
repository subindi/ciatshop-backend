package com.ciatshop.mng.client;

import com.ciatshop.mng.dto.ApiResponse;
import com.ciatshop.mng.dto.SalesEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SalesEventApiClient {

    private final RestClient restClient;

    public List<SalesEventDto> getSalesEvents() {
        ApiResponse<List<SalesEventDto>> response = restClient.get()
                .uri("/api/v1/sales-events")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : List.of();
    }

    public SalesEventDto getSalesEvent(Integer salesEventSeq) {
        ApiResponse<SalesEventDto> response = restClient.get()
                .uri("/api/v1/sales-events/{seq}", salesEventSeq)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : null;
    }
}
