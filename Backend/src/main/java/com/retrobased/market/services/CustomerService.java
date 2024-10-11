package com.retrobased.market.services;

import com.retrobased.market.dtos.CustomerDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.mappers.CustomerMapper;
import com.retrobased.market.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

// TODO da deprecare
@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(readOnly = true)
    public Boolean exists(UUID id) {
        return customerRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> get(UUID id) {
        return customerRepository.findById(id);
    }

    public Customer findByKeycloakId(String userId) {
        return null;
        // TODO da completare
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }
}
