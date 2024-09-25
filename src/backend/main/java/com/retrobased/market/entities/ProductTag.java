package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Entity
@Table(name = "product_tags")
public class ProductTag {

    @EmbeddedId
    @Valid
    private ProductTagId id;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", nullable = false, insertable = false, updatable = false)
    private Product product;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id", nullable = false, insertable = false, updatable = false)
    private Tag tag;


    @Embeddable
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode

    public static class ProductTagId implements Serializable {

        @Column(name = "product_id")
        @NotNull
        private UUID productId;

        @Column(name = "tag_id")
        @NotNull
        private UUID tagId;

    }
}
