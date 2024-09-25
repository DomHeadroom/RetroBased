package com.retrobased.market.mappers;

import com.retrobased.market.dto.CustomerDTO;
import com.retrobased.market.entities.Customer;

public class CustomerMapper {
    public static Customer toEntity(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.firstName());
        customer.setLastName(customerDTO.lastName());
        customer.setEmail(customerDTO.email());
        return customer;
    }
}
