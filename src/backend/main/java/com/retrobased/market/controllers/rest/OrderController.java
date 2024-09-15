package com.retrobased.market.controllers.rest;

import com.retrobased.market.entities.Order;
import com.retrobased.market.services.OrderService;
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
@RequestMapping("/api/order")
@Validated
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // metodo per ottenere ordini
    @GetMapping("/get")
    public ResponseEntity<?> getOrder(
            @RequestBody @NotNull UUID customerId,
            @RequestParam(value = "page", defaultValue = "0") @Min(0) int pageNumber
    ) {
        // UUID customerId = TODO cambiare con metodo per estrarre id da token
        List<Order> result = orderService.getOrder(customerId, pageNumber);
        if (result.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

        return ResponseEntity.ok(result);
    }
}
