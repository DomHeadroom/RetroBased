package com.retrobased.market.dto;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record ProductQuantityDTO(
        @NotNull UUID productId,
        @NotNull @Min(1) Long quantity
) {}