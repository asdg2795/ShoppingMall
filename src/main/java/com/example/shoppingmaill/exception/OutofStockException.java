package com.example.shoppingmaill.exception;

// 상품 재고 부족 Exception
public class OutofStockException extends RuntimeException{
    public OutofStockException(String message){
        super(message);
    }
}
