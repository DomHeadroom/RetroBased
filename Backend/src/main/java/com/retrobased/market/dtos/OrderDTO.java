package com.retrobased.market.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.UUID;

public record OrderDTO(
        @JsonProperty("id")
        UUID orderId,

        CustomerAddressDTO address,

        LocalDateTime orderApprovedAt,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime orderDeliveredCarrierDate,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        LocalDateTime orderDeliveredCustomerDate,

        LocalDateTime createdAt
) {
}
