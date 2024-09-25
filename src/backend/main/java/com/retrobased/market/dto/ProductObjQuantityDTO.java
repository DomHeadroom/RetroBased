package com.retrobased.market.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ProductObjQuantityDTO(
        @NotNull ProductDTO product,
        @NotNull @Min(1) Long quantity
) {
}