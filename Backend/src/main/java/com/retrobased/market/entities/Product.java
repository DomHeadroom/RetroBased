package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class Product {

    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "sku")
    private String sku;

    @Column(name = "sale_price", nullable = false, updatable = false)
    private BigDecimal salePrice = BigDecimal.ZERO;

    @Column(name = "quantity", nullable = false)
    private Long quantity = 0L;

    @Column(name = "short_description", nullable = false)
    private String shortDescription;

    @Column(name = "product_description", nullable = false)
    private String productDescription;

    @Column(name = "published", nullable = false)
    private Boolean published = false;

    @Column(name = "disable_out_of_stock", nullable = false)
    private Boolean disableOutOfStock = false;

    @Column(name = "note")
    private String note;

    @Column(name = "deleted")
    private Boolean deleted;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

}
