package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "sells")
public class Sell {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

}
