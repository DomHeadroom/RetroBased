package com.retrobased.market.repositories;

import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductSeller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProductSellerRepository extends JpaRepository<ProductSeller, UUID>, JpaSpecificationExecutor<ProductSeller> {

    @Query("SELECT ps.product FROM ProductSeller ps WHERE ps.seller.id = :sellerId")
    Page<Product> findProductsBySellerId(@Param("sellerId") UUID sellerId, Pageable paging );

    boolean existsByIdProductIdAndIdSellerId(UUID productId, UUID sellerId);
}