package com.retrobased.market.services;

import com.retrobased.market.entities.CartItems;
import com.retrobased.market.entities.Products;
import com.retrobased.market.repositories.*;
import com.retrobased.market.support.exceptions.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CartItemsService {
    private final CartItemsRepository cartItemsRepository;

    private final CartRepository cartRepository;

    private final ProductsRepository productsRepository;

    private final CustomersRepository customersRepository;

    public CartItemsService(CartItemsRepository cartItemsRepository, CartRepository cartRepository, ProductsRepository productsRepository, CustomersRepository customersRepository) {
        this.cartItemsRepository = cartItemsRepository;
        this.cartRepository = cartRepository;
        this.productsRepository = productsRepository;
        this.customersRepository = customersRepository;
    }

    // TODO CAMBIARE CON OGGETTO CARRELLO E NON ID PRODOTTO
    @Transactional(propagation = Propagation.REQUIRED)
    public void increaseQuantity(@NonNull UUID cartId, @NonNull Products product, @NonNull BigDecimal quantity) throws ArgumentValueNotValidException, ProductQuantityNotAvailableException, ProductDontExistsException, ValueCannotBeEmptyException, ClientDontExistsException {
        checkValues(cartId, product);

        if (!cartItemsRepository.existsByCartIdAndProductId(cartId, product.getId()))
            throw new ProductDontExistsException();

        if (quantity.compareTo(BigDecimal.ZERO) <= 0
                || cartItemsRepository.findQuantitàByIdClienteAndIdProdotto(idCliente, product.getId()).compareTo(quantity) > 0
        )
            throw new ArgumentValueNotValidException();

        if (productsRepository.findQuantitàById(product.getId()).compareTo(quantity) < 0)
            throw new ProductQuantityNotAvailableException();

        cartItemsRepository.changeQuantity(idCliente, product.getId(), quantity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void decreaseQuantity(@NonNull Long idCliente, @NonNull Prodotto product, @NonNull BigDecimal quantity) throws ArgumentValueNotValidException, ProductDontExistsException, ValueCannotBeEmptyException, ClientDontExistsException {
        checkValues(idCliente, product);

        if (!cartItemsRepository.existsByIdClienteAndIdProdotto(idCliente, product.getId()))
            throw new ProductDontExistsException();

        if (quantity.compareTo(BigDecimal.ZERO) < 0
                || cartItemsRepository.findQuantitàByIdClienteAndIdProdotto(idCliente, product.getId()).compareTo(quantity) < 0
        )
            throw new ArgumentValueNotValidException();

        if (quantity.compareTo(BigDecimal.ZERO) == 0) {
            removeProdotto(idCliente, product);
            return;
        }
        cartItemsRepository.changeQuantity(idCliente, product.getId(), quantity);
    }

    private void checkValues(UUID cartId, Products product) throws ValueCannotBeEmptyException, ProductDontExistsException, CartDontExistsException {
        if (product.getId() == null)
            throw new ValueCannotBeEmptyException();

        if (!cartRepository.existsById(cartId))
            throw new CartDontExistsException();

        if (!productsRepository.existsById(product.getId()))
            throw new ProductDontExistsException();
    }

    // TODO IDCLIENTE PRESO DAL TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeProdotto(@NonNull UUID customerId, @NonNull Products product) throws ValueCannotBeEmptyException, ClientDontExistsException, ProductDontExistsException {
        checkValues(customerId, product);

        if (!cartItemsRepository.existsByIdClienteAndIdProdotto(customerId, product.getId()))
            throw new ProductDontExistsException();

        cartItemsRepository.deleteByIdClienteAndIdProdotto(customerId, product.getId());

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CartItems addProducts(@NonNull Long idCustomer, @NonNull Products products, @NonNull BigDecimal quantity) throws ValueCannotBeEmptyException, ClientDontExistsException, ClientTokenMismatchException, ArgumentValueNotValidException, ProductDontExistsException, ProductAlreadyPresentException, ProductQuantityNotAvailableException {
        checkValues(idCustomer, products);

        if (quantity.compareTo(BigDecimal.ONE) < 0)
            throw new ArgumentValueNotValidException();

        if (productsRepository.findQuantitàById(product.getId()).compareTo(quantity) > 0)
            throw new ProductQuantityNotAvailableException();

        if (cartItemsRepository.existsByIdClienteAndIdProdotto(idCustomer, product.getId()))
            throw new ProductAlreadyPresentException();

        OggettoCarrello object = new OggettoCarrello();
        object.setIdProdotto(productsRepository.getReferenceById(product.getId()));
        object.setIdCliente(customersRepository.getReferenceById(idCustomer));
        object.setQuantità(quantity);

        return cartItemsRepository.save(object);

    }


}
