package com.retrobased.market.services;

import com.retrobased.market.controllers.dto.ProductQuantityDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.OrderItemRepository;
import com.retrobased.market.repositories.OrderRepository;
import com.retrobased.market.repositories.ProductRepository;
import com.retrobased.market.support.exceptions.ArgumentValueNotValidException;

import com.retrobased.market.support.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Product addProduct(Product product) throws ArgumentValueNotValidException {
        if (product.getQuantity() < 0 ||
                product.getSalePrice().signum() == -1
        )
            throw new ArgumentValueNotValidException();


        return productRepository.save(product);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(UUID productId) throws ProductNotFoundException {

        if (!productRepository.existsById(productId))
            throw new ProductNotFoundException();

        productRepository.deleteById(productId);

    }

    // TODO SI DEVE CREARE UN NUOVO METODO PER LA RICERCA SORTATA
    @Transactional(readOnly = true)
    public List<Product> showAllProducts(int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findAll(paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional(readOnly = true)
    public List<Product> searchProduct(String name, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findByNameIgnoreCase(name, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent();

        return new ArrayList<>();
    }

    @Transactional
    public Order lockAndReduceQuantities(List<ProductQuantityDTO> productQuantities, CustomerAddress address, UUID customerId) throws ArgumentValueNotValidException, ProductNotFoundException {

        List<UUID> productIds = productQuantities.stream()
                .map(ProductQuantityDTO::getProductId)
                .distinct()
                .collect(Collectors.toList());

        List<Product> products = productRepository.findByIdInWithLock(productIds);

        if (products.size() != productQuantities.size())
            throw new ProductNotFoundException();

        for (ProductQuantityDTO productQuantity : productQuantities) {
            Product product = products.stream()
                    .filter(p -> p.getId().equals(productQuantity.getProductId()))
                    .findFirst()
                    .orElseThrow();

            if (product.getQuantity() < productQuantity.getQuantity())
                throw new ArgumentValueNotValidException();

            product.setQuantity(product.getQuantity() - productQuantity.getQuantity());
        }

        Order currentOrder = new Order();
        currentOrder.setCustomer(customerRepository.getReferenceById(customerId));
        currentOrder.setAddress(address);

        Order savedOrder = orderRepository.save(currentOrder);

        List<OrderItem> orderItems = new LinkedList<>();
        for (ProductQuantityDTO productQuantity : productQuantities) {
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(productRepository.getReferenceById(productQuantity.getProductId()));
            orderItem.setQuantity(productQuantity.getQuantity());
            orderItem.setOrder(savedOrder);
            orderItem.setPrice(orderItem.getProduct().getSalePrice());

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        productRepository.saveAll(products);

        return currentOrder;
    }

}
