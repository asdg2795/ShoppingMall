package com.example.shoppingmaill.entity;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.dto.ItemFormDto;
import com.example.shoppingmaill.exception.OutofStockException;
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

    // 상품 재고 변경
    public void removeStock(int stockNumber){
        int restStock = this.stockNumber - stockNumber;
        // (기존 재고 - 주문 수량 재고)
        if (restStock < 0) { // 만약 0보다 작다면 재고가 부족한 것으므로 Exception 발생
            throw new OutofStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    // 주문 취소 시 상품의 재고를 상품 주문 수량만큼 다시 더해주는 메소드 추가
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}

