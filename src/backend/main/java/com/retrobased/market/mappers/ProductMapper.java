package com.retrobased.market.mappers;

import com.retrobased.market.dto.ProductDTO;
import com.retrobased.market.entities.Product;

public class ProductMapper {
    public static ProductDTO toDTO(Product product) {
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

    public static Product toEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setSlug(productDTO.slug());
        product.setProductName(productDTO.productName());
        product.setSku(productDTO.sku());
        product.setSalePrice(productDTO.salePrice());
        product.setQuantity(productDTO.quantity());
        product.setShortDescription(productDTO.shortDescription());
        product.setProductDescription(productDTO.productDescription());
        product.setNote(productDTO.note());

        return product;
    }
}
