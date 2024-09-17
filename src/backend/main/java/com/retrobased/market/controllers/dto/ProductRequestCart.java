package com.retrobased.market.controllers.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductRequestCart {

    @NotEmpty
    private List<@Valid ProductQuantityDTO> products;

}