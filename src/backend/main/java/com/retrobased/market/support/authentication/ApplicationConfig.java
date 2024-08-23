package com.retrobased.market.support.authentication;

import com.retrobased.market.repositories.RepositoryCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final RepositoryCliente repoCliente;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails) repoCliente.findByEmail(username);
    }
}
