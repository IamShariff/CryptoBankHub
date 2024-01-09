package com.cbh.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.web.server.SecurityWebFilterChain;
import lombok.RequiredArgsConstructor;

/**
 * Configuration class for defining security settings in the application.
 */
@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity serverHttpSecurity) {
		serverHttpSecurity.csrf(t -> t.disable());
		serverHttpSecurity.cors(t -> t.disable())
				.authorizeExchange(exchange -> exchange.pathMatchers("/eureka/**", "/user/login", "/keycloak/accessToken","/user").permitAll()
						.pathMatchers(SWAGGER_PATH_ALLOWED).permitAll().anyExchange().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.jwtDecoder(reactiveJwtDecoder())));
		return serverHttpSecurity.build();
	}

	@Bean
	public ReactiveJwtDecoder reactiveJwtDecoder() {
		return ReactiveJwtDecoders.fromOidcIssuerLocation("http://localhost:8080/realms/Security");
	}

	/**
	 * Array of Swagger paths allowed without authentication.
	 */
	protected static final String[] SWAGGER_PATH_ALLOWED = { "/api/v1/auth/**", "/v2/api-docs", "/v3/api-docs",
			"/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**", "/configuration/ui",
			"/configuration/security", "/swebjars/**", "swagger-ui/index.html", "swagger-ui/**", "/swagger-ui.html",
			"/v3/api-docs.yml" };
}