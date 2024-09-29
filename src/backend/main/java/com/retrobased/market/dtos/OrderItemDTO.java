package com.retrobased.market.dtos;

import java.math.BigDecimal;

public record OrderItemDTO(

        ProductDTO product,

        BigDecimal price,

        Long quantity
) {
}
