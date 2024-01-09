package com.custom.security.requestdto;

/**
 * Data Transfer Object (DTO) representing the request for obtaining an access
 * token.
 *
 * This record encapsulates the information required to request an access token,
 * including the user's user name and password.
 */
public record AccessTokenRequestDto(String userName, String password) {

}
