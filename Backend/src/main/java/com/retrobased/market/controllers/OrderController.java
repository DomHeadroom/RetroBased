package com.retrobased.market.controllers;

import com.retrobased.market.authentications.AuthenticationService;
import com.retrobased.market.dtos.OrderDTO;
import com.retrobased.market.dtos.OrderItemDTO;
import com.retrobased.market.dtos.ProductRequestOrderDTO;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.OrderService;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.utils.exceptions.AddressNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final CustomerAddressService customerAddressService;

    private final AuthenticationService authenticationService;

    public OrderController(
            OrderService orderService,
            ProductService productService,
            CustomerAddressService customerAddressService,
            AuthenticationService authenticationService
    ) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerAddressService = customerAddressService;
        this.authenticationService = authenticationService;
    }

    /**
     * Retrieves a paginated list of orders associated with the authenticated customer.
     * <p>
     * This method extracts the user ID from the JWT token to identify the customer and fetches
     * their orders. If the customer is not authenticated, it returns a <strong>401 Unauthorized</strong>
     * response. If no orders are found for the customer, a <strong>204 No Content</strong> response is returned.
     * </p>
     *
     * @param pageNumber the page number of orders to retrieve, starting from 0; defaults to 0 if not specified, must be a non-negative integer
     * @return A {@link ResponseEntity} containing a list of {@link OrderDTO} if the operation is successful,
     * or an error message if the customer is not authenticated or no orders are found.
     * <ul>
     *     <li><strong>200 OK</strong> – If orders are successfully retrieved.</li>
     *     <li><strong>204 No Content</strong> – If the customer has no orders in the system.</li>
     *     <li><strong>401 Unauthorized</strong> – If the user ID cannot be extracted from the JWT token.</li>
     * </ul>
     */
    @GetMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOrder(
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        Optional<String> keycloakUserIdOpt = authenticationService.extractUserId();

        if (keycloakUserIdOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String keycloakUserId = keycloakUserIdOpt.get();

        List<OrderDTO> result = orderService.getOrders(keycloakUserId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    /**
     * <p>This method fetches products for the given order ID. The order must belong to the
     * authenticated customer.</p>
     * <p>
     * This method extracts the user ID from the JWT token to verify that the order
     * is associated with the authenticated customer. If the user is not authenticated,
     * a <strong>401 Unauthorized</strong> response is returned. If the order does not exist
     * or has no products, a <strong>204 No Content</strong> response is returned.
     * </p>
     *
     * @param orderId    The UUID of the order for which products are being requested.
     *                   This value must not be {@code null}.
     * @param pageNumber The page number for pagination, with a default value of 0 if
     *                   not provided. This value must be zero or greater.
     * @return A {@link ResponseEntity} containing a list of {@link OrderItemDTO}
     * objects if products are found for the order, or {@code NO_CONTENT}
     * (204 status) if the order doesn't exist or has no products.
     * <ul>
     *     <li><strong>200 OK</strong> – If products are successfully retrieved for the order.</li>
     *     <li><strong>204 No Content</strong> – If the order doesn't exist, has no products,
     *         or does not belong to the authenticated customer.</li>
     *     <li><strong>401 Unauthorized</strong> – If the user ID cannot be extracted from the JWT token.</li>
     * </ul>
     */
    @GetMapping("{order}/products")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductsFromOrder(
            @PathVariable("order") @NotNull UUID orderId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        Optional<String> keycloakUserIdOpt = authenticationService.extractUserId();

        if (keycloakUserIdOpt.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String keycloakUserId = keycloakUserIdOpt.get();

        if (!orderService.existsOrderForCustomer(keycloakUserId, orderId))
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        List<OrderItemDTO> result = orderService.getOrderedItems(orderId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    /**
     * <p>This method allows the customer to create a new order based on the products
     * they wish to purchase and the address provided.</p>
     * <p>
     * The user ID is extracted from the JWT token to ensure that the order is being
     * placed by the authenticated customer. If the user is not authenticated, a
     * <strong>403 Forbidden</strong> response is returned. The method also checks if
     * the provided address ID exists and is not deleted. If the address is invalid,
     * a <strong>400 Bad Request</strong> response is returned.
     * </p>
     *
     * @param productRequestOrder The DTO object containing the order details, including
     *                            the products to be ordered and the address ID. This
     *                            must be a valid {@link ProductRequestOrderDTO} object
     *                            as per the validation constraints.
     * @return A {@link ResponseEntity} containing the created {@link OrderDTO} if the
     * operation is successful, or an error message if the address is not found or
     * any of the product quantities are invalid.
     * <ul>
     *     <li><strong>201 Created</strong> – If the order is successfully created.</li>
     *     <li><strong>400 Bad Request</strong> – If the address does not exist, the
     *         provided address is deleted, or the product quantities are invalid.</li>
     *     <li><strong>403 Forbidden</strong> – If the user ID cannot be extracted from
     *         the JWT token.</li>
     * </ul>
     */
    @PostMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> makeOrder(
            @RequestBody @Valid @NotNull ProductRequestOrderDTO productRequestOrder
    ) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerNotFoundException, AddressNotFoundException {
        String keycloakUserId = authenticationService.extractUserId().orElseThrow(CustomerNotFoundException::new);

        Optional<CustomerAddress> customerAddressOpt = customerAddressService.getIfExistsAndNotDeleted(keycloakUserId, productRequestOrder.addressId());

        if (customerAddressOpt.isEmpty())
            throw new AddressNotFoundException();

        CustomerAddress customerAddress = customerAddressOpt.get();

        OrderDTO finalOrder = productService.lockAndReduceQuantities(productRequestOrder.products(), customerAddress, keycloakUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(finalOrder);
    }
}
