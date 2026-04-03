package com.stock.orderservice.service.impl;

import com.stock.orderservice.dto.OrderRequestDto;
import com.stock.orderservice.dto.OrderResponseDto;
import com.stock.orderservice.entity.Order;
import com.stock.orderservice.entity.OrderStatus;
import com.stock.orderservice.exception.ResourceNotFoundException;
import com.stock.orderservice.repository.OrderRepository;
import com.stock.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {

        Order order = Order.builder()
                .userId(requestDto.getUserId())
                .stockSymbol(requestDto.getStockSymbol())
                .quantity(requestDto.getQuantity())
                .price(requestDto.getPrice())
                .orderType(requestDto.getOrderType())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        // 🔥 EVENT PLACEHOLDER (VERY IMPORTANT)
        log.info("Order created with id: {}, type: {}, symbol: {}, quantity: {}",
                savedOrder.getId(),
                savedOrder.getOrderType(),
                savedOrder.getStockSymbol(),
                savedOrder.getQuantity()
        );

        return mapToDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {

        List<Order> orders = orderRepository.findAll();

        return orders.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));

        return mapToDto(order);
    }

    // 🔹 Common mapper
    private OrderResponseDto mapToDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .stockSymbol(order.getStockSymbol())
                .quantity(order.getQuantity())
                .price(order.getPrice())
                .orderType(order.getOrderType())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }
}