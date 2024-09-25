package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CustomerAddressDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank(message = "This field cannot be empty.")
        String addressLine1,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String addressLine2,

        @NotNull(message = "This field cannot be null.")
        Long country,

        @NotBlank(message = "This field cannot be empty.")
        String postalCode,

        @NotBlank(message = "This field cannot be empty.")
        String city

) {
}
