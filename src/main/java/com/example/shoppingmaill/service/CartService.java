package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.CartItemDto;
import com.example.shoppingmaill.entity.Cart;
import com.example.shoppingmaill.entity.CartItem;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.Member;
import com.example.shoppingmaill.repository.CartItemRepository;
import com.example.shoppingmaill.repository.CartRepository;
import com.example.shoppingmaill.repository.ItemRepository;
import com.example.shoppingmaill.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public Long addCart(CartItemDto cartItemDto, String email) {
        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());

        // 장바구니가 존재하지 않는다면 생성
        if(cart == null){
            cart = Cart.createCart(member);
            cartRepository.save(cart);
        }

        Item item = itemRepository.findById(cartItemDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), item.getId());

        // 해당 상품이 장바구니에 존재하지 않는다면 생성 후 추가
        if(cartItem == null) {
            cartItem = CartItem.createCartItem(cart, item, cartItemDto.getCount());
            cartItemRepository.save(cartItem);

        // 해당 상품이 장바구니에 이미 존재한다면 수량을 증가
        } else {
            cartItem.addCount(cartItemDto.getCount());
        }
        return cartItem.getId();
    }
}
