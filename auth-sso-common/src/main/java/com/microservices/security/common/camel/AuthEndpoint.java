package com.microservices.security.common.camel;

import org.apache.camel.Component;
import org.apache.camel.Consumer;
import org.apache.camel.Processor;
import org.apache.camel.Producer;
import org.apache.camel.impl.DefaultEndpoint;
import org.apache.camel.spi.Metadata;
import org.apache.camel.spi.UriEndpoint;
import org.apache.camel.spi.UriParam;
import org.apache.camel.spi.UriPath;

@UriEndpoint(scheme = "auth", title = "Auth", syntax = "auth:name", producerOnly = true, label = "custom")
public class AuthEndpoint extends DefaultEndpoint {
    @UriPath
    @Metadata(required = "true")
    private String name;
    @UriParam
    @Metadata(required = "true")
    private String serverUrl;
    @UriParam
    @Metadata(required = "true")
    private String realm;
    @UriParam
    @Metadata(required = "true")
    private String clientID;
    @UriParam
    @Metadata(required = "true")
    private String clientSecret;
    @UriParam
    @Metadata(required = "true")
    private String grantType;
    @UriParam
    @Metadata(required = "true")
    private String username;
    @UriParam
    @Metadata(required = "true")
    private String password;

    AuthEndpoint(String endpointUri, Component component) {
        super(endpointUri, component);
    }

    public Producer createProducer() throws Exception {
        return new AuthProducer(this);
    }

    public Consumer createConsumer(Processor processor) throws Exception {
        return new AuthConsumer(this, processor);
    }

    public boolean isSingleton() {
        return true;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
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
