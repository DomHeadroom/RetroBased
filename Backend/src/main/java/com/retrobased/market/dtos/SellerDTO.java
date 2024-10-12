package com.retrobased.market.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.UUID;

public record SellerDTO(

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank(message = "First name cannot be empty.")
        String firstName,

        @NotBlank(message = "Last name cannot be empty.")
        String lastName,

        @NotBlank(message = "E-mail cannot be empty.")
        @Email
        String email,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        LocalDateTime registeredAt

) {
}
