package com.stock.orderservice.client;

import com.stock.orderservice.dto.OrderEventDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class MatchingEngineClient {

    private final WebClient webClient;

    public MatchingEngineClient(
            @Qualifier("matchingEngineWebClient")
            WebClient webClient) {

        this.webClient = webClient;
    }

    public void sendOrderToMatchingEngine(OrderEventDto eventDto) {

        webClient.post()
                .uri("/api/v1/matching-engine/match")
                .bodyValue(eventDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}