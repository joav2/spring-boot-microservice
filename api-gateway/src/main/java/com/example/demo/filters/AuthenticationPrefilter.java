package com.example.demo.filters;

import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.demo.models.ConnValidationResponse;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationPrefilter extends AbstractGatewayFilterFactory<AuthenticationPrefilter.Config>{
	
	@Autowired
    @Qualifier("excludedUrls")
    List<String> excludedUrls;
	private final WebClient.Builder webClient;
	
	public AuthenticationPrefilter(WebClient.Builder webClient) {
		super(Config.class);	
		this.webClient = webClient;
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		return (exchange, chain) -> {
			ServerHttpRequest request = exchange.getRequest();
			String bearerToken = request.getHeaders().getFirst(TOKEN_HEADER);
			if (isSecured.test(request)) {
				return webClient.build().get()
						.uri("lb://authorization-service/api/v1/validateToken")
						.header(TOKEN_HEADER, bearerToken)
						.retrieve().bodyToMono(ConnValidationResponse.class)
						.map(response -> {
							exchange.getRequest().mutate().header("username", response.username());
							exchange.getRequest().mutate().header("jwt", response.token());
							exchange.getRequest().mutate().header("authorities", response.authorities().stream().map(String::new).reduce("", (a, b) -> a + "," + b));
							
							return exchange;
						}).flatMap(chain::filter).onErrorResume(error -> Mono.error(error));
			}
			
			return chain.filter(exchange);
		};
	}
	
	public Predicate<ServerHttpRequest> isSecured = request -> excludedUrls.stream().noneMatch(uri -> request.getURI().getPath().contains(uri));
	
	public static class Config {
		
	}
	
	public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
}
