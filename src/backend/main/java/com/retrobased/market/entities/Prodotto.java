package com.retrobased.market.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Prodotto\"")
public class Prodotto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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