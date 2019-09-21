package com.microservices.apigateway.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfiguration {

    @Value("${integration.health.host}")
    private String host;

    @Value("${integration.health.port}")
    private Integer port;

    @Value("${integration.health.context-path}")
    private String contextPath;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

}
