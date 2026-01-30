package com.bookstore.customer.api;

import com.bookstore.customer.app.CustomerService;
import com.bookstore.customer.domain.Customer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import lombok.AllArgsConstructor;

import static com.bookstore.customer.api.CustomerDtos.*;

@RestController
@RequestMapping("/api/customers")
@Validated
@AllArgsConstructor
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_WRITE')")
    public CustomerResponse create(@RequestBody CreateCustomerRequest request) {
        Customer c = service.create(request.name());
        return new CustomerResponse(c.getId(), c.getName(), c.getTotalPurchase());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public CustomerResponse get(@PathVariable Long id) {
        Customer c = service.get(id);
        return new CustomerResponse(c.getId(), c.getName(), c.getTotalPurchase());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_READ')")
    public List<CustomerResponse> list() {
        return service.list().stream()
                .map(c -> new CustomerResponse(c.getId(), c.getName(), c.getTotalPurchase()))
                .toList();
    }
}
