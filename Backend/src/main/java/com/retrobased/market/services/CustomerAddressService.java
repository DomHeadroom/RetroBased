package com.retrobased.market.services;

import com.retrobased.market.dtos.CustomerAddressDTO;
import com.retrobased.market.entities.Country;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.mappers.CustomerAddressMapper;
import com.retrobased.market.repositories.CustomerAddressRepository;
import com.retrobased.market.support.exceptions.AddressNotFoundException;
import com.retrobased.market.support.exceptions.CountryNotFoundException;
import com.retrobased.market.support.exceptions.CustomerNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

    private final CustomerService customerService;
    private final CountryService countryService;

    public CustomerAddressService(
            CustomerAddressRepository customerAddressRepository,
            CustomerService customerService,
            CountryService countryService
    ) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerService = customerService;
        this.countryService = countryService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressDTO addAddress(@NotNull UUID customerId, @NotNull CustomerAddressDTO addressDTO) throws CustomerNotFoundException, CountryNotFoundException {
        Customer customer = customerService.get(customerId)
                .orElseThrow(CustomerNotFoundException::new);

        Country country = countryService.get(addressDTO.country())
                .orElseThrow(CountryNotFoundException::new);

        CustomerAddress newAddress = new CustomerAddress();
        newAddress.setCustomer(customer);
        newAddress.setAddressLine1(addressDTO.addressLine1());
        newAddress.setAddressLine2(addressDTO.addressLine2());
        newAddress.setCountry(country);
        newAddress.setPostalCode(addressDTO.postalCode());
        newAddress.setCity(addressDTO.city());
        newAddress.setDeleted(false);

        CustomerAddress savedAddress = customerAddressRepository.save(newAddress);
        return CustomerAddressMapper.toDTO(savedAddress);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void removeAddress(@NotNull UUID customerId, @NotNull UUID addressId) throws AddressNotFoundException, CustomerNotFoundException {
        if (!customerService.exists(customerId))
            throw new CustomerNotFoundException();

        CustomerAddress address = customerAddressRepository.findByIdAndCustomerIdAndDeletedFalse(addressId, customerId)
                .orElseThrow(AddressNotFoundException::new);

        address.setDeleted(true);
        customerAddressRepository.save(address);

    }

    @Transactional(readOnly = true)
    public CustomerAddress get(@NotNull UUID customerAddressId) {
        return customerAddressRepository.getReferenceById(customerAddressId);
    }

    @Transactional(readOnly = true)
    public Optional<CustomerAddress> getIfExistsAndNotDeleted(UUID customerId, UUID addressId) {
        return customerAddressRepository.findByIdAndCustomerIdAndDeletedFalse(addressId, customerId);
    }
}
