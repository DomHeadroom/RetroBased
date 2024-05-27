package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.services.ServiceCliente;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class ControllerAccounting {
    @Autowired
    private ServiceCliente ServiceCliente;

    @GetMapping("/create")
    public ResponseEntity register(@RequestBody @Valid Cliente user) {
        try {
            Cliente added = ServiceCliente.registerUser(user);
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        } catch (ValueCannotBeEmpty e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PAYLOAD_EMPTY"), HttpStatus.BAD_REQUEST);
        }
    }



}
