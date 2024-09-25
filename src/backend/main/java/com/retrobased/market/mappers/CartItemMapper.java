package com.retrobased.market.mappers;

import com.retrobased.market.dto.ProductDTO;
import com.retrobased.market.dto.ProductObjQuantityDTO;
import com.retrobased.market.entities.CartItem;

public class CartItemMapper {
    /**
     * Maps a CartItem entity to a ProductObjQuantityDTO.
     *
     * @param cartItem the CartItem entity to be mapped
     * @return a ProductObjQuantityDTO representing the product and its quantity
     */
    public static ProductObjQuantityDTO toProductObjQuantityDTO(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null) {
            return null; // or throw an exception if preferred
        }

        ProductDTO productDTO = new ProductDTO(
                cartItem.getProduct().getId(),
                cartItem.getProduct().getSlug(),
                cartItem.getProduct().getProductName(),
                cartItem.getProduct().getSku(),
                cartItem.getProduct().getSalePrice(),
                cartItem.getProduct().getQuantity(),
                cartItem.getProduct().getShortDescription(),
                cartItem.getProduct().getProductDescription(),
                cartItem.getProduct().getDisableOutOfStock(),
                cartItem.getProduct().getNote(),
                cartItem.getProduct().getCreatedAt()
        );

        return new ProductObjQuantityDTO(productDTO, cartItem.getQuantity());
    }
}
