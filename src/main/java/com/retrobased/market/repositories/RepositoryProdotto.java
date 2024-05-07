package com.retrobased.market.repositories;

import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryProdotto  extends JpaRepository<Prodotto,Integer> {
    @Query("SELECT p " +
            "FROM Prodotto p " +
            "WHERE (p.nome LIKE ?1 OR ?1 IS NULL) AND " +
            "      (p.descrizione LIKE ?2 OR ?2 IS NULL)")
    List<Prodotto> advancedSearch(String name, String description);
}
