package com.custom.security.requestdto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data transfer object representing the response of an access token request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn;

    @JsonProperty("refresh_expires_in")
    private int refreshExpiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("not-before-policy")
    private int notBeforePolicy;

    @JsonProperty("session_state")
    private String sessionState;

    private String scope;
}
