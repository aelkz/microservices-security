package com.microservices.apigateway.security.auth.oauth2;

import java.util.Map;

public class Token {
    private String accessToken;
    private long expiresIn;
    private long refreshExpiresIn;
    private String refreshToken;
    private String tokenType;
    private String idToken;
    private int notBeforePolicy;
    private String sessionState;
    private Map<String, Object> otherClaims;
    private String scope;

    Token(
            String accessToken,
            long expiresIn,
            long refreshExpiresIn,
            String refreshToken,
            String tokenType,
            String idToken,
            int notBeforePolicy,
            String sessionState,
            Map<String, Object> otherClaims,
            String scope) {

        if (accessToken == null) {
            throw new NullPointerException("Access Token is null");
        }
        if (refreshToken == null) {
            throw new NullPointerException("Refresh Token is null");
        }
        if (tokenType == null) {
            throw new NullPointerException("Token Type is null");
        }

        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshExpiresIn = refreshExpiresIn;
        this.refreshToken = refreshToken;
        this.tokenType = tokenType;
        this.idToken = idToken;
        this.notBeforePolicy = notBeforePolicy;
        this.sessionState = sessionState;
        this.otherClaims = otherClaims;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public long getRefreshExpiresIn() {
        return refreshExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getIdToken() {
        return idToken;
    }

    public int getNotBeforePolicy() {
        return notBeforePolicy;
    }

    public String getSessionState() {
        return sessionState;
    }

    public Map<String, Object> getOtherClaims() {
        return otherClaims;
    }

    public String getScope() {
        return scope;
    }
}
