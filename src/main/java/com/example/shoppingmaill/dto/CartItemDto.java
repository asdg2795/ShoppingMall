package com.example.shoppingmaill.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDto {
    @NotNull(message = "상품 아이디는 필수 입력 값입니다.")
    private Long itemId; // 장바구니에 담을 상품 Id

    @Min(value = 1, message = "최소 1개 이상 담아주세요")
    private int count; // 수량을 전달 받을 객체
}
