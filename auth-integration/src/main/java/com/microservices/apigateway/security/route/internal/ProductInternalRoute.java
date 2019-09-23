package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.ProductConfiguration;
import com.microservices.apigateway.security.model.Product;
import com.microservices.apigateway.security.processor.ExceptionProcessor;
import io.opentracing.Span;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.opentracing.ActiveSpanManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("ProductInternalRoute")
public class ProductInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(ProductInternalRoute.class);

    private ProductConfiguration productConfig;

    private ExceptionProcessor exceptionProcessor;

    public ProductInternalRoute (
            ProductConfiguration productConfig,
            ExceptionProcessor exceptionProcessor
    ) {
        this.productConfig = productConfig;
        this.exceptionProcessor = exceptionProcessor;
    }

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

        from("direct:internal-status-product")
            .id("direct-status-product")
            .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
            .end();

        from("direct:internal-list-products")
            .id("direct-list-products")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
                .unmarshal().json(JsonLibrary.Jackson)
            .end();

        // ${header.productId}
        from("direct:internal-get-product")
            .id("direct-get-product")
            .log("HELLO 3")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .pipeline()
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .process((exchange) -> {
                    exchange.getIn().getHeaders().forEach((k,v) -> {
                        System.out.println("["+k+"]="+v);
                    });
                })
                .toD("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "/${header.productId}?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
            .end()
            .unmarshal().json(JsonLibrary.Jackson, Product.class)
            .end();

        from("direct:internal-create-product")
            .id("direct-create-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .marshal().json(JsonLibrary.Jackson)
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
            .unmarshal().json(JsonLibrary.Jackson, Product.class)
            .end();

        from("direct:internal-update-product")
            .id("direct-update-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .marshal().json(JsonLibrary.Jackson)
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "/${header.productId}?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
            .unmarshal().json(JsonLibrary.Jackson, Product.class)
            .end();

        from("direct:internal-delete-product")
            .id("direct-delete-product")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + productConfig.getHost() + ":" + productConfig.getPort() + productConfig.getContextPath() + "/${header.productId}?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=product")
            .unmarshal().json(JsonLibrary.Jackson, Product.class)
            .end();
    }

    public void addTracer(Exchange exchange){
        String userAgent = (String) exchange.getIn().getHeader("user-agent");
        Span span = ActiveSpanManager.getSpan(exchange);
        span.setBaggageItem("user-agent", userAgent);
    }

}
