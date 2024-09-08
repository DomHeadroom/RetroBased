package com.retrobased.market.services;

import com.retrobased.market.entities.Customers;
import com.retrobased.market.repositories.CustomersRepository;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CustomerService {
    private final CustomersRepository userRepo;

    public CustomerService(CustomersRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void registerUser(@NonNull Customers user) throws MailUserAlreadyExistsException, ValueCannotBeEmpty {

        if (user.getFirstName() == null
                || user.getLastName() == null
                || user.getEmail() == null
        )
            throw new ValueCannotBeEmpty();

        user.setId(null);
        user.setRegisteredAt(null);

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }

        Customers savedUser = userRepo.save(user);
        System.out.println("Saved user: " + savedUser);

        userRepo.save(savedUser);
    }


}
