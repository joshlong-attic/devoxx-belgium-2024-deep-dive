package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	RouteLocator gateway(RouteLocatorBuilder rlb) {
		return rlb
				.routes()
				.route(rs -> rs
						.path("/api/**")
						.filters(f -> f
								.tokenRelay()
								.rewritePath("/api/(?<segment>.*)", "/$\\{segment}")
						)
						.uri("http://localhost:8080"))
				.route(rs -> rs
						.path("/**")
						.uri("http://localhost:8020")
				)
				.build();
	}
}

