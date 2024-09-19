package com.retrobased.market.dto;

import com.retrobased.market.entities.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductObjQuantityDTO {

    @NotNull
    private Product product;

    @NotNull
    @Min(1)
    private Long quantity;
}
