package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Prodotto_Ordinato\"")
public class ProdottoOrdinato implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto idProdotto;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ordine", nullable = false)
    private Ordine idOrdine;

    @Column(name = "prezzo", nullable = false)
    private Integer prezzo;

    @Column(name = "\"quantità\"", nullable = false)
    private Integer quantità;

}