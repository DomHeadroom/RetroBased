package com.retrobased.market.mappers;

import com.retrobased.market.dtos.CustomerDTO;
import com.retrobased.market.entities.Customer;

// TODO da deprecare
public class CustomerMapper {
    public static Customer toEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.firstName());
        customer.setLastName(customerDTO.lastName());
        customer.setEmail(customerDTO.email());
        return customer;
    }
}
