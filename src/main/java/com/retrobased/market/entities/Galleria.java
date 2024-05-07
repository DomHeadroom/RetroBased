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
@Table(name = "\"Galleria\"")
public class Galleria {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prodotto", nullable = false)
    private Prodotto idProdotto;

    @Column(name = "percorso_immagine", nullable = false, length = Integer.MAX_VALUE)
    private String percorsoImmagine;

    @Column(name = "percorso_copertina", nullable = false, length = Integer.MAX_VALUE)
    private String percorsoCopertina;

    @Column(name = "ordine_visualizzazione")
    private Integer ordineVisualizzazione;

}