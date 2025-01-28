package com.local_dating.api_gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service", p -> p.path("/v1/users/**")
                        .uri("http://localhost:8081"))
                .route("matching-service", p -> p.path("/test/**")
                        .uri("http://localhost:8082"))
                /*.route("user-service", p -> p.path("/get")
                        .filters(f -> f.addRequestHeader("Hello", "World"))
                        .uri("http://httpbin.org:80"))
                        */
                .build();
        //return builder.routes().build();
    }


}
