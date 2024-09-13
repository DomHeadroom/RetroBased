package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Product;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {
    private final ProductService productService;

    public CartController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody @Valid Product product) {
        try {
            Product added = productService.addProduct(product);
            return new ResponseEntity<>(added, HttpStatus.OK);
        } catch (ValueCannotBeEmptyException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_VALUE_CANNOT_BE_EMPTY"), HttpStatus.BAD_REQUEST);
        } catch (ArgumentValueNotValidException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_VALUE_NOT_PERMITTED"), HttpStatus.BAD_REQUEST);
        }
    }

    /*@GetMapping("/get")
    public ResponseEntity getItems(@RequestBody @Valid Customer customer) {
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
