package com.retrobased.market.services;

import com.retrobased.market.dto.SellerDTO;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.mappers.SellerMapper;
import com.retrobased.market.repositories.SellerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class SellerService {
    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerSeller(SellerDTO seller) {

        sellerRepository.save(SellerMapper.toEntity(seller));
    }

    @Transactional(readOnly = true)
    public Optional<Seller> get(UUID id) {
        return sellerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public boolean exists(UUID sellerId) {
        return sellerRepository.existsById(sellerId);
    }
}
