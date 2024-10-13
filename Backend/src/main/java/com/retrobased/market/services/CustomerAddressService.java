package com.retrobased.market.services;

import com.retrobased.market.dtos.CustomerAddressDTO;
import com.retrobased.market.entities.Country;
import com.retrobased.market.entities.Customer;
import com.retrobased.market.entities.CustomerAddress;
import com.retrobased.market.mappers.CustomerAddressMapper;
import com.retrobased.market.repositories.CustomerAddressRepository;
import com.retrobased.market.utils.exceptions.AddressNotFoundException;
import com.retrobased.market.utils.exceptions.CountryNotFoundException;
import com.retrobased.market.utils.exceptions.CustomerNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerAddressService {

    private final CustomerAddressRepository customerAddressRepository;

    private final CountryService countryService;
    private final CustomerService customerService;

    public CustomerAddressService(
            CustomerAddressRepository customerAddressRepository,
            CountryService countryService,
            CustomerService customerService) {
        this.customerAddressRepository = customerAddressRepository;
        this.countryService = countryService;
        this.customerService = customerService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAddressDTO addAddress(@NotEmpty String keycloakId, @NotNull CustomerAddressDTO addressDTO) throws CustomerNotFoundException, CountryNotFoundException {

        Customer customer = customerService.findByKeycloakId(keycloakId);

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
    public void removeAddress(@NotEmpty UUID customerId, @NotNull UUID addressId) throws AddressNotFoundException, CustomerNotFoundException {

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

    @Transactional
    public List<CustomerAddressDTO> getAddresses(UUID customerId) {
        List<CustomerAddress> customerAddress = customerAddressRepository.findByCustomerIdAndDeletedFalse(customerId);

        return customerAddress.stream()
                .map(CustomerAddressMapper::toDTO)
                .collect(Collectors.toList());

    }
}
