package com.microservices.apigateway.security.route.internal;

import com.microservices.apigateway.security.configuration.HealthConfiguration;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.ws.rs.core.MediaType;

@Component("MetricsInternalRoute")
public class MetricsInternalRoute extends RouteBuilder {

    static final Logger logger = LoggerFactory.getLogger(SupplierInternalRoute.class);

    @Autowired
    private HealthConfiguration healthConfig;

    @Override
    public void configure() throws Exception {

        // /--------------------------------------------------\
        // | Internal route definition                        |
        // | Actuator Endpoints (health, metrics, etc.)       |
        // \--------------------------------------------------/
        from("direct:internal-integration-health")
                .id("direct-integration-health")
                .to("log:list?showHeaders=true&level=DEBUG")
                .removeHeader("origin")
                .removeHeader(Exchange.HTTP_PATH)
                .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                .removeHeader("breadcrumbId")
                .to("http4://" + healthConfig.getHost() + ":" + healthConfig.getPort() + "/" + healthConfig.getContextPath() + "?connectTimeout=500&bridgeEndpoint=true&copyHeaders=true&connectionClose=true&type=metrics")
                .unmarshal().json(JsonLibrary.Jackson)
                .end();
    }
}
