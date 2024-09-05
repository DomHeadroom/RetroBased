package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@Table(name = "\"Status_Ordine\"")
public class StatusOrdine implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "\"desc\"", nullable = false, length = Integer.MAX_VALUE)
    private String desc;

    @Column(name = "colore", nullable = false)
    private LocalDate colore;

    @Column(name = "icona", nullable = false, length = Integer.MAX_VALUE)
    private String icona;

}