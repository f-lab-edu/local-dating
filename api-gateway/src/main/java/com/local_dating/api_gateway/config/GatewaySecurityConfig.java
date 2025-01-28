package com.local_dating.api_gateway.config;

import com.local_dating.api_gateway.util.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public GatewaySecurityConfig(final JwtAuthorizationFilter jwtAuthorizationFilter) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeExchange((requests) -> requests
                        .pathMatchers("/", "/v1/users/login", "/v1/users/register, /test/**").permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterAt(jwtAuthorizationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .logout((logout) -> logout.permitAll()).build();

    }

}
