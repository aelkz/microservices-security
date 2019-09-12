package com.microservices.apigateway.security.model;

public class Meta {
    private String config;
    private String username;
    private String password;

    public Meta(String config, String username, String password) {
        this.config = config;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfig() {
        return this.config;
    }

    public void setConfig(String config) {
        this.config = config;
    }
}
