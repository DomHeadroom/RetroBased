package com.retrobased.market.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ProductRequestCartDTO(
        @NotEmpty List<@Valid ProductQuantityDTO> products
) {
}