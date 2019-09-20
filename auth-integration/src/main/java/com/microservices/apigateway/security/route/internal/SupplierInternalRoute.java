package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.SupplierConfiguration;
import com.microservices.apigateway.security.configuration.SupplierServiceAccountConfiguration;
import com.microservices.apigateway.security.processor.ExceptionProcessor;
import io.opentracing.Span;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.opentracing.ActiveSpanManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;

@Component("SupplierInternalRoute")
public class SupplierInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(SupplierInternalRoute.class);

    @Autowired
    private SupplierConfiguration supplierConfig;

    @Autowired
    private ExceptionProcessor exceptionProcessor;

    @Autowired
    private SupplierServiceAccountConfiguration supplierServiceAccountConfig;

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .handled(true)
                .process(exceptionProcessor)
                .redeliveryDelay(150)
                .maximumRedeliveries(2) // 1 + 2 retries
                .to("log:exception");

        // /--------------------------------------------------\
        // | Internal route definition                        |
        // | GET <?> Event                                    |
        // \--------------------------------------------------/
        from("direct:internal-supplier-status")
            .id("direct-status-supplier")
                .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .log("HELLO2")
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .removeHeader("breadcrumbId")
                .removeHeader("Authorization")
                .process((e) -> {
                    e.getIn().getHeaders().forEach((k,v) -> {
                        System.out.println(k + "=[" + v + "]");
                    });
                })
                .to(authCredentials())
                .log("HELLO3")
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .log("http4://" + supplierConfig.getHost() + ":" + supplierConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
                .to("http4://" + supplierConfig.getHost() + ":" + supplierConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
                .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-supplier-event")
            .id("direct-supplier-event")
                .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
                .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .to(authCredentials())
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("http4://" + supplierConfig.getHost() + ":" + supplierConfig.getPort() + supplierConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
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

}
