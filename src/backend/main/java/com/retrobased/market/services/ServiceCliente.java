package com.retrobased.market.services;

import com.retrobased.market.entities.Cliente;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.support.exceptions.ClientNotExist;
import com.retrobased.market.support.exceptions.MailUserAlreadyExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ServiceCliente {
    @Autowired
    private RepositoryCliente userRepo;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Cliente registerUser(Cliente user) throws MailUserAlreadyExistsException, ValueCannotBeEmpty {
        if (user == null)
            throw new ValueCannotBeEmpty();

        if (user.getId() == null
                || user.getNome() == null
                || user.getCognome() == null
                || user.getEmail() == null
                || user.getHashPassword() == null
        )
            throw new ValueCannotBeEmpty();

        if (userRepo.existsByEmail(user.getEmail()))
            throw new MailUserAlreadyExistsException();

        return userRepo.save(user);
    }

    public void addAddress(Integer idCliente, String città, String paese, String lineaIndirizzo1, String lineaIndirizzo2, String codicePostale) throws ClientNotExist, ValueCannotBeEmpty {
        if (!userRepo.existById(idCliente))
            throw new ClientNotExist();

        if (idCliente == null
                || città == null
                || paese == null
                || lineaIndirizzo1 == null
                || codicePostale == null
        )
            throw new ValueCannotBeEmpty();


    }


}
