package com.custom.security.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing the request for creating or updating
 * a user in Keycloak.
 *
 * This class encapsulates the information required to create or update a user
 * in Keycloak, including the user's first name, last name, email, and password.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeycloakUserRequesDto {

	private String firstName;
	private String lastName;
	private String userEmail;
	private String password;

}
