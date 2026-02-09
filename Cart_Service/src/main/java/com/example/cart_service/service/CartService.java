package com.example.cart_service.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.cart_service.client.ProductClient;
import com.example.cart_service.client.ProductDto;
import com.example.cart_service.dto.CartItemResponse;
import com.example.cart_service.dto.CartResponse;
import com.example.cart_service.repository.CartItemRepository;
import com.example.cart_service.repository.CartRepository;

import aj.org.objectweb.asm.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.example.cart_service.entity.Cart;
import com.example.cart_service.entity.CartItems;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {
		
		private final CartRepository cartRepository;
		private final CartItemRepository cartItemRepository;
		private final ProductClient productClient;
		
		@CircuitBreaker(name = "cart-service")
		public String addToCart(Long userId, Long productId, Integer quantity) {
			
			ProductDto product = productClient.getProduct(productId);
			if (product == null) {
				throw new RuntimeException("Product not found " + productId);
			}
			
			Cart cart = cartRepository.findByUserId(userId)
										.orElseGet(() -> {
											Cart newCart = new Cart();
											newCart.setUserId(userId);
											return cartRepository.save(newCart);
											
										});
			
			 CartItems existing = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);
			
			 if (existing != null) {
				 existing.setQuantity(existing.getQuantity() + quantity);
			 }else {
				 CartItems item = new CartItems();
				 item.setProductId(productId);
				 item.setQuantity(quantity);
				 item.setCart(cart);
				 item.setPrice(product.getPrice());
				 cart.getCartItems().add(item);
			 }
			 
			 cartRepository.save(cart);
			 
			 return "Product Added Successfully";
		}

		
		public CartResponse getCart(Long userId) {
			
				Cart cart = cartRepository.findByUserId(userId)
											.orElseThrow(() -> new RuntimeException("Cart Not found for user" + userId));
				
				List<CartItemResponse> cartItems = new ArrayList<>();
				Double grandTotal = 0.0;
				
				for (CartItems items : cart.getCartItems()) {
					ProductDto product = productClient.getProduct(items.getProductId());
					
					CartItemResponse itemRes = new CartItemResponse();
					
					itemRes.setProductId(items.getProductId());
					itemRes.setProductName(product.getName());
					itemRes.setPrice(product.getPrice());
					itemRes.setQuantity(items.getQuantity());
					itemRes.setTotal(product.getPrice() * items.getQuantity());
					
					grandTotal += itemRes.getTotal();
					
					
					cartItems.add(itemRes);
				}
				
				
		        CartResponse cartResponse = new CartResponse();
		        cartResponse.setUserId(userId);
		        cartResponse.setItems(cartItems);
		        cartResponse.setGrandTotal(grandTotal);
		        
		        return cartResponse;
				
		}
		
		public CartItemResponse getItem(Long id) {
			
				CartItems item = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Product is not in the cart"));
				
				CartItemResponse itemRes = new CartItemResponse();
				ProductDto product = productClient.getProduct(item.getProductId());
				
				itemRes.setProductId(item.getProductId());
				itemRes.setProductName(product.getName());
				itemRes.setPrice(product.getPrice());
				itemRes.setQuantity(item.getQuantity());
				itemRes.setTotal(product.getPrice() * item.getQuantity());
				
				return itemRes;
				
		}
	
}
