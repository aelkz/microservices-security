package com.microservices.security.common.oauth2;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

public class OAuth2ClientCredentials {
    private Keycloak keycloak;

    public OAuth2ClientCredentials(
            String serverUrl,
            String realm,
            String clientId,
            String clientSecret,
            String grantType,
            String username,
            String password) {

        keycloak = KeycloakBuilder.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(grantType)
                .realm(realm)
                .serverUrl(serverUrl)
                .username(username)
                .password(password)
                .build();
    }

    public Token getToken() {
        AccessTokenResponse accessToken = keycloak.tokenManager().getAccessToken();
        return new Token(
                accessToken.getToken(),
                accessToken.getExpiresIn(),
                accessToken.getRefreshExpiresIn(),
                accessToken.getRefreshToken(),
                accessToken.getTokenType(),
                accessToken.getIdToken(),
                accessToken.getNotBeforePolicy(),
                accessToken.getSessionState(),
                accessToken.getOtherClaims(),
                accessToken.getScope());
    }
}
