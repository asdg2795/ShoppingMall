package com.example.shoppingmaill.entity;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.dto.ItemFormDto;
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


    // 엔티티 클래스에 로직을 추가한다면 좀 더 객체지향적이고 코드 재활용이 가능
    // 변경 포인트를 한군데에서 관리할 수 있음
    public void updateItem(ItemFormDto itemFormDto){
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }
}

