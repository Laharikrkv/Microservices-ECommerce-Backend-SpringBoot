package com.example.order_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class OrderDto {
    private Long orderId;
    private Long userId;
    private List<OrderItemDto> items;
}

