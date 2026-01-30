package com.bookstore.customer.app;

import com.bookstore.customer.domain.Customer;
import com.bookstore.customer.domain.CustomerRepository;
import com.bookstore.customer.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;

    @Transactional
    public Customer create(String name) {
        Customer customer = Customer.builder().name(name).build();
        return repository.save(customer);
    }

    @Transactional(readOnly = true)
    public Customer get(Long id) {
        return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public List<Customer> list() {
        return repository.findAll();
    }
}
