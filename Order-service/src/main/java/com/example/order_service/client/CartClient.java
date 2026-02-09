package com.example.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@FeignClient(name = "Cart-Service", url = "http://localhost:8081")
public interface CartClient {
		
	@GetMapping("/api/cart/{userId}/cart/{id}")
    public CartItemResponse getItem(@PathVariable Long id, @PathVariable Long userId);
}
