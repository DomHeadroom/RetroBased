package com.retrobased.market.controllers;

import com.retrobased.market.dto.ProductObjQuantityDTO;
import com.retrobased.market.dto.ProductRequestCartDTO;
import com.retrobased.market.services.CartItemService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.support.exceptions.CustomerNotFoundException;
import com.retrobased.market.support.exceptions.ProductNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> addProductsToCart(@RequestBody @Valid @NotNull ProductRequestCartDTO productRequestCartDTO) {
        try {
            UUID customerId = UUID.fromString("f3106b66-3ed0-4d61-a7ae-fcc0651eb8cf"); // TODO cambiare con metodo per estrarre id da token

            List<ProductObjQuantityDTO> added = cartItemService.addProductsToCart(customerId, productRequestCartDTO.products());
            return ResponseEntity.ok(added);
        } catch (ArgumentValueNotValidException e) {
            return createErrorResponse("ERROR_QUANTITY_VALUE_NOT_VALID", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProductNotFoundException e) {
            return createErrorResponse("ERROR_PRODUCT_NOT_FOUND", HttpStatus.NOT_FOUND);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_TOKEN_USER", HttpStatus.FORBIDDEN);
        }
    }

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
     *     <li><strong>404 Not Found</strong> – If the customer extracted from the token is not present in the database.</li>
     * </ul>
     */
    @GetMapping
    public ResponseEntity<?> getCartProducts(
            @RequestParam @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        try {
            List<ProductObjQuantityDTO> result = cartItemService.getCartItems(customerId, pageNumber);

            if (result.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.ok(result);

        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping("{productId}")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable @NotNull UUID productId,
            @RequestParam @NotNull @Min(0) Long quantity) {
        // TODO cambiare con metodo per estrarre id da token
        UUID customerId = null;
        try {
            cartItemService.changeQuantity(customerId, productId, quantity);
            return ResponseEntity.ok().build();
        } catch (ArgumentValueNotValidException e) {
            return createErrorResponse("ERROR_QUANTITY_VALUE_NOT_VALID", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProductNotFoundException e) {
            return createErrorResponse("ERROR_PRODUCT_NOT_IN_CART", HttpStatus.NOT_FOUND);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<?> createErrorResponse(String message, HttpStatus status) {
        return ResponseEntity.status(status).body(new ResponseMessage(message));
    }

}
