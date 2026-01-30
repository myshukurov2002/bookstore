package com.bookstore.book.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class BookDtos {
    public record CreateBookRequest(
            @NotBlank String title,
            @NotNull BigDecimal price,
            @Min(0) int stock
    ) {}

    public record AdjustStockRequest(
            @NotNull Long bookId,
            int delta
    ) {}

    public record BookResponse(
            Long id,
            String title,
            BigDecimal price,
            Integer stock
    ) {}
}
