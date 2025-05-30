package com.ecommerce.shop.exception;

public class InsufficientStockException extends RuntimeException{

    InsufficientStockException(String message){
        super(message);
    }

}
