package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Galleria\"")
public class Galleria implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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