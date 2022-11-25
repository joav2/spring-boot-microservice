package com.example.demo.configs;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.filters.AuthenticationPrefilter;

@Configuration
public class RouteConfiguration {

	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, AuthenticationPrefilter authPrefilter) {
		return builder.routes()
				.route(p -> p
						.path("/authorization-service/**")
						.filters(f ->
							f.rewritePath("/authorization-service(?<segment>/?.*)", "$\\{segment}")
								.filter(authPrefilter.apply(new AuthenticationPrefilter.Config())))
						.uri("lb://authorization-service"))
				.route(p -> p
						.path("/user-service/**")
						.filters(f ->
                        	f.rewritePath("/user-service(?<segment>/?.*)", "$\\{segment}")
                        		.filter(authPrefilter.apply(new AuthenticationPrefilter.Config())))
						.uri("lb://user-service"))
				.build();
	}
}
