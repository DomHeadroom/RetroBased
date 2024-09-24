package com.retrobased.market.dto;

import com.retrobased.market.entities.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record ProductObjQuantityDTO(
        @NotNull ProductDTO product,
        @NotNull @Min(1) Long quantity
) {
}