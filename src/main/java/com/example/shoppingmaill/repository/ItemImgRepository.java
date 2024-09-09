package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {
    // 이미지가 잘 저장되었는지 확인하기 위해 ItemImgRepository 커스텀 조회문 추가
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);

    // 구매 내역 페이지에서 주문 상품의 대표 이미지를 위한 조회
    ItemImg findByItemIdAndRepImgYn(Long itemId, String repImgYn);
}
