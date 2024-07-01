package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.dto.ItemSearchDto;
import com.example.shoppingmaill.dto.MainItemDto;
import com.example.shoppingmaill.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

// 사용자 정의 인터페이스 작성
// 상품 조회 조건을 담고 있는 itemSearchDto 객체와
// 페이징 정보를 담고 있는 pageable 객체를 파라미터로 받고,
// Page<Item> 객체를 반환
public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
