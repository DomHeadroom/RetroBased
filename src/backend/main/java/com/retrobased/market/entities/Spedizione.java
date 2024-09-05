package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Spedizione\"")
public class Spedizione implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_ordine", nullable = false)
    private Ordine idOrdine;

    @Column(name = "costo_spedizione", nullable = false)
    private Integer costoSpedizione;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_indirizzo", nullable = false)
    private IndirizzoCliente idIndirizzo;

}