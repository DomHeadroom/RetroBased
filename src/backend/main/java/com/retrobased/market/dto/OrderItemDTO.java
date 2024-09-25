package com.retrobased.market.dto;

import java.math.BigDecimal;

public record OrderItemDTO(

        ProductDTO product,

        BigDecimal price,

        Long quantity
) {
}
