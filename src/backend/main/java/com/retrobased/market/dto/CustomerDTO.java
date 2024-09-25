package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.time.OffsetDateTime;

public record CustomerDTO(

        @NotBlank(message = "First name cannot be empty.")
        String firstName,

        @NotBlank(message = "Last name cannot be empty.")
        String lastName,

        @NotBlank(message = "E-mail cannot be empty.")
        String email,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        OffsetDateTime registeredAt

) {
}
