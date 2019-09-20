package com.microservices.security.common.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import org.apache.camel.component.http4.HttpClientConfigurer;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// <touri="http4://invoke?httpClientConfigurer=#sslConfig"/>

@Configuration
public class SSLHttpClientConfigurer implements HttpClientConfigurer {

    // @Bean
    public void configureHttpClient(HttpClientBuilder clientBuilder) {
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();

            clientBuilder.setSslcontext(sslContext);

        }catch(Exception e) { }
    }
}
