package com.example.shoppingmaill.exception;

// 상품 재고 부족 Exception
public class OutofStockException extends RuntimeException{
    // 상품 주문 수량보다 현재 재고의 수가 적을 때 발생시킬 Excepiton 정의
    public OutofStockException(String message){
        super(message);
    }
}
