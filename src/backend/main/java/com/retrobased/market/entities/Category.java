package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @Column(name = "category_name", nullable = false, unique = true)
    private String categoryName;

    @Column(name = "category_description")
    private String categoryDescription;

    @Column(name = "icon")
    private String icon;

    @Column(name = "image")
    private String image;

    @Column(name = "placeholder")
    private String placeholder;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

}
