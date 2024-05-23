package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.ArgumentValueNotValid;
import com.retrobased.market.support.exceptions.ProductNotExist;
import com.retrobased.market.support.exceptions.ProductQuantityNotAvailable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OggettoCarrelloService {
    @Autowired
    private RepositoryOggettoCarrello repoCart;
    @Autowired
    private RepositoryProdotto repoProd;
    @Autowired
    private RepositoryCliente repoCliente;

    public void increaseQuantity(Integer idCliente, Integer idProd, Integer value) throws ArgumentValueNotValid, ProductQuantityNotAvailable, ProductNotExist {
        if (!repoProd.existsById(idProd))
            throw new ProductNotExist();

        if (value <= 0
                || !repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoCart.existsProdottoByClienteId(idCliente, idProd) // controllo se il cliente ha il prodotto
                || repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) > 0
        )
            throw new ArgumentValueNotValid();

        if (repoProd.getQuantità(idProd).compareTo(value) < 0)
            throw new ProductQuantityNotAvailable();

        repoCart.changeQuantity(idCliente, idProd, value);
    }

    public void decreaseQuantity(Integer idCliente, Integer idProd, Integer value) throws ArgumentValueNotValid, ProductNotExist {
        if (!repoProd.existsById(idProd))
            throw new ProductNotExist();

        if (value <= 0
                || !repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoCart.existsProdottoByClienteId(idCliente, idProd)
                || repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) < 0
        )
            throw new ArgumentValueNotValid();

        if (repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) == 0) {
            removeProdotto(idCliente, idProd);
            return ;
        }
        repoCart.changeQuantity(idCliente, idProd, value);
    }

    public void removeProdotto(Integer idCliente, Integer idProd) throws ArgumentValueNotValid, ProductNotExist {
        if(!repoProd.existsById(idProd))
            throw new ProductNotExist();

        if (!repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoCart.existsProdottoByClienteId(idCliente, idProd)
        )
            throw new ArgumentValueNotValid();

        // rimozione

    }

}
