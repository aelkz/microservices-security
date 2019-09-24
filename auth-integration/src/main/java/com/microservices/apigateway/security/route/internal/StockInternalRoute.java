package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.StockConfiguration;
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
import org.springframework.http.MediaType;

@Component("StockInternalRoute")
public class StockInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(StockInternalRoute.class);

    private StockConfiguration stockConfig;

    private ExceptionProcessor exceptionProcessor;

    public StockInternalRoute (
            StockConfiguration stockConfig,
            ExceptionProcessor exceptionProcessor
    ) {
        this.stockConfig = stockConfig;
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
        // | Internal route definition                        |
        // | GET <?> Event                                    |
        // \--------------------------------------------------/

        from("direct:internal-stock-status")
            .id("direct-status-stock")
            .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("http4://" + stockConfig.getHost() + ":" + stockConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=stock")
            .end();

        from("direct:internal-stock-event")
            .id("direct-stock-event")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .to("log:post-list?showHeaders=true&level=DEBUG")
                .to("http4://" + stockConfig.getHost() + ":" + stockConfig.getPort() + stockConfig.getContextPath() + "/sync?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=stock")
                .unmarshal().json(JsonLibrary.Jackson)
            .end();

    }

    public void addTracer(Exchange exchange){
        String userAgent = (String) exchange.getIn().getHeader("user-agent");
        Span span = ActiveSpanManager.getSpan(exchange);
        span.setBaggageItem("user-agent", userAgent);
    }

}
