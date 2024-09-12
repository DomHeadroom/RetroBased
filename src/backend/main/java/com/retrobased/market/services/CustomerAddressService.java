package com.retrobased.market.services;

import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.repositories.CustomerAddressRepository;
import com.retrobased.market.repositories.CustomerRepository;
import com.retrobased.market.support.exceptions.AddressDontExistsException;
import com.retrobased.market.support.exceptions.CustomerDontExistsException;
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
    public CustomerAddress addAddress(@NonNull UUID customerId, @NonNull CustomerAddress address) throws ValueCannotBeEmptyException, CustomerDontExistsException {
        if (address.getAddressLine1() == null ||
                address.getCity() == null ||
                address.getCountry() == null ||
                address.getPostalCode() == null
        )
            throw new ValueCannotBeEmptyException();

        if (!customerRepository.existsById(customerId))
            throw new CustomerDontExistsException();

        CustomerAddress newAddress = new CustomerAddress();
        newAddress.setCustomer(customerRepository.getReferenceById(customerId));
        newAddress.setCity(address.getCity());
        newAddress.setCountry(address.getCountry());
        newAddress.setPostalCode(address.getPostalCode());
        newAddress.setAddressLine1(address.getAddressLine1());

        return customerAddressRepository.save(newAddress);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(@NonNull UUID customerId, @NonNull UUID addressId) throws AddressDontExistsException, CustomerDontExistsException {
        if (!customerRepository.existsById(customerId))
            throw new CustomerDontExistsException();

        if (!customerAddressRepository.existsById(customerId) ||
                !customerAddressRepository.existsByIdAndCustomerId(customerId, addressId))
            throw new AddressDontExistsException();

        customerRepository.deleteById(addressId);

    }
}
