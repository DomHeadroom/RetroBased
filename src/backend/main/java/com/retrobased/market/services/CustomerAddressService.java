package com.retrobased.market.services;

import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.repositories.CustomerAddressRepository;
import com.retrobased.market.support.exceptions.AddressDontExistsException;
import com.retrobased.market.support.exceptions.CustomerNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerService customerService;

    public CustomerAddressService(CustomerAddressRepository customerAddressRepository, CustomerService customerService) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerService = customerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddress addAddress(@NotNull UUID customerId, @NotNull CustomerAddress address) throws CustomerNotFoundException {
        Customer customer = customerService.get(customerId)
                .orElseThrow(CustomerNotFoundException::new);

        CustomerAddress newAddress = new CustomerAddress();
        newAddress.setCustomer(customer);
        newAddress.setCity(address.getCity());
        newAddress.setCountry(address.getCountry());
        newAddress.setPostalCode(address.getPostalCode());
        newAddress.setAddressLine1(address.getAddressLine1());

        return customerAddressRepository.save(newAddress);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(@NotNull UUID customerId, @NotNull UUID addressId) throws AddressDontExistsException, CustomerNotFoundException {
        if (!customerService.exists(customerId))
            throw new CustomerNotFoundException();

        if (!customerAddressRepository.existsById(customerId) ||
                !customerAddressRepository.existsByIdAndCustomerId(customerId, addressId))
            throw new AddressDontExistsException();

        customerAddressRepository.deleteById(addressId);

    }

    @Transactional(readOnly = true)
    public boolean existsAddressForCustomer(@NotNull UUID customerId, @NotNull UUID customerAddressId) {
        return customerAddressRepository.existsByIdAndCustomerId(customerAddressId, customerId);
    }

    @Transactional(readOnly = true)
    public CustomerAddress get(@NotNull UUID customerAddressId) {
        return customerAddressRepository.getReferenceById(customerAddressId);
    }
}
