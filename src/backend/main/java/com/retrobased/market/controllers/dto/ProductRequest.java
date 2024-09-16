package com.retrobased.market.controllers.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotEmpty
    private List<ProductQuantityDTO> products;

    @NotEmpty
    private UUID addressId;
}