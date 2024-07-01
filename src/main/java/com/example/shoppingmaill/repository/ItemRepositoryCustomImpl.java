package com.example.shoppingmaill.repository;

import com.example.shoppingmaill.constant.ItemSellStatus;
import com.example.shoppingmaill.dto.ItemSearchDto;
import com.example.shoppingmaill.entity.Item;
import com.example.shoppingmaill.entity.QItem;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

// 사용자 정의 인터페이스 구현
public class ItemRepositoryCustomImpl implements ItemRepositoryCustom{
    private JPAQueryFactory queryFactory;

    // 생성자 DI를 통해서 EntityManager 주입
    public ItemRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    // BooleanExpression 울 통해 where 절에 적용될 조회 조건을 생성
    // BooleanExpression 값이 null 이면 해당 조회 조건을 사용하지 않겠다는 의미 (=all)
    // 상품 등록일 조건
    // 상품 등록일에 대한 조회 조건 BooleanExpression
    private BooleanExpression regDtsAfter(String searchDataType){
        LocalDateTime dateTime = LocalDateTime.now();

        if (StringUtils.equals("all", searchDataType) || searchDataType == null) {
            return null;
        } else if (StringUtils.equals("1d",searchDataType)){
            dateTime = dateTime.minusDays(1);
        } else if (StringUtils.equals("1w",searchDataType)){
            dateTime = dateTime.minusWeeks(1);
        } else if (StringUtils.equals("1m",searchDataType)){
            dateTime = dateTime.minusMonths(1);
        } else if (StringUtils.equals("6m",searchDataType)){
            dateTime = dateTime.minusMonths(6);
        }
        return QItem.item.regTime.after(dateTime);
    }

    // 상품 판매 상태 조건
    // 상품 상태에 대한 조회 조건 BooleanExpression
    private BooleanExpression searchSellStatusEq(ItemSellStatus searchSellStatus){
        return searchSellStatus == null ? null : QItem.item.itemSellStatus.eq(searchSellStatus);
    }

    // 상품명 또는 상품 등록자 아이디 조건
    // 상품명 또는 등록자 아이디에 대한 조회 조건 BooleanExpression
    private BooleanExpression searchByLike(String searchBy, String searchQuery) {
        if(StringUtils.equals("itemNm", searchBy)){
            return QItem.item.itemNm.like("%"+searchQuery+"%");
        } else if(StringUtils.equals("createdBy", searchBy)) {
            return QItem.item.createdBy.like("%"+searchQuery+"%");
        }
        return null;
    }

    // QueryFactory를 이용하여 Querydsl 쿼리문 생성
    // fetchResult() 메소드를 이용하여 조회 대상 리스트 및 전체 개수를 포함하는 QueryResults 객체 반환
    // Page 클래스의 구현체인 PageImpl 객체로 반환
    @Override
    public Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable) {
        QueryResults<Item> results = queryFactory
                .selectFrom(QItem.item)
                .where(regDtsAfter(itemSearchDto.getSearchDataType()),
                        searchSellStatusEq(itemSearchDto.getSearchSellStatus()),
                        searchByLike(itemSearchDto.getSearchBy(), itemSearchDto.getSearchQuery()))
                .orderBy(QItem.item.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults(); // 2번의 select문 실행

        // 조회 대상 리스트 결과
        List<Item> content = results.getResults();

        // 조회 대상 리스트의 개수(count)
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }
}
