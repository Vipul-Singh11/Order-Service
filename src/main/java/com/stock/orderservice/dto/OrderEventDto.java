package com.stock.orderservice.dto;

import com.stock.orderservice.entity.OrderExecutionType;
import com.stock.orderservice.entity.OrderType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderEventDto {

    private Long orderId;

    private Long userId;   // 🔥 ADD THIS (CRITICAL)

    private String stockSymbol;

    private int quantity;

    private BigDecimal price;

    private OrderType orderType;

    private LocalDateTime timestamp;

    private OrderExecutionType executionType;
}
