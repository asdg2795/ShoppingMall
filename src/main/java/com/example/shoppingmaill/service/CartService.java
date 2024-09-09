package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.CartDetailDto;
import com.example.shoppingmaill.dto.CartItemDto;
import com.example.shoppingmaill.dto.CartOrderDto;
import com.example.shoppingmaill.dto.OrderDto;
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
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderService orderService;

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

    // Controller 에서 email 값을 전달 받음
    @Transactional(readOnly = true)
    public List<CartDetailDto> getCartList(String email){
        List<CartDetailDto> cartDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);
        Cart cart = cartRepository.findByMemberId(member.getId());
        // 로그인 유저 정보 -> 유저의 cart 조회 ->

        if(cart == null){
            return cartDetailDtoList;
        }

        cartDetailDtoList = cartItemRepository.findcartDetailDtoList(cart.getId());
        // cart 를 참조하는 CartItem 조회 (cartDetailDto 로 변환)
        return cartDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateCartItem(Long cartItemId, String email){
        // 현재 로그인한 유저와 해당 장바구니 상품의 저장한 유저가 같은지 검증

        // 현재 로그인한 유저
        Member curMember = memberRepository.findByEmail(email);

        // 수량 변경 요청이 들어온 장바구니 상품의 유저
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = cartItem.getCart().getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){
            return false;
        }
        return true;
    }

    public void updateCartItemCount(Long cartItemId, int count){
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItem.updateCount(count);
    }

    public void deleteCartItem(Long cartItemId){
        // 장바구니 상품 번호를 파라미터로 받아서 삭제하는 로직 추가
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(EntityNotFoundException::new);
        cartItemRepository.delete(cartItem);
    }

    public Long orderCartItem(List<CartOrderDto> cartOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();

        // CartOrderDto 객체를 이용하여 cartItem 객체를 조회
        // cartItem 객체에서 itemId 와 count 값을 이용해 orderDto 객체 생성
        for (CartOrderDto cartOrderDto : cartOrderDtoList) {
            // cartOrderDto 에 존재하는 cartItemId 를 이용하여 CartItem 객체 조회 ->
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            OrderDto orderDto = new OrderDto();
            // cartItem 에 존재하는 ItemId 와 count 값을 얻은 뒤에 이를 이용하여 OrderDto 객체 생성 ->
            orderDto.setItemId(cartItem.getItem().getId());
            orderDto.setCount(cartItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        // 주문한 장바구니 상품을 제거하는 로직
        for(CartOrderDto cartOrderDto : cartOrderDtoList) {
            // OrderDtoList 를 orderService.orders() 메소드 파라미터로 넘겨 호출 ->
            CartItem cartItem = cartItemRepository.findById(cartOrderDto.getCartItemId()).orElseThrow(EntityNotFoundException::new);
            cartItemRepository.delete(cartItem);
        }
        return orderId;
    }
}
