package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "\"Vendita\"")
public class Vendita {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "prezzo", nullable = false)
    private Integer prezzo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto idProdotto;

    @Column(name = "\"quantità\"", nullable = false)
    private Integer quantità;

}