package com.retrobased.market.services;

import com.retrobased.market.entities.CartItem;
import com.retrobased.market.entities.Product;
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
    public void increaseQuantity(@NonNull UUID cartId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductDontExistsException, ValueCannotBeEmptyException, CartDontExistsException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        if (quantity <= 0 ||
                cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId) > quantity
        )
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantityById(productId) < quantity)
            throw new ProductQuantityNotAvailableException();

        cartItemRepository.changeQuantity(cartId, productId, quantity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NonNull UUID cartId, @NonNull UUID productId, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductDontExistsException, ValueCannotBeEmptyException, ClientDontExistsException, CartDontExistsException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        if (quantity < 0
                || cartItemRepository.findQuantityByCartIdAndProductId(cartId, productId) < quantity
        )
            throw new ArgumentValueNotValidException();

        if (quantity == 0) {
            removeProdotto(cartId, productId);
            return;
        }
        cartItemRepository.changeQuantity(idCliente, product.getId(), quantity);
    }

    private void checkValues(UUID cartId, UUID productId) throws ValueCannotBeEmptyException, ProductDontExistsException, CartDontExistsException {
        if (!cartRepository.existsById(cartId))
            throw new CartDontExistsException();

        if (!productRepository.existsById(productId))
            throw new ProductDontExistsException();
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProdotto(@NonNull UUID cartId, @NonNull UUID productId) throws ValueCannotBeEmptyException, ClientDontExistsException, ProductDontExistsException, CartDontExistsException {
        checkValues(cartId, productId);

        if (!cartItemRepository.existsByCartIdAndProductId(cartId, productId))
            throw new ProductDontExistsException();

        cartItemRepository.deleteByCartIdAndProductId(cartId, productId);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CartItem addProducts(@NonNull UUID cartId, @NonNull Product product, @NonNull Long quantity) throws ArgumentValueNotValidException, ProductAlreadyPresentException, ProductQuantityNotAvailableException, CartDontExistsException {

        if (!cartRepository.existsById(cartId))
            throw new CartDontExistsException();

        if (quantity < 0)
            throw new ArgumentValueNotValidException();

        if (productRepository.findQuantitàById(product.getId()).compareTo(quantity) > 0)
            throw new ProductQuantityNotAvailableException();

        if (cartItemRepository.existsByIdClienteAndIdProdotto(idCustomer, product.getId()))
            throw new ProductAlreadyPresentException();

        OggettoCarrello object = new OggettoCarrello();
        object.setIdProdotto(productRepository.getReferenceById(product.getId()));
        object.setIdCliente(customerRepository.getReferenceById(idCustomer));
        object.setQuantità(quantity);

        return cartItemRepository.save(object);

    }


}
