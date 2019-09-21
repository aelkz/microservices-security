package com.microservices.apigateway.security.common.camel;

import org.apache.camel.Endpoint;
import org.apache.camel.impl.DefaultComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

@SuppressWarnings("WeakerAccess")
public class AuthComponent extends DefaultComponent {
    private Logger log = LoggerFactory.getLogger(AuthComponent.class);

    public static final String SERVER_URL = "serverUrl";
    public static final String REALM = "realm";
    public static final String CLIENT_ID = "clientID";
    public static final String CLIENT_SECRET = "clientSecret";
    public static final String GRANT_TYPE = "grantType";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @Override
    protected Endpoint createEndpoint(String uri, String remaining, Map<String, Object> parameters) throws Exception {
        Endpoint endpoint = new AuthEndpoint(uri, this);
        setProperties(endpoint, parameters);
        return endpoint;
    }
}
