package com.example.shoppingmaill.dto;

import com.example.shoppingmaill.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

// 상품 조회 조건
// 1. 상품 등록일
// 2. 상품 판매 상태
// 3. 상품명 또는 등록자 아이디
@Getter @Setter
public class ItemSearchDto {
    private String searchDataType;
    private ItemSellStatus searchSellStatus;
    private String searchBy;
    private String searchQuery = "";
}
