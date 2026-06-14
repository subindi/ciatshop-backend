package com.ciatshop.mng.client;

import com.ciatshop.mng.dto.ApiResponse;
import com.ciatshop.mng.dto.ItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemApiClient {

    private final RestClient restClient;

    public List<ItemDto> getItems() {
        ApiResponse<List<ItemDto>> response = restClient.get()
                .uri("/api/v1/items")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : List.of();
    }

    public ItemDto getItem(String itemCd) {
        ApiResponse<ItemDto> response = restClient.get()
                .uri("/api/v1/items/{itemCd}", itemCd)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : null;
    }
}
