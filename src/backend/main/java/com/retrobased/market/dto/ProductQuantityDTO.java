package com.retrobased.market.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;

public record ProductQuantityDTO(
        @NotNull @JsonProperty("id") UUID productId,
        @NotNull @Min(1) Long quantity
) {
}