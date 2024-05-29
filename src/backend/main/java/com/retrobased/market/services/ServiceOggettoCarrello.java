package com.retrobased.market.services;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.entities.Prodotto;
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
        if (idCliente == null
                || objectCart == null
                || objectCart.getId() == null
        )
            throw new ValueCannotBeEmpty();

        if (repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoCart.existsById(objectCart.getId()))
            throw new ProductNotExist();

        if (!objectCart.getIdCliente().getId().equals(idCliente))
            throw new ClientTokenMismatch();

        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, objectCart.getIdProdotto().getId()))
            throw new ProductNotExist();

        if (objectCart.getQuantità().compareTo(BigDecimal.ZERO) <= 0
                || repoCart.findQuantitàByIdClienteAndIdProdotto(idCliente, objectCart.getIdProdotto().getId()).compareTo(objectCart.getIdProdotto().getQuantità()) > 0
        )
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(objectCart.getIdProdotto().getId()).compareTo(objectCart.getIdProdotto().getQuantità()) < 0)
            throw new ProductQuantityNotAvailable();

        repoCart.changeQuantity(idCliente, objectCart.getIdProdotto().getId(), objectCart.getIdProdotto().getQuantità());
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void decreaseQuantity(Integer idCliente, Prodotto po, BigDecimal quantity) throws ArgumentValueNotValid, ProductNotExist, ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch {
        if (idCliente == null
                || po == null
                || po.getId() == null
                || quantity == null
        )
            throw new ValueCannotBeEmpty();

        if (repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoProd.existsById(po.getId()))
            throw new ProductNotExist();
        
        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, po.getId()))
            throw new ProductNotExist();

        if (quantity.compareTo(BigDecimal.ZERO) < 0
                || repoCart.findQuantitàByIdClienteAndIdProdotto(idCliente, po.getId()).compareTo(quantity) < 0
        )
            throw new ArgumentValueNotValid();

        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            removeProdotto(idCliente, po);
            return;
        }
        repoCart.changeQuantity(idCliente, po.getId(), quantity);
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeProdotto(Integer idCliente, Prodotto po) throws ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch, ArgumentValueNotValid, ProductNotExist {
        if (idCliente == null
                || po == null
                || po.getId() == null
        )
            throw new ValueCannotBeEmpty();

        if (repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, po.getId()))
            throw new ProductNotExist();

        repoCart.deleteByIdClienteAndIdProdotto(idCliente, po.getId());

    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public OggettoCarrello addProdotto(Integer idCliente, Prodotto po, BigDecimal quantity) throws ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch, ArgumentValueNotValid, ProductNotExist, ProductAlreadyPresent, ProductQuantityNotAvailable {
        if (idCliente == null
                || po == null
                || po.getId() == null
                || quantity == null
        )
            throw new ValueCannotBeEmpty();

        if (repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoProd.existsById(po.getId()))
            throw new ProductNotExist();

        if (quantity.compareTo(BigDecimal.ONE) < 0)
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(po.getId()).compareTo(quantity) > 0)
            throw new ProductQuantityNotAvailable();

        if (repoCart.existsByIdClienteAndIdProdotto(idCliente, po.getId()))
            throw new ProductAlreadyPresent();

        OggettoCarrello ognew = new OggettoCarrello();
        ognew.setIdProdotto(repoProd.getReferenceById(po.getId()));
        ognew.setIdCliente(repoCliente.getReferenceById(idCliente));
        ognew.setQuantità(quantity);

        return repoCart.save(ognew);

    }


}
