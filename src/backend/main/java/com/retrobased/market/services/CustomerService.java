package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.support.exceptions.UserMailAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerCustomer(Customer customer) throws UserMailAlreadyExistsException {

        if (customerRepository.existsByEmail(customer.getEmail()))
            throw new UserMailAlreadyExistsException();

        Customer customerAdded = customerRepository.save(customer);
        System.out.println("Saved user: " + customerAdded);
    }


}
