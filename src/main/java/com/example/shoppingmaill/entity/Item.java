package com.example.shoppingmaill.entity;

import com.example.shoppingmaill.constant.ItemSellStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Table(name = "item")
@Setter
@ToString
public class Item extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "item_id")
    private Long id;                            // 상품 코드

    @Column(nullable = false, length = 50)
    private String itemNm;                      // 상품명

    @Column(nullable = false)
    private int price;                          // 상품 가격

    @Column(nullable = false)
    private int stockNumber;                    // 재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail;                  // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;      // 상품 판매 상태
}

