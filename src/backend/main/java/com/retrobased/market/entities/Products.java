package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Products {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sale_price", nullable = false)
    private BigDecimal salePrice;

    @Column(name = "compare_price")
    private BigDecimal comparePrice;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;
    // TODO non sono sicuro questo sia necessario

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "product_type")
    private String productType;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "disable_out_of_stock")
    private Boolean disableOutOfStock;

    @Column(name = "note")
    private String note;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

}
