package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "countries")
public class Country {

    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "iso", nullable = false)
    private String iso;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "upper_name", nullable = false)
    private String upperName;

    @Column(name = "iso3")
    private String iso3;

}
