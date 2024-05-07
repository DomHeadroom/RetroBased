package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "\"Prodotto\"")
public class Prodotto {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "prezzo", nullable = false)
    private BigDecimal prezzo;

    @Column(name = "\"quantità\"", nullable = false)
    private BigDecimal quantità;

    @Column(name = "versione", nullable = false)
    private Integer versione;

    @Column(name = "descrizione", nullable = false, length = Integer.MAX_VALUE)
    private String descrizione;

    @Column(name = "nome", nullable = false, length = Integer.MAX_VALUE)
    private String nome;

}