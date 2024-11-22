package com.retrobased.market.services;

import com.retrobased.market.dtos.OrderDTO;
import com.retrobased.market.dtos.ProductDTO;
import com.retrobased.market.dtos.ProductQuantityDTO;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.entities.Order;
import com.retrobased.market.entities.OrderItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.entities.Seller;
import com.retrobased.market.mappers.OrderMapper;
import com.retrobased.market.mappers.ProductMapper;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.ProductRepository;
import com.retrobased.market.utils.exceptions.ArgumentValueNotValidException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import com.retrobased.market.utils.exceptions.ProductNotFoundException;
import com.retrobased.market.utils.exceptions.SellerNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductSellerService productSellerService;
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final SellerService sellerService;
    private final CustomerRepository customerRepository;

    public ProductService(
            ProductRepository productRepository,
            ProductSellerService productSellerService,
            OrderItemService orderItemService,
            OrderService orderService,
            SellerService sellerService,
            CustomerRepository customerRepository) {
        this.productRepository = productRepository;
        this.productSellerService = productSellerService;
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.sellerService = sellerService;
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Product addProduct(ProductDTO productDTO, UUID sellerId) throws SellerNotFoundException, ArgumentValueNotValidException {
        if (existSlug(productDTO.slug()))
            throw new ArgumentValueNotValidException();

        Seller seller = sellerService.get(sellerId)
                .orElseThrow(SellerNotFoundException::new);

        Product product = ProductMapper.toEntity(productDTO);
        product.setDeleted(false);
        product.setPublished(true);

        Product productAdded = productRepository.save(product);

        productSellerService.createAndSave(productAdded, seller);
        return productAdded;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(UUID productId) throws ProductNotFoundException {

        Product product = get(productId).orElseThrow(ProductNotFoundException::new);

        product.setDeleted(true);

        productRepository.save(product);

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
    public List<ProductDTO> searchProduct(String data, int pageNumber, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(sortBy));
        Page<Product> pagedResult = productRepository.findByProductNameContainingIgnoreCaseOrProductDescriptionContainingIgnoreCase(data, data, paging);

        if (pagedResult.hasContent())
            return pagedResult.getContent()
                    .stream()
                    .map(ProductMapper::toDTO)
                    .collect(Collectors.toList());

        return new ArrayList<>();
    }

    @Transactional
    public OrderDTO lockAndReduceQuantities(List<ProductQuantityDTO> productQuantities, CustomerAddress address, String keycloakId) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerNotFoundException {

        Map<UUID, Long> productIds = productQuantities.stream()
                .collect(Collectors.toMap(ProductQuantityDTO::productId, ProductQuantityDTO::quantity, Long::sum));

        List<Product> products = productRepository.findByIdIn(productIds.keySet());

        if (products.size() != productIds.size())
            throw new ProductNotFoundException();

        List<OrderItem> orderItems = new ArrayList<>();

        Customer customer = customerRepository.findByKeycloakId(keycloakId);
        Order currentOrder = new Order();
        currentOrder.setCustomer(customer);
        currentOrder.setAddress(address);

        for (Product product : products) {
            Long requestedQuantity = productIds.get(product.getId());

            if (requestedQuantity > product.getQuantity())
                throw new ArgumentValueNotValidException();

            if (product.getDeleted() ||
                    product.getDisableOutOfStock() ||
                    !product.getPublished())
                throw new ProductNotFoundException();

            product.setQuantity(product.getQuantity() - requestedQuantity);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(requestedQuantity);
            orderItem.setOrder(currentOrder);
            orderItem.setPrice(product.getSalePrice());

            orderItems.add(orderItem);
        }

        Order savedOrder = orderService.save(currentOrder);
        orderItems.forEach(orderItem -> orderItem.setOrder(savedOrder));
        orderItemService.saveAll(orderItems);

        return OrderMapper.toDTO(currentOrder);
    }

    public Product findProductWithLock(UUID productId) throws ProductNotFoundException {
        return productRepository.findByIdAndDeletedFalseAndPublishedTrue(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public Boolean exists(UUID id) {
        return productRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public Boolean existSlug(String slug) {
        return productRepository.findBySlug(slug).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Product> get(Set<UUID> ids) {
        return productRepository.findByIdInAndDeletedFalse(ids);
    }

    @Transactional(readOnly = true)
    public Optional<Product> get(UUID id) {
        return productRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Boolean isDeleted(UUID productId) {
        return productRepository.existsByIdAndDeleted(productId, true);
    }

    @Transactional(readOnly = true)
    public Boolean isOutOfStock(UUID productId) {
        return productRepository.existsByIdAndDisableOutOfStock(productId, true);
    }

    @Transactional(readOnly = true)
    public boolean isPublished(UUID productId) {
        return productRepository.existsByIdAndPublished(productId, true);
    }

    @Transactional(readOnly = true)
    public Long getQuantity(UUID productId) {
        return productRepository.findQuantityById(productId);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getRandomProducts(int pageNumber) {
        // TODO molto probabilmente non serve la page perchè li prende sempre random
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("created_at")));
        List<Product> randomProducts = productRepository.findRandomProducts(paging);

        return randomProducts.stream()
                .map(ProductMapper::toDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProduct(UUID productId) {
        return productRepository.findByIdAndDeletedIsFalseAndPublishedIsTrue(productId).map(ProductMapper::toDTO);
    }
}
