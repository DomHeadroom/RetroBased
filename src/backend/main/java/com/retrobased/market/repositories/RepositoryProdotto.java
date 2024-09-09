package com.retrobased.market.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface RepositoryProdotto extends JpaRepository<Prodotto, Long> {

    @Query("SELECT p " +
            "FROM Prodotto p " +
            "WHERE (p.nome LIKE ?1 OR ?1 IS NULL) AND " +
            "      (p.descrizione LIKE ?2 OR ?2 IS NULL)")
    List<Prodotto> advancedSearch(String name, String description);


    @Query("SELECT tp.idProdotto " +
            "FROM TagProdotto tp " +
            "WHERE tp.idTag = ?1"
    )
    List<Prodotto> findByTagId(Long id);
}
