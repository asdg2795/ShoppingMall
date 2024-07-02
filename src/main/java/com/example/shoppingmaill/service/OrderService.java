package com.example.shoppingmaill.service;

import com.example.shoppingmaill.dto.OrderDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.Member;
import com.example.shoppingmaill.entity.Order;
import com.example.shoppingmaill.entity.OrderItem;
import com.example.shoppingmaill.repository.ItemRepository;
import com.example.shoppingmaill.repository.MemberRepository;
import com.example.shoppingmaill.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Long order(OrderDto orderDto, String email){
        Item item = itemRepository.findById(orderDto.getItemId()).orElseThrow();
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


}
