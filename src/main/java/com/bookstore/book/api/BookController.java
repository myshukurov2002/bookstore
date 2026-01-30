package com.bookstore.book.api;

import com.bookstore.book.app.BookService;
import com.bookstore.book.domain.Book;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.AllArgsConstructor;

import static com.bookstore.book.api.BookDtos.*;

@RestController
@RequestMapping("/api/books")
@Validated
@AllArgsConstructor
public class BookController {
    private final BookService service;

    @PostMapping
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public BookResponse create(@RequestBody CreateBookRequest request) {
        Book b = service.create(request.title(), request.price(), request.stock());
        return new BookResponse(b.getId(), b.getTitle(), b.getPrice(), b.getStock());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BOOK_READ')")
    public BookResponse get(@PathVariable Long id) {
        Book b = service.get(id);
        return new BookResponse(b.getId(), b.getTitle(), b.getPrice(), b.getStock());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BOOK_READ')")
    public List<BookResponse> list() {
        return service.list().stream()
                .map(b -> new BookResponse(b.getId(), b.getTitle(), b.getPrice(), b.getStock()))
                .toList();
    }

    @PatchMapping("/stock")
    @PreAuthorize("hasAuthority('BOOK_WRITE')")
    public BookResponse adjustStock(@RequestBody AdjustStockRequest request) {
        Book b = service.adjustStock(request.bookId(), request.delta());
        return new BookResponse(b.getId(), b.getTitle(), b.getPrice(), b.getStock());
    }
}
