package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_sellers")
public class ProductSeller {

    @EmbeddedId
    private ProductSellerId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @MapsId("sellerId")
    @JoinColumn(name = "seller_id", nullable = false, insertable = false, updatable = false)
    private Seller seller;

    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public static class ProductSellerId implements Serializable {

        @Column(name = "product_id")
        private UUID productId;

        @Column(name = "seller_id")
        private UUID sellerId;
    }
}
