package com.retrobased.market.services;

import com.retrobased.market.dtos.OrderDTO;
import com.retrobased.market.dtos.OrderItemDTO;
import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.mappers.OrderItemMapper;
import com.retrobased.market.mappers.OrderMapper;
import com.retrobased.market.repositories.OrderRepository;
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
public class OrderService {
    private final OrderRepository orderRepository;

    private final OrderItemService orderItemService;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemService orderItemService
    ) {
        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
    }

    /**
     * Retrieves a paginated list of {@link Order} objects associated with a specific customer.
     * Orders are sorted in descending order based on the creation date (i.e., most recent first).
     * Supports pagination with a page size of 20 items.
     *
     * @param customerId The UUID of the customer whose orders are to be retrieved.
     *                   This identifies the customer in the database.
     * @param pageNumber The page number to retrieve, with results paginated in sets of 20.
     *                   Must be a non-negative integer.
     * @return A list of {@link Order} objects associated with the specified customer.
     * If no orders exist for the customer or the page is empty, returns an empty list.
     * @apiNote This method is read-only, using pagination to manage the number of orders returned per request.
     * Orders are sorted by their creation timestamp in descending order to show the most recent orders first.
     * @see OrderRepository#findByCustomerId(UUID, Pageable) OrderRepository.findByCustomerId
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrders(UUID customerId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<Order> orders = orderRepository.findByCustomerId(customerId, paging);

        if (orders.hasContent())
            return orders.getContent()
                    .stream()
                    .map(OrderMapper::toDTO)
                    .collect(Collectors.toList());

        return new ArrayList<>();
    }

    /**
     * Retrieves the list of {@link OrderItem} objects associated with a specific order.
     * Supports pagination with a page size of 20 items and sorting by item ID in ascending order.
     *
     * @param orderId    The UUID of the order from which to retrieve the {@link OrderItem} objects.
     *                   This represents the order to be queried.
     * @param pageNumber The page number to retrieve, with results being paginated in sets of 20.
     *                   Must be a non-negative integer.
     * @return A list of {@link OrderItem} objects that belong to the specified order.
     * If no items are found for the given order or the page is empty, returns an empty list.
     * @apiNote This method is read-only and uses pagination to limit the number of order items returned
     * per page. The items are sorted in ascending order based on their ID.
     * @see OrderItemService#getOrderItem(UUID, Pageable) OrderItemService.getOrderItem
     */
    @Transactional(readOnly = true)
    public List<OrderItemDTO> getOrderedItems(UUID orderId, int pageNumber) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("created_at")));
        Page<OrderItem> orders = orderItemService.getOrderItem(orderId, paging);

        if (orders.hasContent())
            return orders.getContent()
                    .stream()
                    .map(OrderItemMapper::toDTO)
                    .collect(Collectors.toList());

        return new ArrayList<>();

    }

    @Transactional(readOnly = true)
    public boolean existsOrderForCustomer(UUID customerId, UUID orderId) {
        return orderRepository.existsByCustomerIdAndId(customerId, orderId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Order save(Order order) {
        return orderRepository.save(order);
    }

}
