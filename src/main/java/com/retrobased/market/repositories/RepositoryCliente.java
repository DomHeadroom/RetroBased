package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.entities.Ordine;
import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RepositoryCliente extends JpaRepository<Cliente,Integer> {
    List<Cliente> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT o " +
            "FROM Ordine o " +
            "WHERE (o.idCliente == ?1 )")
    List<Ordine> findOrder(Integer id); // TODO CAMBIARE CON TOKEN

    // TODO fare la query in JPQL
    List<Prodotto> findOrdedItemsByUser(Integer id); // TODO CAMBIARE CON TOKEN
}
