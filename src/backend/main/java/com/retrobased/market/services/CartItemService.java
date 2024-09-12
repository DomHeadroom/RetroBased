package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.CartItem;
import com.retrobased.market.repositories.*;
import com.retrobased.market.support.exceptions.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(propagation = Propagation.REQUIRED)
    public void increaseQuantity(@NonNull UUID customerId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductDontExistsException, UnableToChangeValueException, CustomerDontExistsException {
        if (quantity <= 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        Cart cart = getUserCart(customerId);

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductDontExistsException();

        if (cartItemRepository.findQuantityByCartIdAndProductId(cart.getId(), productId) > quantity)
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.updateQuantityByCartIdAndProductId(cart.getId(), productId, quantity) == 0)
            throw new UnableToChangeValueException();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NonNull UUID customerId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductDontExistsException, CustomerDontExistsException {
        if (quantity < 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        Cart cart = getUserCart(customerId);

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductDontExistsException();

        if (cartItemRepository.findQuantityByCartIdAndProductId(cart.getId(), productId) < quantity)
            throw new ArgumentValueNotValidException();

        if (quantity == 0) {
            removeProduct(cart.getId(), productId);
            return;
        }
        cartItemRepository.updateQuantityByCartIdAndProductId(cart.getId(), productId, quantity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(@NonNull UUID customerId, @NonNull UUID productId) throws ProductDontExistsException, CustomerDontExistsException {
        checkValues(customerId, productId);

        Cart cart = getUserCart(customerId);

        if (!cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductDontExistsException();

        cartItemRepository.deleteByCartIdAndProductId(cart.getId(), productId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CartItem addProductToCart(@NonNull UUID customerId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductAlreadyPresentException, ProductQuantityNotAvailableException, CustomerDontExistsException, ProductDontExistsException {

        if (quantity < 0)
            throw new ArgumentValueNotValidException();

        checkValues(customerId, productId);

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        Cart cart = getUserCart(customerId);

        if (cartItemRepository.existsByCartIdAndProductId(cart.getId(), productId))
            throw new ProductAlreadyPresentException();

        CartItem item = new CartItem();
        item.setProduct(productRepository.getReferenceById(productId));
        item.setCart(cart);
        item.setQuantity(quantity);

        return cartItemRepository.save(item);

    }

    private void checkValues(UUID customerId, UUID productId) throws ProductDontExistsException, CustomerDontExistsException {
        if (!customerRepository.existsById(customerId))
            throw new CustomerDontExistsException();

        if (!productRepository.existsById(productId))
            throw new ProductDontExistsException();
    }

    private Cart getUserCart(UUID customerId) {
        return cartService.getCustomerCart(customerId);
    }

}
