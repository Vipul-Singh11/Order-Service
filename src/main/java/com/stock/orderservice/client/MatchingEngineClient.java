package com.stock.orderservice.client;

import com.stock.orderservice.dto.OrderEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class MatchingEngineClient {

    private final WebClient webClient;

    public void sendOrderToMatchingEngine(OrderEventDto eventDto) {

        webClient.post()
                .uri("/api/v1/matching-engine/match")
                .bodyValue(eventDto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
