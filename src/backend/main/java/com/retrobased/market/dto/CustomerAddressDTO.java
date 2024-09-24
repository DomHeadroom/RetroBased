package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CustomerAddressDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank(message = "This field cannot be empty.")
        String addressLine1,

        String addressLine2,

        @NotBlank(message = "This field cannot be empty.")
        String country,

        @NotBlank(message = "This field cannot be empty.")
        String postalCode,

        @NotBlank(message = "This field cannot be empty.")
        String city

) {
}
