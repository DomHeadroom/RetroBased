package com.retrobased.market.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCategoryDTO(
        @Valid @NotNull ProductDTO product,
        UUID firstCategoryId,
        UUID secondCategoryId,
        UUID attributeId
) {
}