package com.stock.orderservice.client;

import com.stock.orderservice.dto.UserResponseDto;
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
}