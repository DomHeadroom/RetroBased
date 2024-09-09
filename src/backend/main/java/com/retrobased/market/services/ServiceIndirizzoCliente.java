package com.retrobased.market.services;

import com.retrobased.market.repositories.RepositoryIndirizzoCliente;
import com.retrobased.market.support.exceptions.AddressDontExistsException;
import com.retrobased.market.support.exceptions.ClientTokenMismatchException;
import com.retrobased.market.support.exceptions.ClientDontExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmptyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServiceIndirizzoCliente {

    private final RepositoryIndirizzoCliente addressRepo;

    private final RepositoryCliente userRepo;

    public ServiceIndirizzoCliente(RepositoryIndirizzoCliente addressRepo, RepositoryCliente userRepo) {
        this.addressRepo = addressRepo;
        this.userRepo = userRepo;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public IndirizzoCliente addAddress(@NonNull Long idCliente, @NonNull IndirizzoCliente address) throws ClientDontExistsException, ValueCannotBeEmptyException {
        if (address.getCittà() == null
                || address.getPaese() == null
                || address.getLineaIndirizzo1() == null
                || address.getCodicePostale() == null
                || address.getIdCliente() == null
        )
            throw new ValueCannotBeEmptyException();

        if (!userRepo.existsById(address.getIdCliente().getId()))
            throw new ClientDontExistsException();

        address.setIdCliente(userRepo.getReferenceById(idCliente));

        return addressRepo.save(address);

    }

    // TODO CAMBIARE IDCLIENTE CON TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(@NonNull Long idClient, @NonNull IndirizzoCliente address) throws ClientDontExistsException, ValueCannotBeEmptyException, ClientTokenMismatchException, AddressDontExistsException {
        if (address.getId() == null
                || address.getIdCliente() == null
        )
            throw new ValueCannotBeEmptyException();

        if (!idClient.equals(address.getIdCliente().getId()))
            throw new ClientTokenMismatchException();

        if (!userRepo.existsById(address.getIdCliente().getId()))
            throw new ClientDontExistsException();

        if(!addressRepo.existsIndirizzoClienteByIdAndIdCliente(address.getId(), userRepo.getReferenceById(idClient)))
           throw new AddressDontExistsException();

        userRepo.deleteById(address.getId());

    }
}
