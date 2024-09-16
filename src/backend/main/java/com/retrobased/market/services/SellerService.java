package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerSeller(Seller seller){

        Seller sellerAdded = sellerRepository.save(seller);
        System.out.println("Saved user: " + sellerAdded);
    }
}
