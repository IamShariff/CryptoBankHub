/**
 * Implementation of the {@link KeycloakService} interface that provides operations for managing users in Keycloak.
 * Uses the Keycloak Admin Client for user management and WebClientConfig for making HTTP requests.
 *
 * @author mdsharif
 * @version 1.0
 * @since 2023-01-01
 */
package com.custom.security.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.custom.security.config.KeycloakConfig;
import com.custom.security.config.WebClientConfig;
import com.custom.security.requestdto.AccessTokenRequestDto;
import com.custom.security.requestdto.KeycloakUserRequesDto;
import com.custom.security.requestdto.TokenResponseDto;
import com.custom.security.service.KeycloakService;
import com.custom.security.util.Constants;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the {@link KeycloakService} interface that provides
 * operations for managing users in Keycloak.
 */
@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {

	@Value("${com.keycloak.user.realm}")
	private String userRealm;
	@Value("${com.keycloak.config.clientId}")
	private String clientId;
	@Value("${com.keycloak.config.secret}")
	private String clientSecret;

	private final KeycloakConfig keycloakConfig;
	private final WebClientConfig webClientConfig;

	@Override
	public boolean addKeycloakUser(KeycloakUserRequesDto keycloakUserRequestDto) {
		Keycloak keycloak = keycloakConfig.keycloakAdminClient();
		UsersResource usersResource = keycloak.realm(userRealm).users();
		UserRepresentation createUserRepresentation = createUserRepresentation(keycloakUserRequestDto);
		CredentialRepresentation credentials = setCredentials(keycloakUserRequestDto.getPassword());

		createUserRepresentation.setCredentials(Collections.singletonList(credentials));

		return Optional.of(usersResource.create(createUserRepresentation)).map(response -> {
			if (response.getStatus() == 201) {
				String userId = CreatedResponseUtil.getCreatedId(response);
				UserResource userResource = usersResource.get(userId);
				UserRepresentation updatedUser = userResource.toRepresentation();
				updatedUser.setEnabled(true);
				userResource.update(updatedUser);
				RoleRepresentation userRole = findUserRoleByName(keycloak, "USER");
				userResource.roles().realmLevel().add(Collections.singletonList(userRole));
				return true;
			} else {
				return false;
			}
		}).orElse(false);
	}

	private RoleRepresentation findUserRoleByName(Keycloak keycloak, String roleName) {
		return keycloak.realm(userRealm).roles().get(roleName).toRepresentation();
	}

	@Override
	public Optional<UserRepresentation> getkeycloakUserDetails(String userName) {
		Keycloak keycloak = keycloakConfig.keycloakAdminClient();
		UsersResource usersResource = keycloak.realm(userRealm).users();
		List<UserRepresentation> search = usersResource.search(userName, true);
		if (!search.isEmpty()) {
			return Optional.of(search.get(0));
		} else {
			return Optional.empty();
		}
	}

	private UserRepresentation createUserRepresentation(KeycloakUserRequesDto keycloakUserRequestDto) {
		UserRepresentation newUser = new UserRepresentation();
		newUser.setUsername(keycloakUserRequestDto.getUserEmail());
		newUser.setFirstName(keycloakUserRequestDto.getFirstName());
		newUser.setLastName(keycloakUserRequestDto.getLastName());
		newUser.setEmail(keycloakUserRequestDto.getUserEmail());
		newUser.setEmailVerified(true);
		newUser.setEnabled(true);
		return newUser;
	}

	private CredentialRepresentation setCredentials(String password) {
		CredentialRepresentation credentials = new CredentialRepresentation();
		credentials.setType(CredentialRepresentation.PASSWORD);
		credentials.setValue(password);
		credentials.setTemporary(false);
		return credentials;
	}

	@Override
	public TokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
		MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
		formData.add(Constants.KEY_CLIENT_ID, clientId);
		formData.add(Constants.KEY_CLIENT_SECRET, clientSecret);
		formData.add(Constants.KEY_GRANT_TYPE, "password");
		formData.add(Constants.KEY_USER_NAME, accessTokenRequestDto.userName());
		formData.add(Constants.KEY_PASSWORD, accessTokenRequestDto.password());
		return webClientConfig.post("http://localhost:8080/realms/Security/protocol/openid-connect/token", formData,
				TokenResponseDto.class);
	}

	@Override
	public boolean updateKeycloakUser(String userName, KeycloakUserRequesDto keycloakUserRequestDto) {
		Keycloak keycloak = keycloakConfig.keycloakAdminClient();
		UsersResource usersResource = keycloak.realm(userRealm).users();

		return getkeycloakUserDetails(userName).map(user -> {
			UserRepresentation updatedKeycloakUser = createUserRepresentation(keycloakUserRequestDto);
			CredentialRepresentation credentials = setCredentials(keycloakUserRequestDto.getPassword());
			updatedKeycloakUser.setCredentials(Collections.singletonList(credentials));
			usersResource.get(user.getId()).update(updatedKeycloakUser);
			return true;
		}).orElse(false);
	}

	@Override
	public boolean deleteKeycloakUser(String userName) {
		Keycloak keycloak = keycloakConfig.keycloakAdminClient();
		UsersResource usersResource = keycloak.realm(userRealm).users();

		return getkeycloakUserDetails(userName).map(user -> {
			usersResource.get(user.getId()).remove();
			return true;
		}).orElse(false);
	}

}
