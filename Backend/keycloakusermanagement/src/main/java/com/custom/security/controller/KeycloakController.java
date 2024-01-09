package com.custom.security.controller;

import java.util.Optional;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.custom.security.requestdto.AccessTokenRequestDto;
import com.custom.security.requestdto.KeycloakUserRequesDto;
import com.custom.security.requestdto.TokenResponseDto;
import com.custom.security.service.KeycloakService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Controller class for handling Keycloak-related operations.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/keycloak")
@Tag(name = "Keycloak Controller", description = "API's for keycloak related operations")
public class KeycloakController {

    private final KeycloakService keycloakService;

    /**
     * Retrieves the access token for the given request.
     *
     * @param accessTokenRequestDto Request DTO containing information for acquiring the access token
     * @return ResponseEntity containing the access token details
     */
    @PostMapping("/accessToken")
    ResponseEntity<TokenResponseDto> getAccessToken(@RequestBody AccessTokenRequestDto accessTokenRequestDto) {
        return ResponseEntity.ok(keycloakService.getAccessToken(accessTokenRequestDto));
    }

    /**
     * Adds a Keycloak user with the provided information.
     *
     * @param keycloakAddUserRequestDto Request DTO containing information for adding a Keycloak user
     * @return ResponseEntity indicating the success of the user addition
     */
    @PostMapping("/user")
    ResponseEntity<Boolean> addKeycloakUser(@RequestBody KeycloakUserRequesDto keycloakAddUserRequestDto) {
        return ResponseEntity.ok(keycloakService.addKeycloakUser(keycloakAddUserRequestDto));
    }

    /**
     * Retrieves Keycloak user details by username.
     *
     * @param userName Username of the Keycloak user
     * @return ResponseEntity containing an Optional with the user details
     */
    @GetMapping("/user/{userName}")
    public ResponseEntity<Optional<UserRepresentation>> getUserByUserName(@PathVariable String userName) {
        return ResponseEntity.ok(keycloakService.getkeycloakUserDetails(userName));
    }

    /**
     * Updates a Keycloak user with the provided information.
     *
     * @param userName               Username of the Keycloak user to be updated
     * @param keycloakUserRequesDto Request DTO containing information for updating the Keycloak user
     * @return ResponseEntity indicating the success of the user update
     */
    @PutMapping("/user/{userName}")
    ResponseEntity<Boolean> updateKeycloakUser(@PathVariable String userName,
                                               @RequestBody KeycloakUserRequesDto keycloakUserRequesDto) {
        return ResponseEntity.ok(keycloakService.updateKeycloakUser(userName, keycloakUserRequesDto));
    }

    /**
     * Deletes a Keycloak user by username.
     *
     * @param userName Username of the Keycloak user to be deleted
     * @return ResponseEntity indicating the success of the user deletion
     */
    @DeleteMapping("/user/{userName}")
    ResponseEntity<Boolean> deleteKeycloakUser(@PathVariable String userName) {
        return ResponseEntity.ok(keycloakService.deleteKeycloakUser(userName));
    }
}
