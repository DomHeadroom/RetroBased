package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.repositories.RepositoryOggettoCarrello;
import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.ArgumentValueNotValid;
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

    // TODO CAMBIARE I CHECK CHE HANNO SOLO UN ECCEZZIONE A PIù ECCEZZIONI
    public void increaseQuantity(Integer idCliente, Integer idProd, Integer value) throws ArgumentValueNotValid {
        if (value <= 0
                || !repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoProd.existsById(idProd) // controllo se esiste il prodotto
                || !repoCart.existsProdottoByClienteId(idCliente, idProd) // controllo se il cliente ha il prodotto
                || repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) < 0
                || repoProd.getQuantità(idProd).compareTo(value) < 0
        ) {
            throw new ArgumentValueNotValid();
        }

        repoCart.changeQuantity(idCliente, idProd, value);
    }

    public void decreaseQuantity(Integer idCliente, Integer idProd, Integer value) throws ArgumentValueNotValid {
        if (value >= 0
                || !repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoProd.existsById(idProd) // controllo se esiste il prodotto
                || !repoCart.existsProdottoByClienteId(idCliente, idProd)
                || repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) > 0
        ) {
            throw new ArgumentValueNotValid();
        }

        if (repoCart.getQuantityProdotto(idCliente, idProd).compareTo(value) == 0) {
            removeProdotto(idCliente,idProd);
            return ;
        }
        repoCart.changeQuantity(idCliente, idProd, value);
    }

    public void removeProdotto(Integer idCliente, Integer idProd) throws ArgumentValueNotValid {
        if (!repoCliente.existById(idCliente) // controllo se esiste il cliente
                || !repoProd.existsById(idProd) // controllo se esiste il prodotto
                || !repoCart.existsProdottoByClienteId(idCliente, idProd)
        ) {
            throw new ArgumentValueNotValid();
        }

        // rimozione

    }

}
