package com.retrobased.market.controllers.rest;

import com.retrobased.market.controllers.dto.ProductRequestCart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.services.CartItemService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.*;
import jakarta.validation.Valid;
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

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/cart")
@Validated
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    //TODO FIXXARE STA STRONZATA PERCHè NON WORKA ALLA RICEVUTA DELL'OGGETTO PER QUALCHE MOTIVO

    // aggiunta prodotto al carrello
    @GetMapping("/add")
    public ResponseEntity<?> addProductToCart(@RequestBody @Valid @NotNull ProductRequestCart productRequestCart) {
        try {
            UUID customerId = UUID.fromString("f3106b66-3ed0-4d61-a7ae-fcc0651eb8cf"); // TODO cambiare con metodo per estrarre id da token

            List<CartItem> added = cartItemService.addProductToCart(customerId, productRequestCart.getProducts());
            return ResponseEntity.ok(added);
        } catch (ArgumentValueNotValidException | ProductAlreadyPresentException |
                 ProductQuantityNotAvailableException | CustomerDontExistsException | ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_ARGUMENT_VALUE_NOT_VALID"));
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
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);

    }

}
