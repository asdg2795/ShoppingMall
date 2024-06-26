package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.QItem;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

@SpringBootTest // 실제 Application을 구동할 때 처럼 모든 Bean을 IOC 컨테이너에 생성
@TestPropertySource(locations = "classpath:application-test.yaml") // 테스트 코드 실행 시 application.yaml와 겹치는 부분은 우선순위를 갖도록 함
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("심플 저장 테스트") // 테스트 코드 실행 시 테스트 명이 노출
    public void createItemTest(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        Item savedItem = itemRepository.save(item);
        System.out.println(savedItem.toString());
    }

    public void createItemList(){
        for(int i = 1; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품"+i);
            item.setPrice(10000+i);
            item.setItemDetail("테스트 상품 상세 설명"+i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            Item savedItem = itemRepository.save(item);
            System.out.println(savedItem.toString());
        }
    }


    // 쿼리 메소드(Query Method)
    // Spring Data JPA에서 제공하는 핵심 기능 중 하나로 Repository 인터페이스에 간단한 네이밍 룰에 따라 메소드를 작성하면,
    // 원하는 쿼리를 실행하도록 지원하는 메소드
    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest(){
        this.createItemList();
        List<Item> itemList = itemRepository.findByItemNm("테스트 상품1");
        for (Item item: itemList){
            System.out.println(item.toString());
        }
    }


    // Spring Data JPA @Query 어노테이션
    // SQL과 유사한 JPQL (Java persistence Query Langeage) 라는 객체지향 쿼리 언어를 통해 복잡한 쿼리 처리를 지원
    // JPQL - 테이블이 아닌 엔티티 객체를 대상으로 검색하는 객체지향 쿼리, SQL 추상화로 인해 특정 db sql에 의존하지 않음
    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest(){
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("nativeQuery를 이용한 상품 조회 테스트")
    public void findByItemDetailByNative(){
        this.createItemList();
        List<Item> itemList = this.itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }


    // @PersistenceContext어노테이션으로 bean 주입
    @PersistenceContext
    EntityManager em;

    // Querydsl
    // SQL 쿼리문을 문자열이 아닌 소스코드로 작성하도록 지원하는 빌더 API(프레임워크)
    // 장점
    // ① 고정된 SQL문이 아닌 조건에 맞게 동적으로 쿼리 생성 가능
    // ② 제약 조건 조립 및 가독성 향상
    // ③ 컴파일 시점에 오류를 발견할 수 있음
    // ④ 자동 완성 기능으로 인한 생산성 향상
    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest(){
        this.createItemList();
        // 쿼리를 동적으로 생성하기 위해 JPAQueryFactory 객체 생성 (매개변수로 엔티티 매니저 받음)
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        // Querydsl을 통해 쿼리르 생성하기 위해 Qdomain 객체 생성
        QItem qItem = QItem.item;

        // querydsl 쿼리문을 받을 JPAQuery 객체 생성
        // SQL 문자열이 아닌 자바 소스코드를 이용해 쿼리 생성
        // select -> 조회, where -> 조건문, orderBy -> 정렬
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%"+"테스트 상품 상세 설명"+"%"))
                .orderBy(qItem.price.desc());
        List<Item> itemList = query.fetch();

        for (Item item : itemList){
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 1; i <= 5; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            itemRepository.save(item);
        }
        for (int i = 6; i <= 10; i++) {
            Item item = new Item();
            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            itemRepository.save(item);
        }
    }
    // QuerydslPredicateExecutor
    // 쿼리문의 where 조건문을 메소드로 정의 해놓은 것
    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2(){
        this.createItemList2();

        // 쿼리문의 where 역할을 수행하는 Predicate를 담는 객체 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        QItem item = QItem.item;
        String itemDetail = "테스트 상품 상세 설명";
        int price = 10003;
        String itemSellStat = "SELL";

        // where 조건부 설정
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));
        booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));


        // 0번쨰 페이지, 5개의 데이터
        // findAll() 메소드의 매개변수로 predicate, pageable 전달
        // 반환된 결과는 Page<Item> 타입으로 받음
        Pageable pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : "+itemPagingResult.getTotalElements());

        // 반환된 페이지에서 content 부분만을 List<Item> 타입으로 받음
        List<Item> resultItemList = itemPagingResult.getContent();
        for(Item resultItem: resultItemList){
            System.out.println(resultItem.toString());
        }


    }
}