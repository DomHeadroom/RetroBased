package com.retrobased.market.services;

import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductSeller;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.mappers.ProductMapper;
import com.retrobased.market.repositories.ProductRepository;
import com.retrobased.market.repositories.ProductSellerRepository;
import com.retrobased.market.utils.exceptions.SellerNotFoundException;
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
import java.util.stream.Collectors;

@Service
public class ProductSellerService {
    private final ProductSellerRepository productSellerRepository;
    private final SellerService sellerService;
    private final ProductRepository productRepository;

    public ProductSellerService(
            ProductSellerRepository productSellerRepository,
            SellerService sellerService,
            ProductRepository productRepository) {
        this.productSellerRepository = productSellerRepository;
        this.sellerService = sellerService;
        this.productRepository = productRepository;
    }

    // TODO get Seller Product
    @Transactional(readOnly = true)
    public List<ProductDTO> getSellerProducts(UUID sellerId, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productSellerRepository.findProductsBySellerId(sellerId, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent()
                    .stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public boolean existsProductForSeller(UUID productId, UUID sellerId) {
        return productSellerRepository.existsByIdProductIdAndIdSellerId(productId, sellerId)
                && productRepository.existsByIdAndDeleted(productId, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ProductSeller save(ProductSeller productSeller) {
        return productSellerRepository.save(productSeller);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void create(UUID sellerId, Product product) throws SellerNotFoundException {
        Seller seller = sellerService.get(sellerId).orElseThrow(SellerNotFoundException::new);
        ProductSeller productSeller = new ProductSeller();
        productSeller.setSeller(seller);
        productSeller.setProduct(product);
        save(productSeller);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void createAndSave(Product product, Seller seller) {
        ProductSeller.ProductSellerId productSellerId = new ProductSeller.ProductSellerId(
                product.getId(),
                seller.getId()
        );

        ProductSeller productSeller = new ProductSeller();
        productSeller.setId(productSellerId);
        productSeller.setProduct(product);
        productSeller.setSeller(seller);
        save(productSeller);
    }

}
