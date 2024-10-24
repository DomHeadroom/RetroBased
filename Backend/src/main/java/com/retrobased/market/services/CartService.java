package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.repositories.CartRepository;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerService customerService;

    public CartService(
            CartRepository cartRepository,
            CustomerService customerService) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cart getCustomerCart(String keycloakId) throws CustomerNotFoundException {
        Customer customer = customerService.findByKeycloakId(keycloakId);
        Optional<Cart> cart = cartRepository.getCartByCustomerId(customer.getId());

        if (cart.isPresent())
            return cart.get();

        Cart customerCart = new Cart();
        customerCart.setCustomer(customer);

        return cartRepository.save(customerCart);

    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    public Cart findCartByKeycloakIdWithLock(String keycloakId) {
        Customer customer = customerService.findByKeycloakId(keycloakId);
        Optional<Cart> cart = cartRepository.getCartByCustomerId(customer.getId());

        if (cart.isPresent())
            return cart.get();

        Cart customerCart = new Cart();
        customerCart.setCustomer(customer);

        return cartRepository.save(customerCart);
    }

}
