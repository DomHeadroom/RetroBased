package com.retrobased.market.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ProductDTO(
        UUID id,

        @NotBlank(message = "Slug cannot be empty.")
        String slug,

        @NotBlank(message = "Product name cannot be empty.")
        String productName,

        String sku,

        @NotNull(message = "Sale price is required.")
        @Min(0L)
        BigDecimal salePrice,

        BigDecimal comparePrice,

        BigDecimal buyingPrice,

        @NotNull(message = "Quantity is required.")
        @Min(0L)
        Long quantity,

        @NotBlank(message = "Short description cannot be empty.")
        String shortDescription,

        @NotBlank(message = "Product description cannot be empty.")
        String productDescription,

        @NotBlank(message = "Product type cannot be empty.")
        String productType,

        Boolean disableOutOfStock,

        String note,

        OffsetDateTime createdAt
) {
}
