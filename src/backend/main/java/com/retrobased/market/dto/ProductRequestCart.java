package com.retrobased.market.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ProductRequestCart(
        @NotEmpty List<@Valid ProductQuantityDTO> products
) {}