package com.retrobased.market.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "\"Ordine\"")
public class Ordine {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente idCliente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private StatusOrdine idStatus;

    @Column(name = "data_arrivo_stimato")
    private LocalDate dataArrivoStimato;

    @Column(name = "data_creazione", nullable = false)
    private LocalDate dataCreazione;

    @Column(name = "data_approvazione", nullable = false)
    private LocalDate dataApprovazione;

}