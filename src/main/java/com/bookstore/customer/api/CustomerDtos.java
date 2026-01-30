package com.bookstore.customer.api;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class CustomerDtos {
    public record CreateCustomerRequest(
            @NotBlank String name
    ) {}

    public record CustomerResponse(
            Long id,
            String name,
            BigDecimal totalPurchase
    ) {}
}
