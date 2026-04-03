package com.stock.orderservice.dto;

import com.stock.orderservice.entity.OrderType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDto {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Stock symbol is required")
    private String stockSymbol;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Order type is required")
    private OrderType orderType;
}