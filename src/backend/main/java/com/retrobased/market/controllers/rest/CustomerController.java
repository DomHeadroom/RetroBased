package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.CustomerService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.CustomerDontExistsException;
import com.retrobased.market.support.exceptions.UserMailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.UUID;


@RestController
@RequestMapping("/api/user")
@Validated
public class CustomerController {

    private final CustomerAddressService customerAddressService;

    private final CustomerService customerService;

    public CustomerController(CustomerAddressService customerAddressService, CustomerService customerService) {
        this.customerAddressService = customerAddressService;
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid Customer customer) {
        try {
            customerService.registerUser(customer);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserMailAlreadyExistsException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_EMAIL_ALREADY_REGISTERED"));
        }
    }

    // TODO cacciare customerId per prenderlo da token
    @PostMapping("/address")
    public ResponseEntity<?> addCustomerAddress(@RequestBody @Valid CustomerAddress address, @RequestParam(value = "user") @NonNull UUID customerId) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(customerAddressService.addAddress(customerId, address));
        } catch (CustomerDontExistsException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_USER_DONT_EXIST"));
        }
    }

}
