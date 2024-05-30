package com.retrobased.market.services;

import com.retrobased.market.entities.OggettoCarrello;
import com.retrobased.market.entities.Prodotto;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ServiceOggettoCarrello {
    private final RepositoryOggettoCarrello repoCart;

    private final RepositoryProdotto repoProd;

    private final RepositoryCliente repoCliente;

    public ServiceOggettoCarrello(RepositoryOggettoCarrello repoCart, RepositoryProdotto repoProd, RepositoryCliente repoCliente) {
        this.repoCart = repoCart;
        this.repoProd = repoProd;
        this.repoCliente = repoCliente;
    }

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(propagation = Propagation.REQUIRED)
    public void increaseQuantity(@NonNull Integer idCliente, @NonNull Prodotto product, @NonNull BigDecimal quantity) throws ArgumentValueNotValid, ProductQuantityNotAvailable, ProductNotExist, ValueCannotBeEmpty, ClientNotExist {
        checkValues(idCliente, product);

        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, product.getId()))
            throw new ProductNotExist();

        if (quantity.compareTo(BigDecimal.ZERO) <= 0
                || repoCart.findQuantitàByIdClienteAndIdProdotto(idCliente, product.getId()).compareTo(quantity) > 0
        )
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(product.getId()).compareTo(quantity) < 0)
            throw new ProductQuantityNotAvailable();

        repoCart.changeQuantity(idCliente, product.getId(), quantity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NonNull Integer idCliente, @NonNull Prodotto product, @NonNull BigDecimal quantity) throws ArgumentValueNotValid, ProductNotExist, ValueCannotBeEmpty, ClientNotExist {
        checkValues(idCliente, product);

        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, product.getId()))
            throw new ProductNotExist();

        if (quantity.compareTo(BigDecimal.ZERO) < 0
                || repoCart.findQuantitàByIdClienteAndIdProdotto(idCliente, product.getId()).compareTo(quantity) < 0
        )
            throw new ArgumentValueNotValid();

        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            removeProdotto(idCliente, product);
            return;
        }
        repoCart.changeQuantity(idCliente, product.getId(), quantity);
    }

    private void checkValues(Integer idCliente, Prodotto product) throws ValueCannotBeEmpty, ClientNotExist, ProductNotExist {
        if (product.getId() == null)
            throw new ValueCannotBeEmpty();

        if (repoCliente.NotExistById(idCliente))
            throw new ClientNotExist();

        if (!repoProd.existsById(product.getId()))
            throw new ProductNotExist();
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProdotto(@NonNull Integer idCliente, @NonNull Prodotto product) throws ValueCannotBeEmpty, ClientNotExist, ProductNotExist {
        checkValues(idCliente, product);

        if (!repoCart.existsByIdClienteAndIdProdotto(idCliente, product.getId()))
            throw new ProductNotExist();

        repoCart.deleteByIdClienteAndIdProdotto(idCliente, product.getId());

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public OggettoCarrello addProdotto(@NonNull Integer idCliente, @NonNull Prodotto product, @NonNull BigDecimal quantity) throws ValueCannotBeEmpty, ClientNotExist, ClientTokenMismatch, ArgumentValueNotValid, ProductNotExist, ProductAlreadyPresent, ProductQuantityNotAvailable {
        checkValues(idCliente, product);

        if (quantity.compareTo(BigDecimal.ONE) < 0)
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(product.getId()).compareTo(quantity) > 0)
            throw new ProductQuantityNotAvailable();

        if (repoCart.existsByIdClienteAndIdProdotto(idCliente, product.getId()))
            throw new ProductAlreadyPresent();

        OggettoCarrello object = new OggettoCarrello();
        object.setIdProdotto(repoProd.getReferenceById(product.getId()));
        object.setIdCliente(repoCliente.getReferenceById(idCliente));
        object.setQuantità(quantity);

        return repoCart.save(object);

    }


}
