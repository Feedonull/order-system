package com.ecommerce.shop.order.controller;

import com.ecommerce.shop.dto.ApiResponse;
import com.ecommerce.shop.dto.PagedResponse;
import com.ecommerce.shop.order.dto.OrderRequest;
import com.ecommerce.shop.order.entity.Order;
import com.ecommerce.shop.order.service.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management APIs")
public class OrderController {

    private final OrderService orderService;


    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<Order>>> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sort
    ){
        Page<Order> orders = orderService.findAllOrders(page, size, sort);
        PagedResponse<Order> response = new PagedResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.isLast()
        );
        return ResponseEntity.ok(new ApiResponse<PagedResponse<Order>>(true, "Orders retrieved successfully", response));
    }

    @GetMapping("/high-value")
    public ResponseEntity<ApiResponse<PagedResponse<Order>>> getHighValueOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Page<Order> orders = orderService.findHighValueOrders(page, size);
        PagedResponse<Order> response = new PagedResponse<>(
                orders.getContent(),
                orders.getNumber(),
                orders.getSize(),
                orders.getTotalElements(),
                orders.getTotalPages(),
                orders.isLast()
        );
        return ResponseEntity.ok(new ApiResponse<PagedResponse<Order>>(true, "High value orders retrieved successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(@RequestBody @Valid OrderRequest orderRequest) throws BadRequestException {

        Order order = orderService.createOrder(orderRequest);

        return ResponseEntity.ok(new ApiResponse<Order>(true, "Order placed successfully", order));
    }

}
