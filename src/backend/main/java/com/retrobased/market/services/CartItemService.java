package com.retrobased.market.services;

import com.retrobased.market.controllers.dto.ProductQuantityDTO;
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
import java.util.HashMap;
import java.util.LinkedList;
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
    public List<CartItem> addProductToCart(@NotNull UUID customerId, @NotNull List<ProductQuantityDTO> productQuantities) throws ArgumentValueNotValidException, ProductAlreadyPresentException, ProductQuantityNotAvailableException, CustomerDontExistsException, ProductNotFoundException {

        HashMap<UUID, Long> productIds = new HashMap<>();

        for (ProductQuantityDTO productQuantity: productQuantities) {
            if (!productRepository.existsById(productQuantity.getProductId()))
                throw new ProductNotFoundException();

            productIds.merge(productQuantity.getProductId(), productQuantity.getQuantity(), Long::sum);
        }

        List<Product> products = productRepository.findByIdIn(productIds.keySet());


        Cart cart = getCustomerCart(customerId);

        List<CartItem> items = new LinkedList<>();

        for (Product product : products) {
            if (cartItemRepository.existsByCartIdAndProductId(cart.getId(), product.getId())) {
                productIds.merge(product.getId(),cartItemRepository.getQuantityByCartIdAndProductId(cart.getId(), product.getId()), Long::sum);
            }
            if (product.getQuantity() < productIds.get(product.getId()))
                throw new ArgumentValueNotValidException();

            // TODO nel caso esista prodotto e carrello già cambiare il valore e non creare un cartItem nuovo
            CartItem item = new CartItem();
            item.setProduct(productRepository.getReferenceById(product.getId()));
            item.setCart(cart);
            item.setQuantity(productIds.get(product.getId()));

            items.add(item);
        }

        return cartItemRepository.saveAll(items);

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
