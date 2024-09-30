package com.retrobased.market.mappers;

import com.retrobased.market.dtos.CustomerAddressDTO;
import com.retrobased.market.entities.CustomerAddress;

public class CustomerAddressMapper {
    public static CustomerAddressDTO toDTO(CustomerAddress customerAddress) {
        return new CustomerAddressDTO(
                customerAddress.getId(),
                customerAddress.getAddressLine1(),
                customerAddress.getAddressLine2(),
                customerAddress.getCountry().getId(),
                customerAddress.getPostalCode(),
                customerAddress.getCity()
        );
    }
}
