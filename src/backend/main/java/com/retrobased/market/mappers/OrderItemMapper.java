package com.retrobased.market.mappers;

import com.retrobased.market.dto.OrderItemDTO;
import com.retrobased.market.entities.OrderItem;

public class OrderItemMapper {
    public static OrderItemDTO toDTO(OrderItem orderItem) {
        return new OrderItemDTO(
                ProductMapper.toDTO(orderItem.getProduct()),
                orderItem.getPrice(),
                orderItem.getQuantity()
        );
    }
}
