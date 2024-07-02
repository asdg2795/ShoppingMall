package com.example.shoppingmaill.service;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.dto.OrderDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.Member;
import com.example.shoppingmaill.entity.Order;
import com.example.shoppingmaill.entity.OrderItem;
import com.example.shoppingmaill.repository.ItemRepository;
import com.example.shoppingmaill.repository.MemberRepository;
import com.example.shoppingmaill.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.yaml")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        return itemRepository.save(item);
    }

    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }

    @Test
    @DisplayName("주문 테스트")
    public void order(){
        Item item = saveItem();
        Member member = saveMember();

        // 상품 상세 페이지 화면에서 넘어오는 값을 설정
        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        // 주문 객체 DB에 저장
        Long orderId = orderService.order(orderDto, member.getEmail());

        // 저장된 주문 객체 조회
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        // 1. DB에 저장된 주문 객체에서 주문 상품 추출 (1개)
        List<OrderItem> orderItems = order.getOrderItems();

        // 2. 위에서 만든 주문 상품 총 가격 (1개)
        int totalPrice = orderDto.getCount() * item.getPrice();

        // 1의 가격과 2가 같은지 테스트
        assertEquals(totalPrice, order.getTotalPrice());
    }

}