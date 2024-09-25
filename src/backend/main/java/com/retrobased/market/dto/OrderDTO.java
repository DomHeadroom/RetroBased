package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.OffsetDateTime;

public record OrderDTO(

        CustomerAddressDTO address,

        OffsetDateTime orderApprovedAt,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        OffsetDateTime orderDeliveredCarrierDate,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        OffsetDateTime orderDeliveredCustomerDate,

        OffsetDateTime createdAt
) {
}
