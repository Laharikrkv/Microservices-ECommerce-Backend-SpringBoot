package com.example.order_service.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.order_service.entity.Order1;

public interface OrderRepository extends JpaRepository<Order1, Long> {

    List<Order1> findByUserId(Long userId);

    Optional<Order1> findById(Long id);
}

