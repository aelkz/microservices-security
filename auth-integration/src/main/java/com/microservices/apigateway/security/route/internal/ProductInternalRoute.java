package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.ProductConfiguration;
import com.microservices.apigateway.security.configuration.ProductServiceAccountConfiguration;
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

@Component("ProductInternalRoute")
public class ProductInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(ProductInternalRoute.class);

    @Autowired
    private ProductConfiguration productConfig;

    @Autowired
    private ProductServiceAccountConfiguration productServiceAccountConfig;

    @Autowired
    private ExceptionProcessor exceptionProcessor;

    @Override
    public void configure() throws Exception {
        onException(Exception.class)
                .handled(true)
                .process(exceptionProcessor)
                .redeliveryDelay(150)
                .maximumRedeliveries(2) // 1 + 2 retries
                .to("log:exception");

        // /--------------------------------------------------\
        // | Internal route definition:                       |
        // | Product CRUD Operations                          |
        // \--------------------------------------------------/

        from("direct:internal-list-products")
            .id("direct-list-products")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to(authCredentials())
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-get-product")
            .id("direct-get-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to(authCredentials())
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-create-product")
            .id("direct-create-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to(authCredentials())
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .marshal().json(JsonLibrary.Jackson)
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-update-product")
            .id("direct-update-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to(authCredentials())
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .marshal().json(JsonLibrary.Jackson)
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-delete-product")
            .id("direct-delete-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to(authCredentials())
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
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
            productServiceAccountConfig.getAuthServerUri(),
            productServiceAccountConfig.getRealm(),
            productServiceAccountConfig.getClientId(),
            productServiceAccountConfig.getSecret(),
            productServiceAccountConfig.getGrantType(),
            productServiceAccountConfig.getUsername(),
            productServiceAccountConfig.getPassword());
    }

    public void addTracer(Exchange exchange){
        String userAgent = (String) exchange.getIn().getHeader("user-agent");
        Span span = ActiveSpanManager.getSpan(exchange);
        span.setBaggageItem("user-agent", userAgent);
    }

}
