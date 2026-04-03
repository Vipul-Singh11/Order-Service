package com.stock.orderservice.service;

import com.stock.orderservice.dto.OrderRequestDto;
import com.stock.orderservice.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderById(Long id);
}