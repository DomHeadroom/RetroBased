package com.retrobased.market.services;

import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.OrderItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(readOnly = true)
    public Page<Product> getOrderProducts(UUID orderId, Pageable paging) {
        return orderItemRepository.findByOrderId(orderId, paging);
    }

    @Transactional(readOnly = true)
    public Page<Product> getOrderProduct(UUID orderId, Pageable paging) {
        return orderItemRepository.findByOrderId(orderId, paging);
    }

    @Transactional(readOnly = true)
    public Page<OrderItem> getOrderItem(UUID orderId, Pageable paging) {
        return orderItemRepository.getByOrderId(orderId, paging);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<OrderItem> save(List<OrderItem> items) {
        return orderItemRepository.saveAll(items);
    }
}
