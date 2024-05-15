package com.retrobased.market.repositories;

import com.retrobased.market.entities.OggettoCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;



@Repository
public interface RepositoryOggettoCarrello extends JpaRepository<OggettoCarrello, Integer> {
    Integer getQuantity(Integer id);

    boolean existsById(Integer id);

    @Query("FROM ") // TODO Query da fare per vedere se esiste l'oggetto nel carrello
    boolean existsInCart(Integer idCart, Integer idObject);

}
