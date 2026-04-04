package com.stock.orderservice.client;

import com.stock.orderservice.dto.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MatchingEngineClient {

    private final WebClient webClient;

    public void sendOrderToMatchingEngine(OrderRequestDto orderRequestDto, Long orderId) {

        Map<String, Object> requestBody = new HashMap<>();

        // ✅ Match EXACT DTO fields
        requestBody.put("orderId", orderId);
        requestBody.put("stockSymbol", orderRequestDto.getStockSymbol());
        requestBody.put("quantity", orderRequestDto.getQuantity());
        requestBody.put("price", orderRequestDto.getPrice());
        requestBody.put("orderType", orderRequestDto.getOrderType());
        requestBody.put("timestamp", LocalDateTime.now());

        webClient.post()
                .uri("/api/v1/matching-engine/match") // ✅ correct endpoint
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}