package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "slideshows")
public class Slideshow {

    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "title")
    private String title;

    @Column(name = "destination_url")
    private String destinationUrl;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "placeholder", nullable = false)
    private String placeholder;

    @Column(name = "description")
    private String description;

    @Column(name = "btn_label")
    private String btnLabel;

    @Column(name = "display_order", nullable = false)
    private Long displayOrder;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "clicks", nullable = false)
    private Long clicks;

    @Column(name = "styles")
    private String styles;

}
