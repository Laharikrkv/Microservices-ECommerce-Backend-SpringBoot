package com.example.order_service.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.OrderDto;
import com.example.order_service.entity.OrderItems;
import com.example.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
		private final OrderService orderService;
		
		//http://localhost:8083/api/orders/1/cartItem/1
		@PostMapping("/{userId}/cartItem/{itemId}")
		@PreAuthorize("hasAuthority('write:orders')")
		public ResponseEntity<String> placeOrder(@PathVariable Long itemId, @PathVariable Long userId){
			String res = orderService.placeOrder(itemId, userId);
			return ResponseEntity.ok(res);
		}
		
		@GetMapping("/user/{userId}")
		 @PreAuthorize("hasAuthority('read:orders')")
		public ResponseEntity<List<OrderDto>> getOrderitems(@PathVariable Long userId){
			List<OrderDto> items = orderService.getOrders(userId);
			return ResponseEntity.ok(items);
		}
		
		@GetMapping("/{id}")
		 @PreAuthorize("hasAuthority('manage:orders')")
		public ResponseEntity<String> setDelivered( @PathVariable Long orderId, 
																@RequestParam Long productId, 
																@RequestParam BigDecimal price) {
			String res = orderService.setToDelivered(orderId, productId, price);
			return ResponseEntity.ok(res);
		}
}

