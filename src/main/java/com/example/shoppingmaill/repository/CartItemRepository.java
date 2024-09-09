package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.dto.CartDetailDto;
import com.example.shoppingmaill.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // 상품이 장바구니에 들어있는지 조회하는 쿼리 메소드 추가
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // select 부분에 new 키워드와 해당 DTO 의 패키지, 클래스명을 지정
    @Query("select new com.example.shoppingmaill.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            // `ci.id`는 `cartItem`의 ID
            // `i.itemNm`는 `Item`의 이름
            // `i.price`는 `Item`의 가격
            // `ci.count`는 `CartItem`의 수량
            // `im.imgUrl`는 `ItemImg`의 이미지 URL
            "from CartItem ci, ItemImg im " +
            // `CartItem`과 `ItemImg` 엔티티를 조회
            "join ci.item i " +
            // `CartItem` 엔티티에서 `Item` 엔티티를 조인
            "where ci.cart.id = :cartId " +
            // 특정 장바구니 ID에 해당하는 `CartItem`을 필터링
            "and im.item.id = ci.item.id " +
            // `ItemImg` 엔티티와 `CartItem` 엔티티를 아이템 ID로 매칭
            "and im.repImgYn = 'Y' " +
            // 대표 이미지(`repImgYn`이 'Y')만 조회
            "order by ci.regTime desc")
            // 등록 시간(`regTime`) 기준으로 내림차순 정렬
    // 최적화를 위해 한 번에 반환하도록 List<CartDetailDto> 타입 지정
    List<CartDetailDto> findcartDetailDtoList(Long cartId);
}
