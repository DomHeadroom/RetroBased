package com.retrobased.market.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerAddressDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank(message = "Address line 1 cannot be empty.")
        String addressLine1,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String addressLine2,

        @NotNull(message = "Country is required.")
        Long country,

        @NotBlank(message = "Postal code cannot be empty.")
        String postalCode,

        @NotBlank(message = "City cannot be empty.")
        String city

) {
}
