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

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final CustomerRepository customerRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(propagation = Propagation.REQUIRED)
    public void increaseQuantity(@NonNull UUID cartId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductDontExistsException, CartDontExistsException, UnableToChangeValueException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        if (quantity <= 0 ||
                cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId) > quantity
        )
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.updateQuantityByCartIdAndProductId(cartId, productId, quantity) == 0)
            throw new UnableToChangeValueException();

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NonNull UUID cartId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductDontExistsException, CartDontExistsException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        if (quantity < 0 ||
                cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId) < quantity
        )
            throw new ArgumentValueNotValidException();

        if (quantity == 0) {
            removeProduct(cartId, productId);
            return;
        }
        cartItemRepository.updateQuantityByCartIdAndProductId(cartId, productId, quantity);
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProduct(@NonNull UUID cartId, @NonNull UUID productId) throws ProductDontExistsException, CartDontExistsException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        cartItemRepository.deleteByCartIdAndProductId(cartId, productId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CartItem addProduct(@NonNull UUID cartId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductAlreadyPresentException, ProductQuantityNotAvailableException, CartDontExistsException {

        if (quantity < 0)
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductAlreadyPresentException();

        CartItem item = new CartItem();
        item.setProduct(productRepository.getReferenceById(productId));
        item.setCart(cartRepository.getReferenceById(cartId));
        item.setQuantity(quantity);

        return cartItemRepository.save(item);

    }

    private void checkValues(UUID cartId, UUID productId) throws ProductDontExistsException, CartDontExistsException {
        if (!cartRepository.existsById(cartId))
            throw new CartDontExistsException();

        if (!productRepository.existsById(productId))
            throw new ProductDontExistsException();
    }

    // TODO estrarre utente dal token e ottenere cart
    private Cart getUserCart(){
        return null;
    }

}
