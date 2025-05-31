package com.ecommerce.shop.exception;

import com.ecommerce.shop.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientStockException(InsufficientStockException ex){
        return new ResponseEntity<>(new ApiErrorResponse(LocalDateTime.now(), "Insufficient Stock", ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentFailedException.class)
    public ResponseEntity<ApiErrorResponse> handleInsufficientStockException(PaymentFailedException ex){
        return new ResponseEntity<>(new ApiErrorResponse(LocalDateTime.now(), "Payment Failed", ex.getMessage()), HttpStatus.PAYMENT_REQUIRED);
    }

}
