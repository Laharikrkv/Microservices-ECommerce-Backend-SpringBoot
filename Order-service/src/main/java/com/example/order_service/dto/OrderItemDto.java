package com.example.order_service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.example.order_service.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
	
		private Long productId;
		
		private Integer quantity;
		
		private BigDecimal Total;
		
		private OrderStatus status;

}
