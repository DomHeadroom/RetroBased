package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import com.retrobased.market.support.exceptions.ArgumentValueNotValid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OggettoCarrelloService {
    @Autowired
    private RepositoryOggettoCarrello repoCart;

    public void updateValueObject(Integer idCart, Integer idObject, Integer value) throws ArgumentValueNotValid {
        if(value.equals(Integer.MAX_VALUE) || value.equals(Integer.MIN_VALUE))
            throw new ArgumentValueNotValid();
        if(!repoCart.existsById(idCart))
            throw new CartNotExist();
        if(!repoCart.existsInCart(idCart,idObject))
            throw new ObjectNotExistInCart();


    }

}
