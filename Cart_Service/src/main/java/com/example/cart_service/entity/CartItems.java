package com.example.cart_service.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItems {

			@Id
			@GeneratedValue(strategy = GenerationType.IDENTITY)
			private Long id;	

			private Long productId;
			private Integer quantity;
			private Double price;
			
			@ManyToOne
			@JoinColumn(name = "cart_id")
			private Cart cart;
}
