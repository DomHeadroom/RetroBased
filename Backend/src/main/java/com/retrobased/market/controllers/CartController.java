package com.retrobased.market.controllers;

import com.retrobased.market.authentications.AuthenticationService;
import com.retrobased.market.dtos.CartItemDTO;
import com.retrobased.market.dtos.ProductRequestCartDTO;
import com.retrobased.market.services.CartItemService;
import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import com.retrobased.market.utils.exceptions.ProductNotFoundException;
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

import static com.retrobased.market.utils.ResponseUtils.createErrorResponse;


@RestController
@RequestMapping("carts")
@Validated
public class CartController {

    private final CartItemService cartItemService;
    private final AuthenticationService authenticationService;

    public CartController(
            CartItemService cartItemService,
            AuthenticationService authenticationService
    ) {
        this.cartItemService = cartItemService;
        this.authenticationService = authenticationService;
    }

    /**
     * Adds one or more products to the customer's shopping cart.
     * <p>
     * This method receives a list of products from the request body, attempts to add them to the customer's cart,
     * and returns the added products along with their quantities. The user's identity is extracted from the JWT token
     * present in the request's security context. If any issues arise, such as invalid argument values or non-existent
     * products, the method will return an appropriate error response.
     * </p>
     *
     * @param productRequestCartDTO the request body containing the list of products to add to the cart; must be valid and not null
     * @return a response entity containing either the added products or an error message if an exception occurs
     * <ul>
     *     <li><strong>200 OK</strong> – If the products were successfully added to the cart.</li>
     *     <li><strong>422 UNPROCESSABLE ENTITY</strong> – If the quantity value is invalid.</li>
     *     <li><strong>404 NOT FOUND</strong> – If any of the products do not exist.</li>
     *     <li><strong>403 FORBIDDEN</strong> – If the user could not be found based on the token.</li>
     * </ul>
     */
    @PostMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addProductsToCart(
            @RequestBody @Valid @NotNull ProductRequestCartDTO productRequestCartDTO
    ) {
        try {
            String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

            List<CartItemDTO> added = cartItemService.addProductsToCart(keycloakUserId, productRequestCartDTO.products());
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
     * It accepts the page number as a parameter. The user's identity is extracted from the JWT token
     * present in the request's security context. If the cart is empty or no products are found
     * for the specified page, the method returns a <strong>204 No Content</strong> response.
     * Otherwise, it returns the list of products in the cart.
     * </p>
     *
     * @param pageNumber the page number of the products to retrieve, starting from 0; defaults to 0 if not specified, must be a non-negative integer
     * @return a response entity containing either the list of products in the cart or a status indicating no content
     * <ul>
     *     <li><strong>200 OK</strong> – If products are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If the cart is empty or there are no products for the given page.</li>
     *     <li><strong>404 Not Found</strong> – If the customer extracted from the token is not present in the database.</li>
     * </ul>
     */
    @GetMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getCartProducts(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        try {
            String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

            List<CartItemDTO> result = cartItemService.getCartItems(keycloakUserId, pageNumber);

            if (result.isEmpty())
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

            return ResponseEntity.ok(result);

        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        }

    }

    /**
     * Updates the quantity of a specific product in the user's cart.
     *
     * <p>This method handles requests to update the quantity of a cart item based on the product's ID.
     * The user's unique Keycloak ID is extracted from the JWT token, and the cart item's quantity is updated
     * accordingly. If the specified quantity is invalid (e.g., less than 0), a custom exception is thrown.
     * Additionally, this method handles various exceptions related to invalid product IDs, missing user details,
     * or invalid argument values, providing appropriate HTTP responses.</p>
     *
     * @param productId The UUID of the product whose quantity in the cart is to be updated.
     *                  Must not be null.
     * @param quantity  The new quantity for the product in the cart.
     *                  Must be a non-negative value (>= 0).
     * @return A {@link ResponseEntity} with an appropriate HTTP status code:
     *         <ul>
     *           <li>200 OK if the quantity update is successful.</li>
     *           <li>422 UNPROCESSABLE ENTITY if the provided quantity value is invalid.</li>
     *           <li>404 NOT FOUND if the product is not found in the cart or the user is not found.</li>
     *         </ul>
     */
    @PostMapping("{productId}")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> updateCartItemQuantity(
            @PathVariable @NotNull UUID productId,
            @RequestParam @NotNull @Min(0) Long quantity
    ) {
        // TODO cambiare con metodo per estrarre id da token
        try {
            String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

            cartItemService.changeQuantity(keycloakUserId, productId, quantity);
            return ResponseEntity.ok().build();
        } catch (ArgumentValueNotValidException e) {
            return createErrorResponse("ERROR_QUANTITY_VALUE_NOT_VALID", HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (ProductNotFoundException e) {
            return createErrorResponse("ERROR_PRODUCT_NOT_IN_CART", HttpStatus.NOT_FOUND);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_USER_NOT_FOUND", HttpStatus.NOT_FOUND);
        }
    }

}
