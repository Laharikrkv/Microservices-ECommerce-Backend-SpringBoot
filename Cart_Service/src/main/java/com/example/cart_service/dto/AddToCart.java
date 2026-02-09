package com.example.cart_service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCart {
	
    private Long productId;
    private Integer quantity;


}
