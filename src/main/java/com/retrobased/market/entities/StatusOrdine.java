package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "\"Status_Ordine\"")
public class StatusOrdine {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "\"desc\"", nullable = false, length = Integer.MAX_VALUE)
    private String desc;

    @Column(name = "colore", nullable = false)
    private LocalDate colore;

    @Column(name = "icona", nullable = false, length = Integer.MAX_VALUE)
    private String icona;

}