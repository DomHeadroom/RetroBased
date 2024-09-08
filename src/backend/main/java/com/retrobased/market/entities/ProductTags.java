package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
@Table(name = "product_tags")
public class ProductTags {

    @Id
    @Column(name = "tag_id", nullable = false)
    private UUID tagId;

    @Id
    @Column(name = "product_id", nullable = false)
    private UUID productId;

}
