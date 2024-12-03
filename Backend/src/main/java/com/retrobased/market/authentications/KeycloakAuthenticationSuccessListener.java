package com.retrobased.market.authentications;

import com.retrobased.market.services.KeycloakService;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class KeycloakAuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final KeycloakService keycloakService;

    public KeycloakAuthenticationSuccessListener(KeycloakService keycloakService) {
        this.keycloakService = keycloakService;
    }

    @SneakyThrows
    @Override
    public void onApplicationEvent(@NonNull AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof JwtAuthenticationToken jwtToken) {
            Map<String, Object> claims = jwtToken.getTokenAttributes();
            keycloakService.saveUserFromKeycloak(claims);
        }
    }
}

