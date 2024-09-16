package com.retrobased.market.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "slug", nullable = false, unique = true)
    @NotBlank(message = "This field cannot be empty.")
    private String slug;

    @Column(name = "product_name", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sale_price", nullable = false)
    @NotNull(message = "A value is required for this field.")
    @Min(0L)
    private BigDecimal salePrice = BigDecimal.ZERO;

    @Column(name = "compare_price")
    private BigDecimal comparePrice = BigDecimal.ZERO;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;
    // TODO non sono sicuro questo sia necessario

    @Column(name = "quantity", nullable = false)
    @NotNull(message = "A value is required for this field.")
    @Min(1)
    private Long quantity = 0L;

    @Column(name = "short_description", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String shortDescription;

    @Column(name = "product_description", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String productDescription;

    @Column(name = "product_type", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String productType;

    @JsonIgnore
    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @Column(name = "disable_out_of_stock", nullable = false)
    private Boolean disableOutOfStock = true;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted")
    @JsonIgnore
    private Boolean deleted;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt = OffsetDateTime.now();

}
