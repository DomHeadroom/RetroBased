package com.retrobased.market.services;

import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductSeller;
import com.retrobased.market.repositories.ProductSellerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ProductSellerService {
    private final ProductSellerRepository productSellerRepository;

    public ProductSellerService(ProductSellerRepository productSellerRepository) {
        this.productSellerRepository = productSellerRepository;
    }

    // TODO get Seller Product
    @Transactional(readOnly = true)
    public List<Product> getSellerProducts(int pageNumber, String sortBy, UUID sellerId) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productSellerRepository.findProductsBySellerId(sellerId, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public boolean existsProductForSeller(UUID productId, UUID sellerId) {
        return productSellerRepository.existsByIdProductIdAndIdSellerId(productId, sellerId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductSeller save(ProductSeller productSeller) {
        return productSellerRepository.save(productSeller);
    }
}
