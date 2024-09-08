package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryProdotto;
import com.retrobased.market.support.exceptions.ArgumentValueNotValid;
import com.retrobased.market.support.exceptions.IdProductAlreadyUsed;

import com.retrobased.market.support.exceptions.ProductNotExist;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProdotto {
    private final RepositoryProdotto repoProd;
    private final RepositoryProdotto repositoryProdotto;

    public ServiceProdotto(RepositoryProdotto repoProd, RepositoryProdotto repositoryProdotto) {
        this.repoProd = repoProd;
        this.repositoryProdotto = repositoryProdotto;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Prodotto addProdotto(@NonNull Prodotto prod) throws IdProductAlreadyUsed, ArgumentValueNotValid, ValueCannotBeEmpty {
        if (prod.getPrezzo() == null
                || prod.getQuantità() == null
                || prod.getVersione() == null
                || prod.getDescrizione() == null
                || prod.getNome() == null
                || prod.getCodice_a_barre() == null
        )
            throw new ValueCannotBeEmpty();

        if (repoProd.existsById(prod.getId()))
            throw new IdProductAlreadyUsed();

        if (prod.getQuantità().signum() == -1
                || prod.getPrezzo().signum() == -1
        )
            throw new ArgumentValueNotValid();


        return repoProd.save(prod);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProdotto(@NonNull Prodotto prod) throws ValueCannotBeEmpty, ProductNotExist {
        if (prod.getId() == null)
            throw new ValueCannotBeEmpty();

        if (!repoProd.existsById(prod.getId()))
            throw new ProductNotExist();

        repoProd.deleteById(prod.getId());

    }

    // TODO SI DEVE CREARE UN NUOVO METODO PER LA RICERCA SORTATA
    @Transactional(readOnly = true)
    public List<Prodotto> showAllProducts(int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Prodotto> pagedResult = repoProd.findAll(paging);
        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Prodotto> searchProducts(String name, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Prodotto> pagedResult = repoProd.find(name, paging);
        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    public Prodotto saveProdotto(Prodotto prodotto) {
        return repositoryProdotto.save(prodotto);
    }
}
