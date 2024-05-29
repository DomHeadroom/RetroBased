package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Status_Ordine\"")
public class StatusOrdine {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "\"desc\"", nullable = false, length = Integer.MAX_VALUE)
    private String desc;

    @Column(name = "colore", nullable = false)
    private LocalDate colore;

    @Column(name = "icona", nullable = false, length = Integer.MAX_VALUE)
    private String icona;

}