package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.entities.Ordine;
import com.retrobased.market.entities.Prodotto;
import org.springframework.data.domain.Example;
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
            "WHERE o.idCliente == ?1")
    List<Ordine> findOrder(Integer id); // TODO CAMBIARE CON TOKEN

    @Query("SELECT po.idProdotto " +
            "FROM Ordine o, ProdottoOrdinato po " +
            "WHERE o.idCliente == ?1 AND " +
            "o.id == po.idOrdine.id"
    )
    List<Prodotto> findOrdedItemsByUser(Integer id); // TODO CAMBIARE CON TOKEN

    @Query("SELECT o.idProdotto " +
            "FROM OggettoCarrello o " +
            "WHERE o.idCliente == ?1")
    List<Prodotto> findCartItemsByUser(Integer id);

    boolean NotExistById(Integer idCart);
}
