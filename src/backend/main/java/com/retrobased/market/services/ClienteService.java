package com.retrobased.market.services;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ClienteService {
    @Autowired
    private RepositoryCliente userRepo;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cliente registerUser(Cliente user) throws MailUserAlreadyExistsException {
        if (userRepo.existsByEmail(user.getEmail()))
            throw new MailUserAlreadyExistsException();

        return userRepo.save(user);
    }



}
