package com.microservices.apigateway.security.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.ws.rs.HttpMethod;

@Configuration
public class FilterRegistrationBean {

    @Bean
    public WebMvcConfigurer corsConfiguration() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/")
                        .allowedMethods(
                                HttpMethod.GET.toString(),
                                HttpMethod.POST.toString(),
                                HttpMethod.PUT.toString(),
                                HttpMethod.DELETE.toString(),
                                HttpMethod.OPTIONS.toString()
                        )
                        .allowedOrigins("*")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
