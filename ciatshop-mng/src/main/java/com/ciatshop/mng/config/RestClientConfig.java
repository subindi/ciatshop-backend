package com.ciatshop.mng.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${ciatshop.api.base-url}")
    private String apiBaseUrl;

    @Bean
    public RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiBaseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
