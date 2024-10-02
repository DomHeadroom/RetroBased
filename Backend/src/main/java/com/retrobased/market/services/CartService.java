package com.retrobased.market.services;

import com.retrobased.market.entities.Cart;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.repositories.CartRepository;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final CustomerService customerService;

    public CartService(
            CartRepository cartRepository,
            CustomerService customerService
    ) {
        this.cartRepository = cartRepository;
        this.customerService = customerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cart getCustomerCart(UUID customerId) throws CustomerNotFoundException {
        Customer customer = customerService.get(customerId)
                .orElseThrow(CustomerNotFoundException::new);
        Optional<Cart> cart = cartRepository.getCartByCustomerId(customer.getId());

        if (cart.isPresent())
            return cart.get();

        Cart customerCart = new Cart();
        customerCart.setCustomer(customer);

        return cartRepository.save(customerCart);

    }

}
