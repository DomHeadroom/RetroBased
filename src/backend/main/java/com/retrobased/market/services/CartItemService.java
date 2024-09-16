package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
import com.retrobased.market.repositories.*;
import com.retrobased.market.support.exceptions.*;
import jakarta.validation.constraints.NotNull;
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

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    private final CartService cartService;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository, CustomerRepository customerRepository, CartService cartService) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.cartService = cartService;
    }

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
    public void increaseQuantity(@NotNull UUID customerId, @NotNull UUID productId, @NotNull Long quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductNotFoundException, UnableToChangeValueException, CustomerDontExistsException {
        if (quantity <= 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        Cart cart = getCustomerCart(customerId);

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductNotFoundException();

        if (cartItemRepository.findQuantityByCartIdAndProductId(cart.getId(), productId) > quantity)
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.updateQuantityByCartIdAndProductId(cart.getId(), productId, quantity) == 0)
            throw new UnableToChangeValueException();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NotNull UUID customerId, @NotNull UUID productId, @NotNull Long quantity) throws ArgumentValueNotValidException, ProductNotFoundException, CustomerDontExistsException {
        if (quantity < 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        Cart cart = getCustomerCart(customerId);

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductNotFoundException();

        if (cartItemRepository.findQuantityByCartIdAndProductId(cart.getId(), productId) < quantity)
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

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductNotFoundException();

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CartItem addProductToCart(@NotNull UUID customerId, @NotNull UUID productId, @NotNull Long quantity) throws ArgumentValueNotValidException, ProductAlreadyPresentException, ProductQuantityNotAvailableException, CustomerDontExistsException, ProductNotFoundException {

        if (quantity <= 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        Cart cart = getCustomerCart(customerId);

        if (cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductAlreadyPresentException();

        CartItem item = new CartItem();
        item.setProduct(productRepository.getReferenceById(productId));
        item.setCart(cart);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);

    }

    private void checkValues(UUID customerId, UUID productId) throws ProductNotFoundException, CustomerDontExistsException {
        if (!customerRepository.existsById(customerId))
            throw new CustomerDontExistsException();

        if (!productRepository.existsById(productId))
            throw new ProductNotFoundException();
    }

    private Cart getCustomerCart(UUID customerId) {
        return cartService.getCustomerCart(customerId);
    }

}
