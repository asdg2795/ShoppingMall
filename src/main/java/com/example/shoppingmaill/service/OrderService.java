package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.OrderDto;
import com.example.shoppingmaill.dto.OrderHistDto;
import com.example.shoppingmaill.dto.OrderItemDto;
import com.example.shoppingmaill.entity.*;
import com.example.shoppingmaill.repository.ItemImgRepository;
import com.example.shoppingmaill.repository.ItemRepository;
import com.example.shoppingmaill.repository.MemberRepository;
import com.example.shoppingmaill.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

// 주문 상품 객체 생성 -> 주문 객체 생성
@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final ItemRepository itemRepository; // 상품을 불러와서 재고를 변경해야함
    private final MemberRepository memberRepository; // 멤버를 불러와서 연결해야함
    private final OrderRepository orderRepository; // 주문 객체를 저장해야함
    private final ItemImgRepository itemImgRepository;

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
        Member member = memberRepository.findByEmail(email);

        List<OrderItem> orderItemList = new ArrayList<>();

        // OrderItem.createOrderItem -> static 메소드
        OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
        orderItemList.add(orderItem);

        // Order.createOrder -> static 메소드
        Order order = Order.createOrder(member, orderItemList);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Page<OrderHistDto> getOrderList(String email, Pageable pageable){
        // 주문 목록을 조회하는 로직 추가
        List<Order> orders = orderRepository.findOrders(email, pageable);
        Long totalCount = orderRepository.countOrder(email);

        List<OrderHistDto> orderHistDtos = new ArrayList<>();

        for(Order order : orders){
            OrderHistDto orderHistDto = new OrderHistDto(order);
            // OrderHistDto 생성자를 통해 Order 객체를 기반으로 생성
            List<OrderItem> orderItems = order.getOrderItems();
            // Order 에 속하는 OrderItem 목록 조히
            for(OrderItem orderItem: orderItems){
                ItemImg itemImg = itemImgRepository
                        .findByItemIdAndRepImgYn(orderItem.getItem().getId(), "Y");
                // OrderItem 에 속하는 ItemImg 객체 중 대표 이미지(Y)인 객체 조회
                OrderItemDto orderItemDto = new OrderItemDto(orderItem, itemImg.getImgUrl());
                // OrderItemDto 생성자를 통해 OrderItem 과 ItemImg 의 imgUrl 를 기반으로 OrderItemDto 생성
                orderHistDto.addOrderItemDto(orderItemDto);
                // OrderHistDto 에 OrderItemDto 추가
            }
            orderHistDtos.add(orderHistDto);
            // 생성된 orderHistDto 를 리스트에 추가
        }
        return new PageImpl<OrderHistDto>(orderHistDtos, pageable, totalCount);
        // PageImpl 을 사용하여 페이지네이션된 OrderHistDto 리스트를 반환
    }

    // 상품을 주문한 유저와 주문 취소를 요청한 유저가 동일한지 검증
    @Transactional(readOnly = true)
    public boolean validateOrder(Long orderId, String email){
        // 주문 취소 요청 유저
        Member curMember = memberRepository.findByEmail(email);

        // 상품을 주문한 유저
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        Member savedMember = order.getMember();

        if(!StringUtils.equals(curMember.getEmail(), savedMember.getEmail())){// 동일하지 않으면
            return false;
        }
        return true;
    }

    // 주문 취소 메소드(변경 감지)
    public void cancelOrder(Long orderId){
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        order.cancelOrder();
    }

    public Long orders(List<OrderDto> orderDtoList, String email) {
        // 장바구니 페이지에서 전달 받은 구매 상품으로 주문을 생성하는 로직 추가

        // 로그인한 유저 조회
        Member member = memberRepository.findByEmail(email);

        // orderDto 객체를 이용하여 item 객체에 count 값을 얻어낸 뒤,
        // 이를 이용하여 OrderItem 객체(들) 생성
        List<OrderItem> orderItemList = new ArrayList<>();
        for(OrderDto orderDto : orderDtoList){
            // 아이템 조회
            Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow(EntityNotFoundException::new);
            // 주문 항목 생성
            // `createOrderItem` 은 정적 팩토리 메서드로 `OrderItem` 객체를 생성하는 역할
            OrderItem orderItem = OrderItem.createOrderItem(item, orderDto.getCount());
            // OrderDto에 존재하는 ItemId 와 Count 값을 얻은 뒤에 이를 이용하여 OrderItem 객체 생성 ->

            //  주문 항목 리스트 추가
            orderItemList.add(orderItem);
        }

        // Order Entity 클래스에 존재하는 createOrder 메소드로 Order 생성 및 저장
        Order order = Order.createOrder(member, orderItemList);
        // Order.createOrder() 메소드 호출하여 Order 객체 생성후 save
        orderRepository.save(order);
        return order.getId();

    }


}
