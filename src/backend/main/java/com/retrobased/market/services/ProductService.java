package com.retrobased.market.services;

import com.retrobased.market.controllers.dto.ProductQuantityDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.ProductSeller;
import com.retrobased.market.entities.Sell;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.OrderItemRepository;
import com.retrobased.market.repositories.OrderRepository;
import com.retrobased.market.repositories.ProductRepository;
import com.retrobased.market.repositories.ProductSellerRepository;
import com.retrobased.market.repositories.SellRepository;
import com.retrobased.market.repositories.SellerRepository;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final SellerRepository sellerRepository;
    private final ProductSellerRepository productSellerRepository;
    private final SellRepository sellRepository;

    public ProductService(ProductRepository productRepository, OrderRepository orderRepository, CustomerRepository customerRepository, OrderItemRepository orderItemRepository, SellerRepository sellerRepository, ProductSellerRepository productSellerRepository, SellRepository sellRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.orderItemRepository = orderItemRepository;
        this.sellerRepository = sellerRepository;
        this.productSellerRepository = productSellerRepository;
        this.sellRepository = sellRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Product addProduct(Product product, UUID sellerId) throws ArgumentValueNotValidException {

        Product productAdded = productRepository.save(product);

        ProductSeller productSeller = new ProductSeller();
        productSeller.setProduct(productAdded);
        productSeller.setSeller(sellerRepository.getReferenceById(sellerId));

        productSellerRepository.save(productSeller);
        return productAdded;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(UUID productId) throws ProductNotFoundException {

        if (!productRepository.existsById(productId))
            throw new ProductNotFoundException();

        productRepository.removeProduct(productId);

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

        Map<UUID, Long> productIds = new HashMap<>();

        for (ProductQuantityDTO productQuantity : productQuantities) {
            UUID productId = productQuantity.getProductId();
            if (!productRepository.existsById(productId))
                throw new ProductNotFoundException();
            productIds.merge(productId, productQuantity.getQuantity(), Long::sum);
        }

        List<Product> products = productRepository.findByIdInWithLock(productIds.keySet());

        for (Product product : products) {
            Long quantityToAdd = productIds.get(product.getId());

            if (product.getQuantity() < quantityToAdd)
                throw new ArgumentValueNotValidException();

            product.setQuantity(product.getQuantity() - quantityToAdd);
        }

        Customer customer = customerRepository.findCustomerById(customerId);

        Order currentOrder = new Order();
        currentOrder.setCustomer(customer);
        currentOrder.setAddress(address);
        Order savedOrder = orderRepository.save(currentOrder);

        List<OrderItem> orderItems = new LinkedList<>();

        List<Sell> soldItems = new LinkedList<>();

        for (Product product : products) {
            Long quantityToAdd = productIds.get(product.getId());

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(quantityToAdd);
            orderItem.setOrder(savedOrder);
            orderItem.setPrice(product.getSalePrice());

            orderItems.add(orderItem);

            Sell soldItem = new Sell();
            soldItem.setProduct(product);
            soldItem.setQuantity(quantityToAdd);
            soldItem.setPrice(product.getSalePrice());
            soldItem.setCustomer(customer);

            soldItems.add(soldItem);
        }

        sellRepository.saveAll(soldItems);

        orderItemRepository.saveAll(orderItems);

        productRepository.saveAll(products);

        return currentOrder;
    }

}
