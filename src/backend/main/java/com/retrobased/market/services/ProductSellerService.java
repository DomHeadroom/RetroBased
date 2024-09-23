package com.retrobased.market.services;

import com.retrobased.market.dto.ProductDTO;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductSeller;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.repositories.ProductSellerRepository;
import com.retrobased.market.support.exceptions.SellerNotFoundException;
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

    public ProductSellerService(ProductSellerRepository productSellerRepository, SellerService sellerService) {
        this.productSellerRepository = productSellerRepository;
        this.sellerService = sellerService;
    }

    // TODO get Seller Product
    @Transactional(readOnly = true)
    public List<ProductDTO> getSellerProducts(UUID sellerId, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productSellerRepository.findProductsBySellerId(sellerId, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent()
                    .stream()
                    .map(this::convertProductToDTO)
                    .collect(Collectors.toList());

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

    @Transactional(propagation = Propagation.REQUIRED)
    public void create(UUID sellerId, Product product) throws SellerNotFoundException {
        Seller seller = sellerService.get(sellerId).orElseThrow(SellerNotFoundException::new);
        ProductSeller productSeller = new ProductSeller();
        productSeller.setSeller(seller);
        productSeller.setProduct(product);
        save(productSeller);
    }

    public ProductDTO convertProductToDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getSlug(),
                product.getProductName(),
                product.getSku(),
                product.getSalePrice(),
                product.getQuantity(),
                product.getShortDescription(),
                product.getProductDescription(),
                product.getDisableOutOfStock(),
                product.getNote(),
                product.getCreatedAt()
        );
    }
}
