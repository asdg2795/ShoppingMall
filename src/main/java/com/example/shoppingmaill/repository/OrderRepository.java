package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Order;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 해당 유저의 구매 이력을 페이징 정보에 맞게 조회 (주문 객체들)
    @Query("select o from  Order o " +
            // 엔티티에서 모든 필드를 조회
            "where o.member.email = :email " +
            // `Order` 엔티티의 `member` 필드의 `email` 속성이 주어진 `email` 값과 일치하는지 확인
            "order by o.orderDate desc")
            // 조회된 결과를 `orderDate` 필드를 기준으로 내림차순으로 정렬
    List<Order> findOrders(@Param("email") String email, Pageable pageable);

    // 해당 유저의 주문 개수
    @Query("select count(o) from Order o " +
            "where o.member.email = :email")
    Long countOrder(@Param("email") String email);
}
