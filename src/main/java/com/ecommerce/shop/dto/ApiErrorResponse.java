package com.ecommerce.shop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
