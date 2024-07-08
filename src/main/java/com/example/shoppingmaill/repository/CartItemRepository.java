package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.dto.CartDetailDto;
import com.example.shoppingmaill.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // select 부분에 new 키워드와 해당 DTO 의 패키지, 클래스명을 지정
    @Query("select new com.example.shoppingmaill.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.regTime desc")
    // 최적화를 위해 한 번에 반환하도록 List<CartDetailDto> 타입 지정
    List<CartDetailDto> findcartDetailDtoList(Long cartId);
}
