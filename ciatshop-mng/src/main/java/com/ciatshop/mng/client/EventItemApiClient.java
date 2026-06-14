package com.ciatshop.mng.client;

import com.ciatshop.mng.dto.ApiResponse;
import com.ciatshop.mng.dto.EventItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class EventItemApiClient {

    private final RestClient restClient;

    public List<EventItemDto> getEventItems(Integer salesEventSeq) {
        ApiResponse<List<EventItemDto>> response = restClient.get()
                .uri("/api/v1/event-items?salesEventSeq={seq}", salesEventSeq)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : List.of();
    }

    public EventItemDto addEventItem(Integer salesEventSeq, String itemCd, int inqnty,
                                     Integer sellPrice, String useYn) {
        ApiResponse<EventItemDto> response = restClient.post()
                .uri("/api/v1/event-items")
                .body(Map.of(
                        "salesEventSeq", salesEventSeq,
                        "itemCd", itemCd,
                        "inqnty", inqnty,
                        "sellPrice", sellPrice != null ? sellPrice : 0,
                        "useYn", useYn != null ? useYn : "Y"
                ))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
        return response != null ? response.data() : null;
    }
}
