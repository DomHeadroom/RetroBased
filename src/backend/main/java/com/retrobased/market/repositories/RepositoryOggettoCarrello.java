package com.retrobased.market.repositories;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface RepositoryOggettoCarrello extends JpaRepository<OggettoCarrello,Integer> {

    boolean existsByIdClienteAndIdProdotto(Integer idCliente, Integer idProdotto);

    boolean existById(Integer id);

    @Modifying
    @Query("UPDATE OggettoCarrello og " +
            "SET og.quantità = ?3 " +
            "WHERE og.idCliente = ?1 AND og.idProdotto = ?2"
    )
    void changeQuantity(Integer idCliente, Integer idProdotto, BigDecimal quantity);

    BigDecimal findQuantitàByIdClienteAndIdProdotto(Integer idCliente, Integer idProdotto);

    void deleteByIdClienteAndIdProdotto(Integer idCliente, Integer idProdotto);

    List<Prodotto> getProdottiByIdCliente(Integer id);
}
