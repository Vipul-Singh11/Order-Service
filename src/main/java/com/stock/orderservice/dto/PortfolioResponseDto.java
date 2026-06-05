package com.stock.orderservice.dto;

import lombok.Data;

@Data
public class PortfolioResponseDto {

    private Long userId;
    private String stockSymbol;
    private Integer quantity;
}