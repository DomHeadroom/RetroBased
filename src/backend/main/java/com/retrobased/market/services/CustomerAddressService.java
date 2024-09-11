package com.retrobased.market.services;

import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.repositories.CustomerAddressRepository;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.repositories.RepositoryIndirizzoCliente;
import com.retrobased.market.support.exceptions.AddressDontExistsException;
import com.retrobased.market.support.exceptions.ClientTokenMismatchException;
import com.retrobased.market.support.exceptions.ClientDontExistsException;
import com.retrobased.market.support.exceptions.ValueCannotBeEmptyException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerRepository customerRepository;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository, CustomerRepository customerRepository) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddress addAddress(@NonNull UUID clientId, @NonNull CustomerAddress address) throws ValueCannotBeEmptyException {
        if (address.getAddressLine1() == null ||
                address.getCity() == null ||
                address.getCountry() == null ||
                address.getPostalCode() == null
        )
            throw new ValueCannotBeEmptyException();

        /* TODO aggiungere customer preso dal token
        address.setCustomer(customerRepository.getReferenceById());
         */

        CustomerAddress newAddress = new CustomerAddress();
        /* TODO prendere customer da token
        customerAddress.setCustomer();
        */

        newAddress.setCity(address.getCity());
        newAddress.setCountry(address.getCountry());
        newAddress.setPostalCode(address.getPostalCode());
        newAddress.setAddressLine1(address.getAddressLine1());

        return customerAddressRepository.save(newAddress);

    }

    // TODO CAMBIARE IDCLIENTE CON TOKEN
    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(@NonNull UUID clientId, @NonNull UUID addressId) throws AddressDontExistsException {
        // TODO estrarre user da token

        if (!customerAddressRepository.existsByIdAndCustomerId(clientId,addressId))
            throw new AddressDontExistsException();

        customerRepository.deleteById(addressId);

    }
}
