package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.SupplierConfiguration;
import com.microservices.apigateway.security.configuration.KeycloakServiceAccountConfiguration;
import io.opentracing.Span;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpComponent;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.opentracing.ActiveSpanManager;
import org.apache.camel.util.jsse.SSLContextParameters;
import org.apache.camel.util.jsse.TrustManagersParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import javax.net.ssl.*;
import javax.ws.rs.core.MediaType;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component("SupplierInternalRoute")
public class SupplierInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(SupplierInternalRoute.class);

    @Autowired
    private SupplierConfiguration supplierConfig;

    @Autowired
    private KeycloakServiceAccountConfiguration supplierServiceAccountConfig;

    @Override
    public void configure() throws Exception {
        errorHandler(noErrorHandler());

        // CAUTION: Use the following block to bypass SSL self-signed certificates.
        // ----------------------------------------------------------------------------------------------------
        TrustManagersParameters trustManagersParameters = new TrustManagersParameters();
        X509ExtendedTrustManager extendedTrustManager = new SupplierInternalRoute.InsecureX509TrustManager();
        trustManagersParameters.setTrustManager(extendedTrustManager);

        SSLContextParameters scp = new SSLContextParameters();
        scp.setTrustManagers(trustManagersParameters);

        HttpComponent httpComponent = getContext().getComponent("https4", HttpComponent.class);
        httpComponent.setSslContextParameters(scp);
        // ----------------------------------------------------------------------------------------------------

        // /--------------------------------------------------\
        // | Internal route definition                        |
        // | GET <?> Event                                    |
        // \--------------------------------------------------/
        from("direct:internal-supplier-status")
            .id("direct-status-supplier")
                .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .removeHeader("breadcrumbId")
                .removeHeader("Authorization")
                .to(authCredentials())
                .process((e) -> {
                    e.getIn().getHeaders().forEach((k,v) -> {
                        if (k.equals("Authorization")) {
                            System.out.println(k + "=[" + v + "]");
                        }
                    });
                })
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("https4://" + supplierConfig.getHost() + ":" + supplierConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=supplier")
                .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-supplier-event")
            .id("direct-supplier-event")
                .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
                .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .removeHeader("breadcrumbId")
                .removeHeader("Authorization")
                .to(authCredentials())
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("https4://" + supplierConfig.getHost() + ":" + supplierConfig.getPort() + supplierConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=supplier")
                .unmarshal().json(JsonLibrary.Jackson)
            .end();

    }

    private String authCredentials() {
        return String.format("auth:api?serverUrl=%s&" +
                        "realm=%s&" +
                        "clientID=%s&" +
                        "clientSecret=%s&" +
                        "grantType=%s&" +
                        "username=%s&" +
                        "password=%s",
                supplierServiceAccountConfig.getAuthServerUri(),
                supplierServiceAccountConfig.getRealm(),
                supplierServiceAccountConfig.getClientId(),
                supplierServiceAccountConfig.getSecret(),
                supplierServiceAccountConfig.getGrantType(),
                supplierServiceAccountConfig.getUsername(),
                supplierServiceAccountConfig.getPassword());
    }

    public void addTracer(Exchange exchange){
        String userAgent = (String) exchange.getIn().getHeader("user-agent");
        Span span = ActiveSpanManager.getSpan(exchange);
        span.setBaggageItem("user-agent", userAgent);
    }

    private static class InsecureX509TrustManager extends X509ExtendedTrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException { }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException { }

        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

}
