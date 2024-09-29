package com.retrobased.market.mappers;

import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.dtos.ProductObjQuantityDTO;
import com.retrobased.market.entities.CartItem;

public class CartItemMapper {
    /**
     * Maps a CartItem entity to a ProductObjQuantityDTO.
     *
     * @param cartItem the CartItem entity to be mapped
     * @return a ProductObjQuantityDTO representing the product and its quantity
     */
    public static ProductObjQuantityDTO toProductObjQuantityDTO(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null)
            return null;

        ProductDTO productDTO = ProductMapper.toDTO(cartItem.getProduct());

        return new ProductObjQuantityDTO(productDTO, cartItem.getQuantity());
    }
}
