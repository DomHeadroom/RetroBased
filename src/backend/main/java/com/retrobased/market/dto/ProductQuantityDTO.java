package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductQuantityDTO(
        @NotNull @JsonProperty("id") UUID productId,
        @NotNull @Min(1) Long quantity
) {
}