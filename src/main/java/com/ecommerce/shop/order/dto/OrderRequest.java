package com.ecommerce.shop.order.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "Customer ID is required")
    private Long customerId;
    @NotNull(message = "items are required")
    private List<OrderRequestItem> items;

}
