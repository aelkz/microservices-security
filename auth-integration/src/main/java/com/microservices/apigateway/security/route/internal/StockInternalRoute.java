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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@Component("StockInternalRoute")
public class StockInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(StockInternalRoute.class);

    @Autowired
    private StockConfiguration stockConfig;

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
        // | Internal route definition                        |
        // | GET <?> Event                                    |
        // \--------------------------------------------------/

        from("direct:internal-status-stock")
            .id("direct-status-stock")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + stockConfig.getHost() + ":" + stockConfig.getPort() + "/actuator/health?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            //.unmarshal(new ListJacksonDataFormat(Event.class));
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

        from("direct:internal-stock-event")
            .id("direct-stock-event")
            .log(LoggingLevel.WARN, logger, "internal route: preparing to call external api using http4 producer")
            .to("log:list?showHeaders=true&level=DEBUG")
            .removeHeader("origin")
            .removeHeader(Exchange.HTTP_PATH)
            .to("log:post-list?showHeaders=true&level=DEBUG")
            .to("http4://" + stockConfig.getHost() + ":" + stockConfig.getPort() + stockConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
            .unmarshal().json(JsonLibrary.Jackson)
            .end();

    }

    public void addTracer(Exchange exchange){
        String userAgent = (String) exchange.getIn().getHeader("user-agent");
        Span span = ActiveSpanManager.getSpan(exchange);
        span.setBaggageItem("user-agent", userAgent);
    }

}
