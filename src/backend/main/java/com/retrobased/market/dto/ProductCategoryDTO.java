package com.retrobased.market.dto;

import com.retrobased.market.entities.Product;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductCategoryDTO {

    @Valid
    private Product product;

    private UUID firstCategoryId;
    private UUID secondCategoryId;

    private UUID attributeId;

}
