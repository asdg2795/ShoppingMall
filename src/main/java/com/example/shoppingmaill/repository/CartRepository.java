package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
