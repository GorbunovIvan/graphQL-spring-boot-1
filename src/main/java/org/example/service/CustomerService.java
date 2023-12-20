package org.example.service;

import org.example.model.Customer;
import org.example.model.Profile;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private final List<Customer> customers = List.of(
            new Customer(1, "Bob", new Profile(3, 1)),
            new Customer(2, "Steve", new Profile(1, 2)),
            new Customer(3, "Maria", new Profile(2, 3))
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

    public Profile getProfileFor(Customer customer) {
        return customer.getProfile();
    }
}
