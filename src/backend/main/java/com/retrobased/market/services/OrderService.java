package com.retrobased.market.services;

import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.CartItemRepository;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.OrderItemRepository;
import com.retrobased.market.repositories.OrderRepository;
import com.retrobased.market.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    public OrderService(OrderItemRepository orderItemRepository, OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> getOrder(UUID customerId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<Order> orders = orderRepository.findByCustomerId(customerId, paging);

        if (orders.hasContent())
            return orders.getContent();

        return new ArrayList<>();
    }

    public List<Product> getOrderProducts(UUID orderId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<Product> orders = orderItemRepository.findByOrderId(orderId, paging);

        if (orders.hasContent())
            return orders.getContent();

        return new ArrayList<>();
    }

}
