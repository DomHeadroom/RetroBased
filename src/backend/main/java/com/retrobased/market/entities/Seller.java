package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import jakarta.validation.constraints.NotBlank;

@Data
@Entity
@Table(name = "sellers")
public class Seller {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

    @Column(name = "seller_name", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String sellerName;

    @Column(name = "company")
    private String company;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address_line1", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private Country country;

    @Column(name = "city")
    private String city;

    @Column(name = "note")
    private String note;

}
