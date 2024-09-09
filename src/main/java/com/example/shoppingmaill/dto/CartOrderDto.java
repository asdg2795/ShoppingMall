package com.example.shoppingmaill.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {
    // 장바구니 페이지에서 주문할 상품의 데이터를 위한 DTO
    private long cartItemId;
    private List<CartOrderDto> cartOrderDtoList;
}
