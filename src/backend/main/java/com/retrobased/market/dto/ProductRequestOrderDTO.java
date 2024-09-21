package com.retrobased.market.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ProductRequestOrderDTO(
        @NotEmpty List<@Valid ProductQuantityDTO> products,
        @NotNull UUID addressId
) {}