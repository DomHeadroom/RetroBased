package com.retrobased.market.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record ProductRequestOrderDTO(
        @NotEmpty List<@Valid ProductQuantityDTO> products,
        @NotNull @JsonProperty("address") UUID addressId
) {
}