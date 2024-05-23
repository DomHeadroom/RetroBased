package com.retrobased.market.services;

import com.retrobased.market.entities.Prodotto;
import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.ArgumentValueNotValid;
import com.retrobased.market.support.exceptions.IdProductAlreadyUsed;

import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdottoService {
    @Autowired
    private RepositoryProdotto prodRepo;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Prodotto addProdotto(Prodotto prod) throws IdProductAlreadyUsed, ArgumentValueNotValid, ValueCannotBeEmpty {
        if (prod.getId() == null ||
                prod.getPrezzo() == null ||
                prod.getQuantità() == null ||
                prod.getVersione() == null ||
                prod.getDescrizione() == null ||
                prod.getNome() == null ||
                prod.getCodice_a_barre() == null
        )
            throw new ValueCannotBeEmpty();

        if (prodRepo.existsById(prod.getId()))
            throw new IdProductAlreadyUsed();

        if (prod.getQuantità().signum() == -1
                || prod.getPrezzo().signum() == -1
        )
            throw new ArgumentValueNotValid();


        return prodRepo.save(prod);
    }

    // public

}
