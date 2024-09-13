package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import jakarta.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@Entity
@Table(name = "customer_addresses")
public class CustomerAddress {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "address_line1", nullable = false)
    @NotBlank(message = "Address Line 1 is required")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    @Column(name = "country", nullable = false)
    @NotBlank(message = "Country is required")
    private String country;

    @Column(name = "postal_code", nullable = false)
    @NotBlank(message = "Postal Code is required")
    private String postalCode;

    @Column(name = "city", nullable = false)
    @NotBlank(message = "City is required")
    private String city;

}
