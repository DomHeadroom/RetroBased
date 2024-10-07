package com.retrobased.market.authentications;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationService {

    /**
     * Extracts a specific claim (Keycloak user ID) from the JWT token present in the SecurityContext.
     *
     * @return Optional of Keycloak user ID (String)
     */
    public Optional<String> extractUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated())
            return Optional.empty();

        if (authentication.getPrincipal() instanceof Jwt jwt) {
            String customerId = jwt.getClaimAsString("sub");
            return Optional.ofNullable(customerId);
        }

        return Optional.empty();
    }


}

