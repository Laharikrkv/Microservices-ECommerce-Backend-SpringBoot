package com.example.cart_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.cart_service.entity.CartItems;

public interface CartItemRepository extends JpaRepository<CartItems, Long>{
		CartItems findByCartIdAndProductId(Long id, Long productId);
		Optional<CartItems> findById(Long id);
}
