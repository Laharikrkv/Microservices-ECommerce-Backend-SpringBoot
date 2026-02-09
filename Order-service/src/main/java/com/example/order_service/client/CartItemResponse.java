package com.example.order_service.client;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {
	
    private Long productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
   


}