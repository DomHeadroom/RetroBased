package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.support.exceptions.UserMailAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmptyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {
    private final CustomerRepository userRepo;

    public CustomerService(CustomerRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerUser(@NonNull Customer user) throws UserMailAlreadyExistsException, ValueCannotBeEmptyException {

        if (user.getFirstName() == null ||
                user.getLastName() == null ||
                user.getEmail() == null
        )
            throw new ValueCannotBeEmptyException();

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new UserMailAlreadyExistsException();
        }

        user.setId(null);
        user.setRegisteredAt(null);

        Customer savedUser = userRepo.save(user);
        System.out.println("Saved user: " + savedUser);
    }


}
