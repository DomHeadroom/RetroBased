package com.retrobased.market.controllers;

import com.retrobased.market.dto.ProductObjQuantityDTO;
import com.retrobased.market.dto.ProductRequestCartDTO;
import com.retrobased.market.entities.Product;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/carts")
@Validated
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    /**
     * Adds one or more products to the customer's shopping cart.
     * <p>
     * This method receives a list of products from the request body, attempts to add them to the customer's cart,
     * and returns the added products along with their quantities. If any issues arise, such as invalid argument values
     * or non-existent products, the method will return an appropriate error response.
     * </p>
     *
     * @param productRequestCartDTO the request body containing the list of products to add to the cart; must be valid and not null
     * @return a response entity containing either the added products or an error message if an exception occurs
     * <ul>
     *     <li><strong>200 OK</strong> – If the products were successfully added to the cart.</li>
     *     <li><strong>400 Bad Request</strong> – If the input is invalid or the product does not exist.</li>
     * </ul>
     */
    @PostMapping
    public ResponseEntity<?> addProductToCart(@RequestBody @Valid @NotNull ProductRequestCartDTO productRequestCartDTO) {
        try {
            UUID customerId = UUID.fromString("f3106b66-3ed0-4d61-a7ae-fcc0651eb8cf"); // TODO cambiare con metodo per estrarre id da token

            List<ProductObjQuantityDTO> added = cartItemService.addProductToCart(customerId, productRequestCartDTO.products());
            return ResponseEntity.ok(added);
        } catch (ArgumentValueNotValidException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_ARGUMENT_VALUE_NOT_VALID"));
        } catch (ProductNotFoundException e) {
            return ResponseEntity.badRequest().body(new ResponseMessage("ERROR_PRODUCT_NOT_EXISTS"));
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseMessage("ERROR_TOKEN_USER"));
        }
    }

    // TODO cacciare customerId per prenderlo da token

    /**
     * Retrieves the products in the customer's shopping cart.
     * <p>
     * This method fetches a paginated list of products that are present in the customer's cart.
     * It accepts the customer's UUID and the page number as parameters. If the cart is empty or no products
     * are found for the specified page, the method returns a <strong>204 No Content</strong> response.
     * Otherwise, it returns the list of products in the cart.
     * </p>
     *
     * @param customerId the UUID of the customer whose cart products are being retrieved; must not be null
     * @param pageNumber the page number of the products to retrieve, starting from 0; defaults to 0 if not specified, must be a non-negative integer
     * @return a response entity containing either the list of products in the cart or a status indicating no content
     * <ul>
     *     <li><strong>200 OK</strong> – If products are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If the cart is empty or there are no products for the given page.</li>
     * </ul>
     */
    @GetMapping
    public ResponseEntity<?> getCartProducts(
            @RequestBody @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<Product> result = null;
        try {
            result = cartItemService.getCart(customerId, pageNumber);
        } catch (CustomerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);

    }

}
