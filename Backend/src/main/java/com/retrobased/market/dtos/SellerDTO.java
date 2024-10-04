package com.retrobased.market.dtos;

import com.retrobased.market.entities.Country;
import com.retrobased.market.validators.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SellerDTO(

        @NotBlank(message = "Seller name cannot be empty.")
        String sellerName,

        String company,

        @NotBlank(message = "Phone number cannot be empty.")
        @PhoneNumber
        String phoneNumber,

        @NotBlank(message = "Address line 1 cannot be empty.")
        String addressLine1,

        String addressLine2,

        @NotNull(message = "Country is required.")
        Country country,

        String city,

        String note

) {
}
