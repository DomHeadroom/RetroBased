package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OggettoCarrelloService {
    @Autowired
    private RepositoryOggettoCarrello repoCart;

    public void updateValueObject(Integer id, Integer value){
        if(value.equals(Integer.MAX_VALUE) || value.equals(Integer.MIN_VALUE)){
            throw new ArgumentValueNotValid();
        }
    }

}
