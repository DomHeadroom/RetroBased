package com.retrobased.market.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Prodotto\"")
public class Prodotto {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "codice_a_barre", nullable = false, length = Integer.MAX_VALUE)
    private String codice_a_barre;

}