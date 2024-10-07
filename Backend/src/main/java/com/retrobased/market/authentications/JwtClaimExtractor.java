package com.retrobased.market.authentications;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class JwtClaimExtractor {

    // TODO questa pure è da deprecare me sa

    /**
     * Extracts a specific claim (customerId) from the JWT token present in the SecurityContext.
     *
     * @return Optional of customerId (UUID)
     */
    public Optional<UUID> extractCustomerId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return Optional.empty();

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            // Extract the 'sub' (subject) or use a custom claim like 'customer_id'
            String customerId = jwt.getClaimAsString("sub"); // Or use a custom claim like 'customer_id'
            return Optional.of(UUID.fromString(customerId));
        }

        return Optional.empty();
    }

}

