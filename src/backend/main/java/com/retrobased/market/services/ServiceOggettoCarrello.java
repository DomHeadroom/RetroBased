package com.retrobased.market.services;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceOggettoCarrello {
    @Autowired
    private RepositoryOggettoCarrello repoCart;

    @Autowired
    private RepositoryProdotto repoProd;

    @Autowired
    private RepositoryCliente repoCliente;

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void increaseQuantity(Integer idCliente, OggettoCarrello objectCart) throws ArgumentValueNotValid, ProductQuantityNotAvailable, ProductNotExist, ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch {
        if(idCliente == null
                || objectCart == null
                || objectCart.getId() == null
        )
            throw new ValueCannotBeEmpty();

        if(repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoCart.existsById(objectCart.getId()))
            throw new ProductNotExist();

        if(!objectCart.getIdCliente().getId().equals(idCliente))
            throw new ClientTokenMismatch();

        if(!repoCart.existsProdottoByClienteId(idCliente, objectCart.getIdProdotto().getId()))
            throw new ProductNotExist();

        if (objectCart.getQuantità().compareTo(BigDecimal.ZERO) <= 0
                || repoCart.getQuantityProdotto(idCliente, objectCart.getIdProdotto().getId()).compareTo(objectCart.getIdProdotto().getQuantità()) > 0
        )
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(objectCart.getIdProdotto().getId()).compareTo(objectCart.getIdProdotto().getQuantità()) < 0)
            throw new ProductQuantityNotAvailable();

        repoCart.changeQuantity(idCliente, objectCart.getIdProdotto().getId(), objectCart.getIdProdotto().getQuantità());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void decreaseQuantity(Integer idCliente, OggettoCarrello objectCart) throws ArgumentValueNotValid, ProductNotExist, ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch {
        if(idCliente == null
                || objectCart == null
                || objectCart.getId() == null
        )
            throw new ValueCannotBeEmpty();

        if(repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoCart.existsById(objectCart.getId()))
            throw new ProductNotExist();

        if(!objectCart.getIdCliente().getId().equals(idCliente))
            throw new ClientTokenMismatch();

        if(!repoCart.existsProdottoByClienteId(idCliente, objectCart.getIdProdotto().getId()))
            throw new ProductNotExist();

        if (objectCart.getQuantità().compareTo(BigDecimal.ZERO) <= 0
                || repoCart.getQuantityProdotto(idCliente, objectCart.getIdProdotto().getId()).compareTo(objectCart.getIdProdotto().getQuantità()) < 0
        )
            throw new ArgumentValueNotValid();

        if (objectCart.getQuantità().compareTo(BigDecimal.ZERO) == 0) {

            removeProdotto(idCliente, objectCart);
            return ;
        }
        repoCart.changeQuantity(idCliente, objectCart.getIdProdotto().getId(), objectCart.getQuantità());
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProdotto(Integer idCliente, OggettoCarrello objectCart) throws ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch, ArgumentValueNotValid {
        if(idCliente == null
                || objectCart == null
                || objectCart.getId() == null
        )
            throw new ValueCannotBeEmpty();

        if(repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if(!repoCart.existsById(objectCart.getId()))
            throw new ArgumentValueNotValid();

        if(!objectCart.getIdCliente().getId().equals(idCliente))
            throw new ClientTokenMismatch();

        repoCart.deleteById(objectCart.getId());

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OggettoCarrello addProdotto(Integer idCliente, OggettoCarrello og ) throws ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch, ArgumentValueNotValid, ProductNotExist, ProductAlreadyPresent {
        if(og == null
                || og.getIdCliente() == null
                || og.getIdProdotto() == null
                || og.getQuantità() == null
        )
            throw new ValueCannotBeEmpty();

        if(repoCliente.NotExistById(og.getIdCliente().getId()))
            throw new ClientNotExist();

        if(!og.getIdCliente().getId().equals(idCliente))
            throw new ClientTokenMismatch();

        if(!repoProd.existsById(og.getIdProdotto().getId()))
            throw new ProductNotExist();

        if(og.getQuantità().compareTo(BigDecimal.ONE) < 0)
            throw new ArgumentValueNotValid();

        if(repoCart.existsProdottoByClienteId(og.getIdCliente().getId(), og.getIdProdotto().getId()))
            throw new ProductAlreadyPresent();

        return repoCart.save(og);

    }



}
