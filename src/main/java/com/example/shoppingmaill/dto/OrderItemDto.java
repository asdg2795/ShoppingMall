package com.example.shoppingmaill.dto;

import com.example.shoppingmaill.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    // 주문 상태 정보를 담을 객체 생성
    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemNm = orderItem.getItem().getItemNm();
        this.count = orderItem.getCount();
        this.orderPrice = orderItem.getOrderPrice();
        this.imgUrl = imgUrl;
    }

    private String itemNm;
    private int count;
    private int orderPrice;
    private String imgUrl;

}
