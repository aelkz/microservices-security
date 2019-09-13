package com.microservices.apigateway.security.route.external;

import com.microservices.apigateway.security.model.ErrorMessage;
import com.microservices.apigateway.security.model.Event;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.print.attribute.standard.Media;
import javax.ws.rs.core.MediaType;
import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

@Component("IntegrationRestRoute")
public class IntegrationRestRoute extends RouteBuilder {

    @Value("${api.version}")
    private String apiVersion;

    @Value("${api.title}")
    private String apiTitle;

    @Value("${api.description}")
    private String apiDescription;

    @Value("${api.hostname}")
    private String apiHostname;

    @Autowired
    private Environment env;

    @Override
    public void configure() throws Exception {
        // @formatter:off

        // /--------------------------------------------------\
        // | Configure REST endpoint                          |
        // \--------------------------------------------------/
        restConfiguration()
                .contextPath("/api/v" + apiVersion)
                .apiContextPath("/api-docs")
                .apiProperty("api.title", apiTitle)
                .apiProperty("api.description", apiDescription)
                .apiProperty("api.version", apiVersion)
                .apiProperty("schemes", "https")
                .host(apiHostname)
                .apiProperty("api.version", apiVersion)
                .dataFormatProperty("prettyPrint", "true")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Headers", "Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        // /--------------------------------------------------\
        // | Expose route w/ REST Product endpoint            |
        // \--------------------------------------------------/

        rest("/product").id("product-endpoint")
            .produces(MediaType.APPLICATION_JSON)
            .consumes(MediaType.APPLICATION_JSON)

            .get().description("List Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .outTypeList(Event.class)
                .route().routeId("list-product")
                    .to("direct:internal-list-products")
                .endRest()

            .get("/{productId}").description("Get Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .param().name("productId").type(RestParamType.path).description("Find Product by ID").endParam()
                .outType(Event.class)
                .route().routeId("get-product")
                    .streamCaching()
                    .onException(HttpOperationFailedException.class)
                        .handled(true).setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                        .transform().constant(new ErrorMessage("Product not found!"))
                        .marshal().json(JsonLibrary.Jackson)
                    .end()
                    .to("direct:internal-get-product")
                .endRest()

            .post().description("Create Product")
                .param().name("body").type(body).description("The Product to be created").endParam()
                .type(Event.class).outType(Event.class)
                .route().routeId("create-product")
                    .streamCaching()
                    .to("direct:internal-create-product")
                .endRest()

            .put().description("Update Product")
                .param().name("body").type(body).description("The Product to be updated").endParam()
                .type(Event.class).outType(Event.class)
                .route().routeId("update-product")
                    .streamCaching()
                    .to("direct:internal-update-product")
                .endRest()

            .delete("/{productId}").description("Delete Product")
                .param().name("productId").type(path).description("Delete Product by ID").endParam()
                .outType(Event.class)
                .route().routeId("delete-product")
                    .streamCaching()
                    .onException(HttpOperationFailedException.class)
                        .handled(true)
                        .setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404)).transform()
                        .constant(new ErrorMessage("Product not found!"))
                        .marshal().json(JsonLibrary.Jackson)
                        .end()
                    .to("direct:internal-delete-product")
                .endRest();

        // /--------------------------------------------------\
        // | Expose route w/ REST Stock endpoint              |
        // \--------------------------------------------------/
/*
        rest("/stock").id("stock-endpoint")
            .get().description("Call stock event").produces("application/json")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .outTypeList(Event.class)
                .route().routeId("stockEvent")
                    .to("direct:stockEvent").bean(this, "wrap")
                .endRest()
            .get("/{eventId}").description("Get Stock Event").produces("application/json")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .param().name("agenciaId").type(RestParamType.path).description("Find stock event by id").endParam()
                .outType(Event.class)
                .route().routeId("getStockEvent")
                    .streamCaching()
                    .onException(HttpOperationFailedException.class)
                        .handled(true).setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                        .transform().constant(new ErrorMessage("Stock Event not found!"))
                        .bean(this, "wrap").marshal().json(JsonLibrary.Jackson)
                    .end()
                    .to("direct:getStockEvent").bean(this, "wrap")
                .endRest();

        // /--------------------------------------------------\
        // | Expose route w/ REST Supplier endpoint           |
        // \--------------------------------------------------/

        rest("/supplier").id("supplier-endpoint")
            .get().description("Call supplier event").produces("application/json")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .outTypeList(Event.class)
                .route().routeId("supplierEvent")
                    .to("direct:supplierEvent").bean(this, "wrap")
                .endRest()
            .get("/{supplierId}").description("Get Supplier Event").produces("application/json")
                .param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .param().name("supplierId").type(RestParamType.path).description("Find supplier event by id").endParam()
                .outType(Event.class)
                .route().routeId("getSupplierEvent")
                    .streamCaching()
                    .onException(HttpOperationFailedException.class)
                        .handled(true).setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                        .transform().constant(new ErrorMessage("Supplier Event not found!"))
                        .bean(this, "wrap").marshal().json(JsonLibrary.Jackson)
                    .end()
                    .to("direct:getSupplierEvent").bean(this, "wrap")
                .endRest();
        // @formatter:on
     */
    }

}
