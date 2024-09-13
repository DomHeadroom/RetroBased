package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.services.CustomerService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.UserMailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/user")
@Validated
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Customer customer) {
        try {
            customerService.registerUser(customer);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserMailAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_EMAIL_ALREADY_REGISTERED"), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO modificare per permettere aggiunta indirizzo
    @PostMapping("/address")
    public ResponseEntity<?> addAddress(@RequestBody @Valid Customer address) {
        try {
            customerService.registerUser(address);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (UserMailAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_EMAIL_ALREADY_REGISTERED"), HttpStatus.BAD_REQUEST);
        }
    }

}
