package com.bookstore.book.app;

import com.bookstore.book.domain.Book;
import com.bookstore.book.domain.BookRepository;
import com.bookstore.book.exception.BookNotFoundException;
import com.bookstore.book.exception.InsufficientStockException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BookService {
    private final BookRepository repository;

    @Transactional
    public Book create(String title, BigDecimal price, int stock) {
        Book book = Book.builder().title(title).price(price).stock(stock).build();
        return repository.save(book);
    }

    @Transactional(readOnly = true)
    public Book get(Long id) {
        return repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Book> list() {
        return repository.findAll();
    }

    @Transactional
    public Book adjustStock(Long id, int delta) {
        Book book = repository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        int newStock = book.getStock() + delta;
        if (newStock < 0) {
            throw new InsufficientStockException(id);
        }
        book.setStock(newStock);
        return repository.save(book);
    }
}
