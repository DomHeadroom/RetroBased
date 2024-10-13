package com.retrobased.market.authentications;

import com.retrobased.market.services.KeycloakService;
import lombok.SneakyThrows;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
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
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        if (event.getAuthentication() instanceof OAuth2AuthenticationToken authToken) {
            // TODO non entra qui se non è
            Map<String, Object> claims = authToken.getPrincipal().getAttributes();
            keycloakService.saveUserFromKeycloak(claims);
        }
    }
}

