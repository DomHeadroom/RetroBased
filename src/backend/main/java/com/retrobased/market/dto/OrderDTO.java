package com.retrobased.market.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;
import java.util.UUID;

public record OrderDTO(
        @JsonProperty("id")
        UUID orderId,

        CustomerAddressDTO address,

        OffsetDateTime orderApprovedAt,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        OffsetDateTime orderDeliveredCarrierDate,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        OffsetDateTime orderDeliveredCustomerDate,

        OffsetDateTime createdAt
) {
}
