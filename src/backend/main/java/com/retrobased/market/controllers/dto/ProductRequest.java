package com.retrobased.market.controllers.dto;

import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRequest {

    @NotEmpty
    private List<@Valid ProductQuantityDTO> products;

    @NotNull
    private UUID addressId;
}