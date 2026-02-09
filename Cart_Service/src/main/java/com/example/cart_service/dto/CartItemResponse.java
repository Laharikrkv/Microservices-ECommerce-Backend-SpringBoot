package com.example.cart_service.dto;

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
    private Double price;
    private Integer quantity;
    private Double total;


}
