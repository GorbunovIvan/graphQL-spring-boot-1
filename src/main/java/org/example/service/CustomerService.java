package org.example.service;

import org.example.model.Customer;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final List<Customer> customers = List.of(
            new Customer(1, "Bob"),
            new Customer(2, "Steve"),
            new Customer(3, "Maria")
    );

    public List<Customer> getAll() {
        return new ArrayList<>(customers);
    }

    public Customer getById(@NonNull Integer id) {
        return customers.stream()
                .filter(c -> id.equals(c.getId()))
                .findAny()
                .orElse(null);
    }
}
