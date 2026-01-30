package com.bookstore.order.api;

import com.bookstore.order.app.OrderService;
import com.bookstore.order.domain.Order;
import com.bookstore.order.domain.OrderItem;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.AllArgsConstructor;

import static com.bookstore.order.api.OrderDtos.*;

@RestController
@RequestMapping("/api/orders")
@Validated
@AllArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    public OrderResponse placeOrder(@RequestBody CreateOrderRequest request) {
        List<OrderService.Item> items = request.items().stream()
                .map(i -> new OrderService.Item(i.bookId(), i.quantity()))
                .toList();
        Order o = service.placeOrder(request.customerId(), items);
        return toResponse(o);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public OrderResponse get(@PathVariable Long id) {
        Order o = service.get(id);
        return toResponse(o);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ORDER_READ')")
    public List<OrderResponse> list() {
        return service.list().stream().map(this::toResponse).toList();
    }

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getItems().stream()
                .map(oi -> new OrderItemResponse(
                        oi.getBook().getId(),
                        oi.getBook().getTitle(),
                        oi.getQuantity(),
                        oi.getUnitPrice(),
                        oi.getLineTotal()))
                .toList();
        return new OrderResponse(o.getId(), o.getCustomer().getId(), items, o.getTotalAmount());
    }
}
