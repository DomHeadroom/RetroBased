package com.retrobased.market.dto;

import com.retrobased.market.entities.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SellerDTO(

        @NotBlank(message = "This field cannot be empty.")
        String sellerName,

        String company,

        @NotBlank
        String phoneNumber,

        @NotBlank(message = "This field cannot be empty.")
        String addressLine1,

        String addressLine2,

        @NotNull(message = "This field cannot be null.")
        Country country,

        String city,

        String note

) {
}
