package com.retrobased.market.mappers;

import com.retrobased.market.dtos.OrderDTO;
import com.retrobased.market.entities.Order;

public class OrderMapper {
    public static OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                CustomerAddressMapper.toDTO(order.getAddress()),
                order.getOrderApprovedAt(),
                order.getOrderDeliveredCarrierDate(),
                order.getOrderDeliveredCustomerDate(),
                order.getCreatedAt()
        );
    }
}
