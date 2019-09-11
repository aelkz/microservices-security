package com.microservices.apigateway.security;

import org.apache.camel.component.servlet.CamelHttpTransportServlet;
import org.apache.camel.opentracing.starter.CamelOpenTracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@CamelOpenTracing
public class AuthFuseApplication {

    private static Logger logger = LoggerFactory.getLogger(AuthFuseApplication.class);

    @Value("${camel.component.servlet.mapping.contextPath}")
    private String contextPath;

    @Bean
    public ServletRegistrationBean camelServletRegistrationBean() {
        ServletRegistrationBean registration = new ServletRegistrationBean(new CamelHttpTransportServlet(), contextPath);
        registration.setName("CamelServlet");
        return registration;
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthFuseApplication.class, args);
    }

}







