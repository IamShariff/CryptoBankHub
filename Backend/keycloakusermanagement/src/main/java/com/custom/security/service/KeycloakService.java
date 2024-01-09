package com.custom.security.service;

import java.util.Optional;
import org.keycloak.representations.idm.UserRepresentation;
import com.custom.security.requestdto.AccessTokenRequestDto;
import com.custom.security.requestdto.KeycloakUserRequesDto;
import com.custom.security.requestdto.TokenResponseDto;

/**
 * Service interface for managing users in Keycloak.
 */
public interface KeycloakService {

	/**
	 * Adds a new user to Keycloak with the specified details.
	 *
	 * @param keycloakUserRequestDto Details of the user to be added.
	 * @return True if the user is successfully added; false otherwise.
	 */
	boolean addKeycloakUser(KeycloakUserRequesDto keycloakUserRequestDto);

	/**
	 * Retrieves details of a user from Keycloak based on the username.
	 *
	 * @param userName The username of the user.
	 * @return Optional containing the UserRepresentation if found; otherwise, an
	 *         empty Optional.
	 */
	Optional<UserRepresentation> getkeycloakUserDetails(String userName);

	/**
	 * Updates the details of an existing user in Keycloak.
	 *
	 * @param userName               The username of the user to be updated.
	 * @param keycloakUserRequestDto Updated details for the user.
	 * @return True if the user is successfully updated; false otherwise.
	 */
	boolean updateKeycloakUser(String userName, KeycloakUserRequesDto keycloakUserRequestDto);

	/**
	 * Retrieves an access token from Keycloak using the provided credentials.
	 *
	 * @param accessTokenRequestDto The credentials for obtaining the access token.
	 * @return TokenResponseDto containing the access token information.
	 */
	TokenResponseDto getAccessToken(AccessTokenRequestDto accessTokenRequestDto);

	/**
	 * Deletes a user from Keycloak based on the username.
	 *
	 * @param userName The username of the user to be deleted.
	 * @return True if the user is successfully deleted; false otherwise.
	 */
	boolean deleteKeycloakUser(String userName);
}
