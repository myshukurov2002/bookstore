package com.bookstore.order.app;

import com.bookstore.book.domain.Book;
import com.bookstore.book.domain.BookRepository;
import com.bookstore.book.exception.InsufficientStockException;
import com.bookstore.customer.domain.Customer;
import com.bookstore.customer.domain.CustomerRepository;
import com.bookstore.customer.exception.CustomerNotFoundException;
import com.bookstore.order.domain.Order;
import com.bookstore.order.domain.OrderItem;
import com.bookstore.order.domain.OrderRepository;
import com.bookstore.order.exception.OrderCreationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final BookRepository bookRepository;

    @Transactional
    public Order placeOrder(Long customerId, List<Item> items) {
        if (items == null || items.isEmpty()) {
            throw new OrderCreationException("Order items required");
        }
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));
        Order order = Order.builder().customer(customer).build();

        for (Item i : items) {
            Book book = bookRepository.findById(i.bookId())
                    .orElseThrow(() -> new OrderCreationException("Book not found: " + i.bookId()));
            if (book.getStock() < i.quantity()) {
                throw new InsufficientStockException(book.getId());
            }
            book.setStock(book.getStock() - i.quantity());
            OrderItem oi = OrderItem.builder()
                    .book(book)
                    .quantity(i.quantity())
                    .unitPrice(book.getPrice())
                    .lineTotal(book.getPrice().multiply(BigDecimal.valueOf(i.quantity())))
                    .build();
            order.addItem(oi);
        }

        BigDecimal total = order.getTotalAmount();
        customer.addToTotalPurchase(total);
        return orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public Order get(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderCreationException("Order not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Order> list() {
        return orderRepository.findAll();
    }

    public record Item(Long bookId, Integer quantity) {}
}
