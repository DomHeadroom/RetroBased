package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryProdotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdottoService {
    @Autowired
    private RepositoryProdotto prodRepo;


}
