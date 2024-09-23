package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductDTO(
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        UUID id,

        @NotBlank(message = "Slug cannot be empty.")
        String slug,

        @NotBlank(message = "Product name cannot be empty.")
        String productName,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String sku,

        @NotNull(message = "Sale price is required.")
        @Min(0L)
        BigDecimal salePrice,

        @NotNull(message = "Quantity is required.")
        @Min(1L)
        Long quantity,

        @NotBlank(message = "Short description cannot be empty.")
        String shortDescription,

        @NotBlank(message = "Product description cannot be empty.")
        String productDescription,

        @JsonInclude(value = JsonInclude.Include.NON_DEFAULT)
        Boolean disableOutOfStock,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        String note,

        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        OffsetDateTime createdAt
) {
}
