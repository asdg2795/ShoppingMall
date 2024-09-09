package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    // 현재 로그인한 유저의 장바구니(Cart)를 찾기 위해서 쿼리 메소드 추가
    Cart findByMemberId(Long memberId);
}
