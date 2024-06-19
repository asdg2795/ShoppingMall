package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>,
        QuerydslPredicateExecutor {

    List<Item> findByItemNm(String itemNm);

    // 상품 상세 설명으로 상품을 조회하고 결과는 가격순으로 정렬
    // @Param을 통해 매개변수로 넘어온 값을 JPQL에 들어갈 변수로 지정
    @Query("select i from Item i where i.itemDetail like %:itemDetail% order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    // @Query nativeQuery를 이용한 조회
    // @Query 속성의 value 값으로 SQL문을 지정하고, nativeQuery 속성 값을 true 지정
    @Query(value = "select * from Item i where i.item_detail" +
    " like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDeatail);

}
