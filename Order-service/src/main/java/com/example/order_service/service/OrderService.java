package com.example.order_service.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.order_service.client.CartClient;
import com.example.order_service.client.CartItemResponse;
import com.example.order_service.client.ProductClient;
import com.example.order_service.dto.OrderDto;
import com.example.order_service.dto.OrderItemDto;
import com.example.events.OrderPlacedEvent;
import com.example.order_service.entity.Order1;
import com.example.order_service.entity.OrderItems;
import com.example.order_service.entity.OrderStatus;
import com.example.order_service.repository.OrderItemsRepository;
import com.example.order_service.repository.OrderRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemsRepository orderItemsRepository;
	private final CartClient cartClient;
	private final ProductClient productClient;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	

	@CircuitBreaker(name = "order-service", fallbackMethod="fallbackForOrder")
	public String placeOrder(Long itemId, Long userId) {
	    CartItemResponse item = cartClient.getItem(itemId, userId);
	    String productValidation = productClient.validateProduct(item.getProductId(), item.getQuantity());
	    final org.slf4j.Logger log = LoggerFactory.getLogger(OrderService.class);

	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication instanceof JwtAuthenticationToken jwtAuth) {
	        String email = jwtAuth.getToken().getSubject();

	        if (productValidation.contains("Product")) {
	            OrderItems orderItem = new OrderItems();
	            orderItem.setProductId(item.getProductId());
	            orderItem.setQuantity(item.getQuantity());
	            orderItem.setStatus(OrderStatus.SHIPPED);
	            
	            orderItem.setTotal(item.getPrice().multiply(new BigDecimal(item.getQuantity())));

	            Order1 order = new Order1();
	            order.setUserId(userId);
	            order.getItems().add(orderItem);
	            orderItem.setOrder1(order);

	            orderRepository.save(order);
	            
	            kafkaTemplate.send(
	                "order-placed-topic",
	                new OrderPlacedEvent(order.getId(), email, orderItem.getTotal()));
	            log.info("email:"+email);

	            return "Order Placed";
	        } else {
	            return "Product validation failed"; // Add this
	        }
	    }
	    
	    return "Authentication failed"; // More descriptive
	}
	
	
	public List<OrderDto> getOrders(Long userId) {
	    return orderRepository.findByUserId(userId)
	            .stream()
	            .map(order -> {
	                OrderDto dto = new OrderDto();
	                dto.setOrderId(order.getId());
	                dto.setUserId(order.getUserId());
	                dto.setItems(
	                	    order.getItems().stream()
	                        .map(item -> {
	                            OrderItemDto itemDto = new OrderItemDto();
	                            itemDto.setProductId(item.getProductId());
	                            itemDto.setQuantity(item.getQuantity());
	                            itemDto.setTotal(item.getTotal());
	                            itemDto.setStatus(item.getStatus());
	                            return itemDto;
	                        })
	                        .toList()
	                );
	                return dto;
	            })
	            .toList();
	}



	public String setToDelivered(Long order1_Id, Long productId, BigDecimal price) {

		OrderItems orderItem = orderItemsRepository.findByProductIdAndOrder1_Id(productId, order1_Id);

		if (price.compareTo(orderItem.getTotal()) == 0) {
			orderItem.setStatus(OrderStatus.DELIVERED);
			orderItemsRepository.save(orderItem);
			return "Item Delivered";
		}
		throw new RuntimeException("Price mismatch!");
	}
	private String fallbackForOrder(Exception ex) {
		return "Waiting for Product and Cart details!";
	}
	
	
}
