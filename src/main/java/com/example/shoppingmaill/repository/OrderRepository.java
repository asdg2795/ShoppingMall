package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
