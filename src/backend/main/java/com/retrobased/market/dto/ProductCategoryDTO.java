package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductCategoryDTO(
        @Valid @NotNull ProductDTO product,
        @JsonProperty("firstCategory")  UUID firstCategoryId,
        @JsonProperty("secondCategory") UUID secondCategoryId,
        @JsonProperty("attribute") UUID attributeId,
        @JsonProperty("tag") UUID tagId
) {
}