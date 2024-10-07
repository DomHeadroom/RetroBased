package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.repositories.CartRepository;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(
            CartRepository cartRepository
    ) {
        this.cartRepository = cartRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cart getCustomerCart(String customerId) throws CustomerNotFoundException {
        Optional<Cart> cart = cartRepository.getCartByCustomerId(customerId);

        if (cart.isPresent())
            return cart.get();

        Cart customerCart = new Cart();
        customerCart.setCustomerId(customerId);

        return cartRepository.save(customerCart);

    }

}
