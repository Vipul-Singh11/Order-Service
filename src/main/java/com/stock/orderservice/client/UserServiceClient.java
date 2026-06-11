package com.stock.orderservice.client;

import com.stock.orderservice.dto.UserResponseDto;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class UserServiceClient {

    private final WebClient webClient;

    public UserServiceClient(
            @Qualifier("userServiceWebClient")
            WebClient webClient) {

        this.webClient = webClient;
    }

    public UserResponseDto getUser(Long userId) {

        return webClient.get()
                .uri("/api/users/" + userId)
                .retrieve()
                .bodyToMono(UserResponseDto.class)
                .block();
    }

    public void reserveAmount(Long userId, BigDecimal amount) {

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/reserve")
                        .queryParam("userId", userId)
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void releaseAmount(Long userId, BigDecimal amount) {

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/release")
                        .queryParam("userId", userId)
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void consumeReservedAmount(Long userId, BigDecimal amount) {

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/users/consume")
                        .queryParam("userId", userId)
                        .queryParam("amount", amount)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}