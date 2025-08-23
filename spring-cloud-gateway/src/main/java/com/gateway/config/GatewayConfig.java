package com.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.addRequestHeader;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

@Configuration
public class GatewayConfig {
    @Bean
    public RouterFunction<ServerResponse> customRoutes() {
        // @formatter:off
        return route("fruits_app_route")
                .route(path("/fruits-app/**"), http())
                .before(uri("http://localhost:8081"))
                .before(rewritePath("/fruits-app/(?<segment>.*)", "/${segment}"))
                .before(addRequestHeader("X-Forwarded-For", "gateway"))
            .build()
                .and(route("student_app_route")
                .route(path("/students-app/**"), http())
                .before(uri("http://localhost:8082"))
                .before(rewritePath("/students-app/(?<segment>.*)", "/${segment}"))
                .before(addRequestHeader("X-Forwarded-For", "gateway"))
            .build());
        // @formatter:on
    }
}
