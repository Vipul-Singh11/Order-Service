package com.stock.orderservice.client;

import com.stock.orderservice.dto.PortfolioResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
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

    public void reserveShares(Long userId, String stockSymbol, Integer quantity){

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/portfolio/reserve")
                        .queryParam("userId", userId)
                        .queryParam("stockSymbol", stockSymbol)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void releaseReservedShares(Long userId, String stockSymbol, Integer quantity){

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/portfolio/release")
                        .queryParam("userId", userId)
                        .queryParam("stockSymbol", stockSymbol)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }

    public void consumeReservedShares(Long userId, String stockSymbol, Integer quantity){

        webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/portfolio/consume")
                        .queryParam("userId", userId)
                        .queryParam("stockSymbol", stockSymbol)
                        .queryParam("quantity", quantity)
                        .build())
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}