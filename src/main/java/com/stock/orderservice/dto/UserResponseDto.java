package com.stock.orderservice.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private BigDecimal walletBalance;
    private String role;
}