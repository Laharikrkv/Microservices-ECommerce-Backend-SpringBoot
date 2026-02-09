package com.example.cart_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cart_service.dto.CartItemResponse;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.service.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add product to cart --- http://localhost:8083/api/cart/1/add
    
    @PostMapping("/{userId}/add")
    @PreAuthorize("hasAuthority('write:cart')" )
    public ResponseEntity<String> addToCart(
            @PathVariable Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }
    
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('read:cart')" )
    public ResponseEntity<CartResponse> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }
    
    @GetMapping("/{userId}/cart/{id}")
    @PreAuthorize("hasAuthority('read:cart')" )
    public ResponseEntity<CartItemResponse> getItem(@PathVariable Long id, @PathVariable Long userId){
    	CartItemResponse item = cartService.getItem(id);
    	return ResponseEntity.ok(item);
    }
}