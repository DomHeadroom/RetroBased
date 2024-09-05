package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.entities.Prodotto;
import com.retrobased.market.services.ServiceOggettoCarrello;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;


@RestController
@RequestMapping("/cart")
public class ControllerCart {
    private final ServiceOggettoCarrello ServiceOgg;

    public ControllerCart(ServiceOggettoCarrello ServiceOgg) {
        this.ServiceOgg = ServiceOgg;
    }

    @GetMapping("/add")
    public ResponseEntity<?> add(@RequestBody @Valid Prodotto pro, @RequestBody @Valid Long idCLiente) {
        try {
            OggettoCarrello added = ServiceOgg.addProdotto(idCLiente, pro, BigDecimal.ONE);
            return new ResponseEntity<>(added, HttpStatus.OK);
        } catch (ValueCannotBeEmpty e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PAYLOAD_EMPTY"), HttpStatus.BAD_REQUEST);
        } catch (ProductNotExist e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PRODUCT_DONT_EXIST"), HttpStatus.BAD_REQUEST);
        } catch (ArgumentValueNotValid | ProductQuantityNotAvailable e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_VALUE_NOT_PERMITTED"), HttpStatus.BAD_REQUEST);
        } catch (ProductAlreadyPresent e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PRODUCT_ALREADY_IN_CART"), HttpStatus.BAD_REQUEST);
        } catch (ClientNotExist e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_CLIENT_DONT_EXIST"), HttpStatus.BAD_REQUEST);
        } catch (ClientTokenMismatch e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_TOKEN_MISSMATCH"), HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/cart")
    public ResponseEntity get(@RequestBody @Valid Cliente user) {
        try {
            OggettoCarrello added = ServiceOggettoCarrello.;
            return new ResponseEntity(added, HttpStatus.OK);
        } catch (MailUserAlreadyExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_MAIL_USER_ALREADY_EXISTS"), HttpStatus.BAD_REQUEST);
        } catch (ValueCannotBeEmpty e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_PAYLOAD_EMPTY"), HttpStatus.BAD_REQUEST);
        }
    }*/

}
