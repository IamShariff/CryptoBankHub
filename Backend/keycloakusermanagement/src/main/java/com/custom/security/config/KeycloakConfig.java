package com.custom.security.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

	@Value("${com.keycloak.server.url}")
	private String keycloakServerUrl;

	@Value("${com.keycloak.master.realm}")
	private String keycloakMasterRealm;

	@Value("${com.keycloak.master.clientId}")
	private String keycloakMasterClientId;

	@Value("${com.keycloak.admin.username}")
	private String adminUsername;

	@Value("${com.keycloak.admin.password}")
	private String adminPassword;

	@Value("${com.keycloak.user.realm}")
	private String userRealm;

	@Bean
	public Keycloak keycloakAdminClient() {
		return KeycloakBuilder.builder().serverUrl(keycloakServerUrl).realm(keycloakMasterRealm)
				.clientId(keycloakMasterClientId).grantType(OAuth2Constants.PASSWORD).username(adminUsername)
				.password(adminPassword).build();
	}

}
