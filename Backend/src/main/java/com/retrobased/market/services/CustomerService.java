package com.retrobased.market.services;

import com.retrobased.market.dtos.CustomerDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.mappers.CustomerMapper;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.utils.exceptions.UserMailAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerCustomer(CustomerDTO customer) throws UserMailAlreadyExistsException {

        if (existsByEmail(customer.email()))
            throw new UserMailAlreadyExistsException();

        customerRepository.save(CustomerMapper.toEntity(customer));
    }

    @Transactional(readOnly = true)
    public Boolean exists(UUID id) {
        return customerRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Customer> get(UUID id) {
        return customerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
}
