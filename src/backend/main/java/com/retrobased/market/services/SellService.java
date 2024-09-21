package com.retrobased.market.services;

import com.retrobased.market.entities.Sell;
import com.retrobased.market.repositories.SellRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class SellService {
    private final SellRepository sellRepository;

    public SellService(SellRepository sellRepository) {
        this.sellRepository = sellRepository;
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public List<Sell> save(List<Sell> sells) {
        return sellRepository.saveAll(sells);
    }

}
