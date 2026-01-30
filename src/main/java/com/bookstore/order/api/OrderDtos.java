package com.bookstore.order.api;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

public class OrderDtos {
    public record OrderItemRequest(
            @NotNull Long bookId,
            @Min(1) Integer quantity
    ) {}

    public record CreateOrderRequest(
            @NotNull Long customerId,
            List<OrderItemRequest> items
    ) {}

    public record OrderItemResponse(
            Long bookId,
            String title,
            Integer quantity,
            BigDecimal unitPrice,
            BigDecimal lineTotal
    ) {}

    public record OrderResponse(
            Long id,
            Long customerId,
            List<OrderItemResponse> items,
            BigDecimal totalAmount
    ) {}
}
