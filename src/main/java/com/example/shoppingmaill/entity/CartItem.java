package com.example.shoppingmaill.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="cart_item")
@Getter
@Setter
@ToString
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cart_item_id")
    private Long Id;

    @ManyToOne
    @JoinColumn(name="cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="item_id")
    private Item item;

    private int count;

    // 장바구니에 담을 객체를 생성하는 메소드 추가
    public static CartItem createCartItem(Cart cart, Item item, int count){
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 담겨 있는 상품을 또 장바구니로 담았을 경우 수량을 증가시키는 메소드 추가
    public void addCount(int count) {
        this.count += count;
    }

    // 수량 변경을 위한 메소드 추가
    public void updateCount(int count){
        this.count = count;
    }
}
