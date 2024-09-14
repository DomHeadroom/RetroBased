package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.services.CartItemService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {
    private final ProductService productService;

    private final CartItemService cartItemService;

    public CartController(ProductService productService, CartItemService cartItemService) {
        this.productService = productService;
        this.cartItemService = cartItemService;
    }

    // aggiunta prodotto al carrello
    @GetMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestParam(value = "product") @NotNull UUID productId, @RequestParam(value = "quantity") @NotNull @Min(1) Long quantity) {
        try {
            UUID customerId = null; // TODO cambiare con metodo per estrarre id da token
            CartItem added = cartItemService.addProductToCart(customerId, productId, quantity);
            return new ResponseEntity<>(added, HttpStatus.OK);
        } catch (ArgumentValueNotValidException | ProductAlreadyPresentException |
                 ProductQuantityNotAvailableException | CustomerDontExistsException | ProductDontExistsException e) {
            return new ResponseEntity<>(new ResponseMessage("ERROR_VALUE_NOT_PERMITTED"), HttpStatus.BAD_REQUEST);
        }
    }

    // TODO cacciare customerId per prenderlo da token
    @GetMapping("/get")
    public ResponseEntity<?> getCartProducts(
            @RequestBody @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<Product> result = cartItemService.getCart(customerId, pageNumber);

        if (result.isEmpty())
            return new ResponseEntity<>(new ResponseMessage("NO_RESULTS_FOUND"), HttpStatus.OK);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }

}
