package com.ecommerce.shop.exception;

public class PaymentFailedException extends RuntimeException{

    public PaymentFailedException(String message){
        super(message);
    }

}
