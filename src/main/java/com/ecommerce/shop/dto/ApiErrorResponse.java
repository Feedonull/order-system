package com.ecommerce.shop.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ApiErrorResponse(LocalDateTime timestamp, String message, String details){
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

}
