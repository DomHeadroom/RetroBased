package com.retrobased.market.mappers;

import com.retrobased.market.dtos.CartItemDTO;
import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.entities.CartItem;

public class CartItemMapper {
    /**
     * Maps a CartItem entity to a CartItemDTO.
     *
     * @param cartItem the CartItem entity to be mapped
     * @return a CartItemDTO representing the product and its quantity
     */
    public static CartItemDTO toDTO(CartItem cartItem) {
        if (cartItem == null || cartItem.getProduct() == null)
            return null;

        ProductDTO productDTO = ProductMapper.toDTO(cartItem.getProduct());

        return new CartItemDTO(productDTO, cartItem.getQuantity());
    }
}
