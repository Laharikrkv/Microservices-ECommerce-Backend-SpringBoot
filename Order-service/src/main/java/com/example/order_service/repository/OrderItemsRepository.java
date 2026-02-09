package com.example.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order_service.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
					
	OrderItems findByProductIdAndOrder1_Id(Long productId, Long orderId);

				
				OrderItems findByOrder1_Id(Long Id);
}
	