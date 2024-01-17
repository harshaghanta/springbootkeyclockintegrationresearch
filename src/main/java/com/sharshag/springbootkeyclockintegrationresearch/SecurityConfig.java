package com.sharshag.springbootkeyclockintegrationresearch;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor

public class SecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(requests -> requests
                        .anyRequest().authenticated());

        http.oauth2ResourceServer(server -> server.jwt().jwtAuthenticationConverter(jwtAuthConverter));

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();

    }
}
