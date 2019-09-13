package com.microservices.security.common.camel;

import com.microservices.security.common.oauth2.OAuth2ClientCredentials;
import com.microservices.security.common.oauth2.Token;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultProducer;

public class AuthProducer extends DefaultProducer {
    protected AuthEndpoint endpoint;
    protected OAuth2ClientCredentials oAuth2ClientCredentials;

    public AuthProducer(AuthEndpoint endpoint) {
        super(endpoint);
        this.endpoint = endpoint;

        log.info("[serverURL: {}, realm: {}, client_id: {}, client_secret: {}, grant_type: {}, username: {} characters, password: {} characters]",
                this.endpoint.getServerUrl(),
                this.endpoint.getRealm(),
                this.endpoint.getClientID(),
                this.endpoint.getClientSecret(),
                this.endpoint.getGrantType(),
                this.endpoint.getUsername() != null ? this.endpoint.getUsername().length() : 0,
                this.endpoint.getPassword() != null ? this.endpoint.getPassword().length() : 0);

        oAuth2ClientCredentials = new OAuth2ClientCredentials(
                this.endpoint.getServerUrl(),
                this.endpoint.getRealm(),
                this.endpoint.getClientID(),
                this.endpoint.getClientSecret(),
                this.endpoint.getGrantType(),
                this.endpoint.getUsername(),
                this.endpoint.getPassword());
    }

    @SuppressWarnings("WeakerAccess")
    protected Token getToken() {
        return oAuth2ClientCredentials.getToken();
    }

    @SuppressWarnings("unused")
    protected String getBearer() {
        String tokenString = getToken().getAccessToken();
        if (tokenString == null) {
            throw new NullPointerException("Token is null");
        }
        return String.format("Bearer %s", tokenString);
    }

    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setHeader("Authorization", getBearer());
    }

}
