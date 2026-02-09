package com.example.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Product-ServiceE", url = "http://localhost:8080")
public interface ProductClient {
	
    @GetMapping("api/products/validate/{id}/{quantity}")
    public String validateProduct(
            @PathVariable Long id,
            @PathVariable Integer quantity);

}
