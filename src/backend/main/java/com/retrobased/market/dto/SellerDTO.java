package com.retrobased.market.dto;

import com.retrobased.market.entities.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SellerDTO(

        @NotBlank(message = "Seller name cannot be empty.")
        String sellerName,

        String company,

        @NotBlank
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
