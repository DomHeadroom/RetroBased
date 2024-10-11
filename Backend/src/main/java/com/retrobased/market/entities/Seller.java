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
@Table(name = "sellers")
public class Seller {

    // TODO da deprecare
    // in ogni caso mi sa che su keycloak uso solo il ruolo user
    // e faccio che possono sia vendere che acquistare

    @Id
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "seller_first_name", nullable = false)
    private String firstName;

    @Column(name = "seller_last_name", nullable = false)
    private String lastName;

    @Column(name = "keycloak_id", updatable = false, nullable = false, unique = true)
    private String keycloakId;

    @Column(name = "email", nullable = false, unique = true)
    private String email;


}
