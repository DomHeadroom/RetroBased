package com.retrobased.market.controllers;

import com.retrobased.market.dto.ProductRequestOrder;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.services.CustomerAddressService;
import com.retrobased.market.services.OrderService;
import com.retrobased.market.services.ProductService;
import com.retrobased.market.support.ResponseMessage;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;
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
@RequestMapping("/api/orders")
@Validated
public class OrderController {

    private final OrderService orderService;

    private final ProductService productService;

    private final CustomerAddressService customerAddressService;

    public OrderController(OrderService orderService, ProductService productService, CustomerAddressService customerAddressService) {
        this.orderService = orderService;
        this.productService = productService;
        this.customerAddressService = customerAddressService;
    }

    // metodo per ottenere ordini
    @GetMapping
    public ResponseEntity<?> getOrder(
            @RequestParam(value = "user") @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<Order> result = orderService.getOrder(customerId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    // metodo per ottenere prodotti da ordine
    @GetMapping("{order}/products")
    public ResponseEntity<?> getProductsFromOrder(
            @PathVariable("order") @NotNull UUID orderId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        UUID customerId = UUID.fromString("a6cd2287-bb39-48b8-b1d7-62ec612ba064"); // TODO cambiare con metodo per estrarre id da token
        if (!orderService.existsOrderForCustomer(customerId, orderId))
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        List<OrderItem> result = orderService.getByOrderId(orderId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> buyOrder(
            @RequestBody @Valid @NotNull ProductRequestOrder productRequestOrder
    ) {
        try{
            // a6cd2287-bb39-48b8-b1d7-62ec612ba064
            UUID customerId = UUID.fromString("a6cd2287-bb39-48b8-b1d7-62ec612ba064"); // TODO cambiare con metodo per estrarre id da token

            if (!customerAddressService.existsCustomerAddressForCustomer(customerId, productRequestOrder.getAddressId()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR_VALUE_NOT_PERMITTED"));

            CustomerAddress customerAddress = customerAddressService.getCustomerAddressById(productRequestOrder.getAddressId());

            Order finalOrder = productService.lockAndReduceQuantities(productRequestOrder.getProducts(),customerAddress,customerId);
            return ResponseEntity.status(HttpStatus.CREATED).body(finalOrder);
        } catch (ArgumentValueNotValidException | ProductNotFoundException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ERROR_VALUE_NOT_PERMITTED"));
        }
    }
}
