package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.repositories.CartRepository;
import com.retrobased.market.repositories.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CustomerRepository customerRepository;

    public CartService(CustomerRepository customerRepository, CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cart getCustomerCart(UUID customerId) {
        if (cartRepository.existsByCustomerId(customerId))
            return cartRepository.getCartByCustomerId(customerId);

        Cart customerCart = new Cart();
        customerCart.setCustomer(customerRepository.getReferenceById(customerId));

        return cartRepository.save(customerCart);

    }


}
