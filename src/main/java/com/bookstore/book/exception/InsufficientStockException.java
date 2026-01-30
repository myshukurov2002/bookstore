package com.bookstore.book.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(Long bookId) {
        super("Insufficient stock for book: " + bookId);
    }
}
