package com.retrobased.market.support.exceptions;

import java.time.LocalDateTime;

public record ApiError(
        String path,
        String messagge,
        int statusCode,
        LocalDateTime localDateTime
) {
}
