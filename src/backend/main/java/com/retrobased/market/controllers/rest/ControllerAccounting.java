package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.services.ServiceCliente;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/auth")
public class ControllerAccounting {
    // private final KeycloakUserServiceImpl keycloakUserService
    private final ServiceCliente ServiceCliente;

    public ControllerAccounting(ServiceCliente ServiceCliente) {
        this.ServiceCliente = ServiceCliente;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid Cliente user) {
        System.out.println("Saving user: " + user);
        try {
            Cliente added = ServiceCliente.registerUser(user);
            return new ResponseEntity<>(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        } catch (ValueCannotBeEmpty e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PAYLOAD_EMPTY"), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO RICHIEDERE CREDENZIALI E CHIEDERE IL TOKEN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid Cliente user) {
        return null;
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@RequestParam("username") String username) {
        return null;
    }

}
