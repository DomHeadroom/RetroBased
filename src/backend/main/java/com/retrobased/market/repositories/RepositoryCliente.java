package com.retrobased.market.repositories;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.entities.Ordine;
import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface RepositoryCliente extends JpaRepository<Cliente,Long> {
    Cliente findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT o " +
            "FROM Ordine o " +
            "WHERE o.idCliente = ?1 "
    )
    List<Ordine> findOrdiniByIdCliente(Long idCliente);

    @Query("SELECT po.idProdotto " +
            "FROM ProdottoOrdinato po JOIN po.idOrdine o " +
            "WHERE o.idCliente = ?1"
    )
    List<Prodotto> findOrderedItemsByUser(Long id); // TODO CAMBIARE CON TOKEN

    // List<Prodotto> findProdottoByIdCliente(Integer idCliente);

    boolean existsById(Integer idCart);
}
