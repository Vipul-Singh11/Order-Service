package com.stock.orderservice.service.impl;

import com.stock.orderservice.dto.OrderEventDto;
import com.stock.orderservice.dto.OrderRequestDto;
import com.stock.orderservice.dto.OrderResponseDto;
import com.stock.orderservice.entity.Order;
import com.stock.orderservice.entity.OrderStatus;
import com.stock.orderservice.exception.OrderValidationException;
import com.stock.orderservice.exception.ResourceNotFoundException;
import com.stock.orderservice.repository.OrderRepository;
import com.stock.orderservice.service.OrderService;
import com.stock.orderservice.client.MatchingEngineClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.stock.orderservice.client.PortfolioServiceClient;
import com.stock.orderservice.client.UserServiceClient;
import com.stock.orderservice.dto.PortfolioResponseDto;
import com.stock.orderservice.dto.UserResponseDto;
import com.stock.orderservice.entity.OrderType;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final MatchingEngineClient matchingEngineClient;
    private final UserServiceClient userServiceClient;
    private final PortfolioServiceClient portfolioServiceClient;

    @Override
    public OrderResponseDto placeOrder(OrderRequestDto requestDto) {

        if (requestDto.getOrderType() == OrderType.BUY) {
            validateBuyOrder(requestDto);
        }

        if (requestDto.getOrderType() == OrderType.SELL) {
            validateSellOrder(requestDto);
        }

        Order order = Order.builder()
                .userId(requestDto.getUserId())
                .stockSymbol(requestDto.getStockSymbol())
                .quantity(requestDto.getQuantity())
                .price(requestDto.getPrice())
                .orderType(requestDto.getOrderType())
                .status(OrderStatus.PENDING)
                .build();

        Order savedOrder = orderRepository.save(order);

        log.info("🔥 Sending order to Matching Engine...");
        
        OrderEventDto eventDto = OrderEventDto.builder()
                .orderId(savedOrder.getId())
                .userId(savedOrder.getUserId()) 
                .stockSymbol(savedOrder.getStockSymbol())
                .quantity(savedOrder.getQuantity())
                .price(savedOrder.getPrice())
                .orderType(savedOrder.getOrderType())
                .timestamp(java.time.LocalDateTime.now())
                .build();

        try {
            matchingEngineClient.sendOrderToMatchingEngine(eventDto);
            log.info("Order sent to Matching Engine successfully");
        } catch (Exception e) {
            log.error("Failed to send order to Matching Engine: {}", e.getMessage());
        }

        return mapToDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToDto(order);
    }

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

    @Override
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId));

        order.setStatus(status);

        Order updatedOrder = orderRepository.save(order);

        log.info("Order {} updated to {}", orderId, status);

        return mapToDto(updatedOrder);
    }

    private void validateBuyOrder(OrderRequestDto requestDto) {

        UserResponseDto user =
                userServiceClient.getUser(
                        requestDto.getUserId());

        BigDecimal requiredAmount =
                requestDto.getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        requestDto.getQuantity()));

        if (user.getWalletBalance()
                .compareTo(requiredAmount) < 0) {

            throw new OrderValidationException(
        "Insufficient wallet balance");
        }
    }

    private void validateSellOrder(OrderRequestDto requestDto) {

        List<PortfolioResponseDto> holdings =
                portfolioServiceClient.getPortfolio(
                        requestDto.getUserId());

        PortfolioResponseDto holding =
                holdings.stream()
                        .filter(p ->
                                p.getStockSymbol()
                                        .equalsIgnoreCase(
                                                requestDto.getStockSymbol()))
                        .findFirst()
                        .orElse(null);

        if (holding == null) {
            throw new OrderValidationException(
            "No holdings found for stock "
                    + requestDto.getStockSymbol());
        }

        if (holding.getQuantity()
                < requestDto.getQuantity()) {

            throw new OrderValidationException(
        "Insufficient stock holdings");
        }
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + orderId));

        if (order.getStatus() == OrderStatus.EXECUTED) {
            throw new OrderValidationException(
                    "Executed order cannot be cancelled");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderValidationException(
                    "Order is already cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order updatedOrder = orderRepository.save(order);

        try {

            matchingEngineClient.cancelOrder(orderId);

            log.info(
                    "Order {} removed from Matching Engine",
                    orderId);

        } catch (Exception e) {

            log.error(
                    "Failed to remove order from Matching Engine: {}",
                    e.getMessage());
        }

        log.info("Order {} cancelled successfully", orderId);

        return mapToDto(updatedOrder);
    }
}
