package com.retrobased.market.repositories;

import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RepositoryOggettoCarrello {

    @Query("SELECT COUNT(og)" +
            "FROM OggettoCarrello og " +
            "WHERE og.idCliente == ?1 AND og.idProdotto ==?2"
    )
    boolean existsProdottoByClienteId(Integer Cliente, Integer Prodotto);

    boolean existById(Integer id);

    @Modifying
    @Query("UPDATE OggettoCarrello og " +
            "SET og.quantità = ?3 " +
            "WHERE og.idCliente = ?1 AND og.idProdotto = ?2"
    )
    void changeQuantity(Integer idCliente, Integer idProdotto, Integer quantity);

    @Query("SELECT og.quantità " +
            "FROM OggettoCarrello og " +
            "WHERE og.idCliente == ?1 AND og.idProdotto ==?2"
    )
    Integer getQuantityProdotto(Integer idCliente, Integer idProdotto);

    List<Prodotto> getProdottiByIdCliente(Integer id);
}
