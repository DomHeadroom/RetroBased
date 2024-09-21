package com.retrobased.market.dto;

import com.retrobased.market.entities.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCategoryDTO(
        @Valid @NotNull Product product,
        UUID firstCategoryId,
        UUID secondCategoryId,
        UUID attributeId
) {}