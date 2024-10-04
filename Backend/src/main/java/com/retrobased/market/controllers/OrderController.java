package com.retrobased.market.controllers;

import com.retrobased.market.dtos.OrderDTO;
import com.retrobased.market.dtos.OrderItemDTO;
import com.retrobased.market.dtos.ProductRequestOrderDTO;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.OrderService;
import com.retrobased.market.services.ProductService;
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

import static com.retrobased.market.utils.ResponseUtils.createErrorResponse;

@RestController
@RequestMapping("orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final CustomerAddressService customerAddressService;

    public OrderController(
            OrderService orderService,
            ProductService productService,
            CustomerAddressService customerAddressService
    ) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerAddressService = customerAddressService;
    }

    // metodo per ottenere ordini
    @GetMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getOrder(
            @RequestParam(value = "user") @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<OrderDTO> result = orderService.getOrders(customerId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    /**
     * <p>This method fetches products for the given order ID. The order must belong to the
     * authenticated customer.</p>
     *
     * @param orderId    The UUID of the order for which products are being requested.
     *                   This value must not be {@code null}.
     * @param pageNumber The page number for pagination, with a default value of 0 if
     *                   not provided. This value must be zero or greater.
     * @return A {@link ResponseEntity} containing a list of {@link OrderItemDTO}
     * objects if products are found for the order, or {@code NO_CONTENT}
     * (204 status) if the order doesn't exist or has no products.
     */
    @GetMapping("{order}/products")
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getProductsFromOrder(
            @PathVariable("order") @NotNull UUID orderId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        UUID customerId = UUID.fromString("a6cd2287-bb39-48b8-b1d7-62ec612ba064"); // TODO cambiare con metodo per estrarre id da token
        if (!orderService.existsOrderForCustomer(customerId, orderId))
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        List<OrderItemDTO> result = orderService.getOrderedItems(orderId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    // @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> makeOrder(
            @RequestBody @Valid @NotNull ProductRequestOrderDTO productRequestOrder
    ) {
        try {
            // a6cd2287-bb39-48b8-b1d7-62ec612ba064
            UUID customerId = UUID.fromString("a6cd2287-bb39-48b8-b1d7-62ec612ba064"); // TODO cambiare con metodo per estrarre id da token

            Optional<CustomerAddress> customerAddressOpt = customerAddressService.getIfExistsAndNotDeleted(customerId, productRequestOrder.addressId());

            if (customerAddressOpt.isEmpty())
                return createErrorResponse("ERROR_ADDRESS_NOT_FOUND", HttpStatus.BAD_REQUEST);

            CustomerAddress customerAddress = customerAddressOpt.get();

            OrderDTO finalOrder = productService.lockAndReduceQuantities(productRequestOrder.products(), customerAddress, customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(finalOrder);
        } catch (ArgumentValueNotValidException | ProductNotFoundException e) {
            return createErrorResponse("ERROR_VALUE_NOT_PERMITTED", HttpStatus.BAD_REQUEST);
        } catch (CustomerNotFoundException e) {
            return createErrorResponse("ERROR_TOKEN_USER", HttpStatus.FORBIDDEN);
        }
    }
}
