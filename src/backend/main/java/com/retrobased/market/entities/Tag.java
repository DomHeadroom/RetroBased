package com.retrobased.market.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"Tag\"")
public class Tag {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome_tag", nullable = false, length = Integer.MAX_VALUE)
    private String nomeTag;

    @Column(name = "icona", nullable = false, length = Integer.MAX_VALUE)
    private String icona;

}