package com.example.order_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private Long productId;
		
		private Integer quantity;
		
		private BigDecimal Total;
		
		@Enumerated(EnumType.STRING)
		private OrderStatus status;
		
		@ManyToOne
		@JoinColumn(name = "order1_id")
		private Order1 order1;
}
