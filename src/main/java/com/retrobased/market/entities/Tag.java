package com.retrobased.market.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "\"Tag\"")
public class Tag {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "nome_tag", nullable = false, length = Integer.MAX_VALUE)
    private String nomeTag;

    @Column(name = "icona", nullable = false, length = Integer.MAX_VALUE)
    private String icona;

}