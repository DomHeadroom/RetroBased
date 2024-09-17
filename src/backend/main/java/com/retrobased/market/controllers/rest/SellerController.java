package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.services.CartItemService;
import com.retrobased.market.services.SellerService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/seller")
@Validated
public class SellerController {
    private final SellerService sellerService;

    private final CartItemService cartItemService;

    public SellerController(SellerService sellerService, CartItemService cartItemService) {
        this.sellerService = sellerService;
        this.cartItemService = cartItemService;
    }

    // aggiunta prodotto al carrello
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid @NotNull Seller seller) {
        sellerService.registerSeller(seller);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // TODO cacciare customerId per prenderlo da token
    @GetMapping("/get")
    public ResponseEntity<?> getSoldProducts(
            @RequestBody @NotNull UUID sellerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<Product> result = cartItemService.getCart(sellerId, pageNumber);

        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);

    }

}
