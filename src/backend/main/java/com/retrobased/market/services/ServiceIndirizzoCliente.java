package com.retrobased.market.services;

import com.retrobased.market.entities.IndirizzoCliente;
import com.retrobased.market.repositories.RepositoryCliente;
import com.retrobased.market.repositories.RepositoryIndirizzoCliente;
import com.retrobased.market.support.exceptions.ClientTokenMismatch;
import com.retrobased.market.support.exceptions.ClientNotExist;
import com.retrobased.market.support.exceptions.ValueCannotBeEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceIndirizzoCliente {

    @Autowired
    private RepositoryIndirizzoCliente addressRepo;

    @Autowired
    private RepositoryCliente userRepo;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public IndirizzoCliente addAddress(IndirizzoCliente address) throws ClientNotExist, ValueCannotBeEmpty {
        if (address == null
                || address.getId() == null
                || address.getCittà() == null
                || address.getPaese() == null
                || address.getLineaIndirizzo1() == null
                || address.getCodicePostale() == null
                || address.getIdCliente() == null
        )
            throw new ValueCannotBeEmpty();

        if (userRepo.NotExistById(address.getIdCliente().getId()))
            throw new ClientNotExist();

        return addressRepo.save(address);

    }

    // TODO CAMBIARE IDCLIENTE CON TOKEN
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void removeAddress(Integer idClient, IndirizzoCliente address) throws ClientNotExist, ValueCannotBeEmpty, ClientTokenMismatch {
        if (address == null
                || address.getId() == null
                || address.getIdCliente() == null
        )
            throw new ValueCannotBeEmpty();

        if (!idClient.equals(address.getIdCliente().getId()))
            throw new ClientTokenMismatch();

        if (userRepo.NotExistById(address.getIdCliente().getId()))
            throw new ClientNotExist();


        userRepo.deleteById(address.getId());

    }
}
