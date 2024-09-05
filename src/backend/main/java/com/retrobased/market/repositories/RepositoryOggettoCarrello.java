package com.retrobased.market.repositories;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.entities.Prodotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface RepositoryOggettoCarrello extends JpaRepository<OggettoCarrello,Integer> {

    @Query("SELECT CASE WHEN COUNT(oc) > 0 THEN TRUE ELSE FALSE END " +
            "FROM OggettoCarrello oc " +
            "WHERE oc.idCliente.id = ?1 AND oc.idProdotto.id = ?2")
    boolean existsByIdClienteAndIdProdotto(Long idCliente, Long idProdotto);

    boolean existsById(Long id);

    @Modifying
    @Query("UPDATE OggettoCarrello og " +
            "SET og.quantità = ?3 " +
            "WHERE og.idCliente = ?1 AND og.idProdotto = ?2"
    )
    void changeQuantity(Long idCliente, Long idProdotto, BigDecimal quantity);

    @Query("SELECT og.quantità " +
            "FROM OggettoCarrello og " +
            "WHERE og.idCliente = ?1 AND og.idProdotto = ?2"
    )
    BigDecimal findQuantitàByIdClienteAndIdProdotto(Long idCliente, Long idProdotto);

    @Modifying
    @Transactional
    @Query("DELETE FROM OggettoCarrello oc " +
            "WHERE oc.idCliente.id = ?1 AND oc.idProdotto.id = ?2")
    void deleteByIdClienteAndIdProdotto(Long idCliente, Long idProdotto);
}
