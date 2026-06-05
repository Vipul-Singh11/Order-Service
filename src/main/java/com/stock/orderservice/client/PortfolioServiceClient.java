package com.stock.orderservice.client;

import com.stock.orderservice.dto.PortfolioResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
public class PortfolioServiceClient {

    private final WebClient webClient;

    public PortfolioServiceClient(
            @Qualifier("portfolioServiceWebClient")
            WebClient webClient) {

        this.webClient = webClient;
    }

    public List<PortfolioResponseDto> getPortfolio(Long userId) {

        return webClient.get()
                .uri("/api/portfolio/" + userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<PortfolioResponseDto>>() {})
                .block();
    }
}