package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "\"Indirizzo_Cliente\"")
public class IndirizzoCliente {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente idCliente;

    @Column(name = "\"città\"", nullable = false, length = Integer.MAX_VALUE)
    private String città;

    @Column(name = "paese", nullable = false, length = Integer.MAX_VALUE)
    private String paese;

    @Column(name = "linea_indirizzo_1", nullable = false, length = Integer.MAX_VALUE)
    private String lineaIndirizzo1;

    @Column(name = "linea_indirizzo_2", length = Integer.MAX_VALUE)
    private String lineaIndirizzo2;

    @Column(name = "codice_postale", nullable = false, length = Integer.MAX_VALUE)
    private String codicePostale;

}