package com.stock.orderservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean(name = "matchingEngineWebClient")
    public WebClient matchingEngineWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8084")
                .build();
    }

    @Bean(name = "userServiceWebClient")
    public WebClient userServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build();
    }

    @Bean(name = "portfolioServiceWebClient")
    public WebClient portfolioServiceWebClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8085")
                .build();
    }
}
