package com.stock.orderservice.service;

import com.stock.orderservice.dto.OrderRequestDto;
import com.stock.orderservice.dto.OrderResponseDto;
import com.stock.orderservice.entity.OrderStatus;

import java.util.List;

public interface OrderService {

    OrderResponseDto placeOrder(OrderRequestDto requestDto);

    List<OrderResponseDto> getAllOrders();

    OrderResponseDto getOrderById(Long id);

    OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status);

    OrderResponseDto cancelOrder(Long orderId);
}