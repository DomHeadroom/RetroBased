package com.retrobased.market.services;

import com.retrobased.market.dto.ProductObjQuantityDTO;
import com.retrobased.market.dto.ProductQuantityDTO;
import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.*;
import com.retrobased.market.support.exceptions.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
import java.util.Optional;
import java.util.UUID;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;


    private final CartService cartService;
    private final ProductService productService;
    private final CustomerService customerService;

    public CartItemService(CartItemRepository cartItemRepository, CartService cartService, ProductService productService, CustomerService customerService) {
        this.cartItemRepository = cartItemRepository;
        this.cartService = cartService;
        this.productService = productService;
        this.customerService = customerService;
    }

    @Transactional(readOnly = true)
    public List<Product> getCart(UUID customerId, int pageNumber) {
        Cart customerCart = getCustomerCart(customerId);
        Pageable paging = PageRequest.of(pageNumber, 20, Sort.by(Sort.Order.desc("createdAt")));
        Page<Product> cartItems = cartItemRepository.findProductsByCartId(customerCart.getId(), paging);

        if (cartItems.hasContent())
            return cartItems.getContent();

        return new ArrayList<>();
    }

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(propagation = Propagation.REQUIRED)
    public void increaseQuantity(@NotNull UUID customerId, @NotNull UUID productId, @NotNull @Min(1) Long quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductNotFoundException, UnableToChangeValueException, CustomerDontExistsException {

        checkValues(customerId, productId);

        Cart cart = getCustomerCart(customerId);

        if (!existsProductInCart(cart.getId(), productId))
            throw new ProductNotFoundException();

        if (getProductQuantityInCart(cart.getId(), productId) > quantity)
            throw new ArgumentValueNotValidException();

        if (productService.getQuantity(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.updateQuantityByCartIdAndProductId(cart.getId(), productId, quantity) == 0)
            throw new UnableToChangeValueException();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NotNull UUID customerId, @NotNull UUID productId, @NotNull @Min(1) Long quantity) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerDontExistsException {

        checkValues(customerId, productId);

        Cart cart = getCustomerCart(customerId);

        if (!existsProductInCart(cart.getId(), productId))
            throw new ProductNotFoundException();

        if (getProductQuantityInCart(cart.getId(), productId) < quantity)
            throw new ArgumentValueNotValidException();

        if (quantity == 0) {
            removeProduct(cart.getId(), productId);
            return;
        }
        cartItemRepository.updateQuantityByCartIdAndProductId(cart.getId(), productId, quantity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(@NotNull UUID customerId, @NotNull UUID productId) throws ProductNotFoundException, CustomerDontExistsException {
        checkValues(customerId, productId);

        Cart cart = getCustomerCart(customerId);

        if (!existsProductInCart(cart.getId(), productId))
            throw new ProductNotFoundException();

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ProductObjQuantityDTO> addProductToCart(@NotNull UUID customerId, @NotNull List<ProductQuantityDTO> productQuantities)
            throws ArgumentValueNotValidException, ProductNotFoundException {

        Map<UUID, Long> productIds = new HashMap<>();

        for (ProductQuantityDTO productQuantity : productQuantities) {
            UUID productId = productQuantity.getProductId();
            if (!productService.exists(productId) ||
                    productService.isDeleted(productId) ||
                    productService.isOutOfStock(productId))
                throw new ProductNotFoundException();

            productIds.merge(productId, productQuantity.getQuantity(), Long::sum);
        }

        List<Product> products = productService.get(productIds.keySet());

        Cart cart = getCustomerCart(customerId);

        List<CartItem> newItems = new LinkedList<>();
        List<ProductObjQuantityDTO> resultItems = new LinkedList<>();

        for (Product product : products) {
            UUID productId = product.getId();
            Long quantityToAdd = productIds.get(productId);

            if (product.getQuantity() < quantityToAdd)
                throw new ArgumentValueNotValidException();

            Optional<CartItem> existingCartItemOpt = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId);

            if (existingCartItemOpt.isPresent()) {
                CartItem existingCartItem = existingCartItemOpt.get();
                Long newQuantity = existingCartItem.getQuantity() + quantityToAdd;
                existingCartItem.setQuantity(newQuantity);
                cartItemRepository.save(existingCartItem);

                resultItems.add(createProductObjQuantityDTO(product, newQuantity));
                continue;
            }

            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setCart(cart);
            newItem.setQuantity(quantityToAdd);
            newItems.add(newItem);

            resultItems.add(createProductObjQuantityDTO(product, quantityToAdd));

        }

        if (!newItems.isEmpty())
            cartItemRepository.saveAll(newItems);

        return resultItems;
    }

    private ProductObjQuantityDTO createProductObjQuantityDTO(Product product, Long quantity) {
        ProductObjQuantityDTO dto = new ProductObjQuantityDTO();
        dto.setProduct(product);
        dto.setQuantity(quantity);
        return dto;
    }

    private void checkValues(UUID customerId, UUID productId) throws ProductNotFoundException, CustomerDontExistsException {
        if (!customerService.exists(customerId))
            throw new CustomerDontExistsException();

        if (!productService.exists(productId))
            throw new ProductNotFoundException();
    }

    @Transactional(readOnly = true)
    public Cart getCustomerCart(UUID customerId) {
        return cartService.getCustomerCart(customerId);
    }

    @Transactional(readOnly = true)
    public Long getProductQuantityInCart(UUID cartId, UUID productId) {
        return cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId);
    }

    @Transactional(readOnly = true)
    public boolean existsProductInCart(UUID cartId, UUID productId) {
        return cartItemRepository.existsByCartIdAndProductId(cartId, productId);
    }

}
