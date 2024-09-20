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

    private final CustomerService customerService;

    public CartService(CartRepository cartRepository, CustomerService customerService) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cart getCustomerCart(UUID customerId) {
        if (cartRepository.existsByCustomerId(customerId))
            return cartRepository.getCartByCustomerId(customerId);

        Cart customerCart = new Cart();
        customerCart.setCustomer(customerService.get(customerId));

        return cartRepository.save(customerCart);

    }

}
