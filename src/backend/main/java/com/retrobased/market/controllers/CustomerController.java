package com.retrobased.market.controllers;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.CustomerService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.CustomerDontExistsException;
import com.retrobased.market.support.exceptions.UserMailAlreadyExistsException;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;


@RestController
@RequestMapping("api/users")
@Validated
public class CustomerController {

    private final CustomerAddressService customerAddressService;

    private final CustomerService customerService;

    public CustomerController(CustomerAddressService customerAddressService, CustomerService customerService) {
        this.customerAddressService = customerAddressService;
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid @NotNull Customer customer) {
        try {
            customerService.registerCustomer(customer);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (UserMailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_EMAIL_ALREADY_REGISTERED"));
        }
    }

    // TODO cacciare customerId per prenderlo da token
    @PostMapping("addresses")
    public ResponseEntity<?> addCustomerAddress(
            @RequestBody @Valid CustomerAddress address,
            @RequestParam(value = "user") @NotNull UUID customerId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerAddressService.addAddress(customerId, address));
        } catch (CustomerDontExistsException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_USER_NOT_FOUND"));
        }
    }

}
