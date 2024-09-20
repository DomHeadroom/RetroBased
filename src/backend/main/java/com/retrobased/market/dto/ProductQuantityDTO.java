package com.retrobased.market.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductQuantityDTO {

    @NotNull
    private UUID productId;

    @NotNull
    @Min(1)
    private Long quantity;
}
