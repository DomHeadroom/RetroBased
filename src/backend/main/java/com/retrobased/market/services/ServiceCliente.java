package com.retrobased.market.services;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ServiceCliente {
    private final RepositoryCliente userRepo;

    public ServiceCliente(RepositoryCliente userRepo) {
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Cliente registerUser(@NonNull Cliente user) throws MailUserAlreadyExistsException, ValueCannotBeEmpty {
        System.out.println("Dentro register con:" + user);

        if (user.getNome() == null
                || user.getCognome() == null
                || user.getEmail() == null
                || user.getHashPassword() == null
        )
            throw new ValueCannotBeEmpty();

        user.setId(null);

        System.out.println("Dio");

        if (userRepo.existsByEmail(user.getEmail())) {
            throw new MailUserAlreadyExistsException();
        }

        System.out.println("Saving user: " + user);
        Cliente savedUser = userRepo.save(user);
        System.out.println("Saved user: " + savedUser);

        return userRepo.save(savedUser);
    }


}
