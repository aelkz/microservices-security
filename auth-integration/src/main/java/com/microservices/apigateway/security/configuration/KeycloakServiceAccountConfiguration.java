package com.microservices.apigateway.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakServiceAccountConfiguration {

    @Value("${keycloak-service-account.auth-server-uri}")
    private String authServerUri;

    @Value("${keycloak-service-account.realm}")
    private String realm;

    @Value("${keycloak-service-account.client-id}")
    private String clientId;

    @Value("${keycloak-service-account.secret}")
    private String secret;

    @Value("${keycloak-service-account.grant-type}")
    private String grantType;

    @Value("${keycloak-service-account.username}")
    private String username;

    @Value("${keycloak-service-account.password}")
    private String password;

    public String getAuthServerUri() {
        return authServerUri;
    }

    public void setAuthServerUri(String authServerUri) {
        this.authServerUri = authServerUri;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getGrantType() {
        return grantType;
    }

    public void setGrantType(String grantType) {
        this.grantType = grantType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
