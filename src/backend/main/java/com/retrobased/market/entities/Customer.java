package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String firstName;

    @Column(name = "last_name", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String lastName;

    @Column(name = "email", nullable = false)
    @NotBlank(message = "This field cannot be empty.")
    private String email;

    @Column(name = "registered_at", nullable = false, updatable = false, insertable = false)
    private OffsetDateTime registeredAt = OffsetDateTime.now();

}
