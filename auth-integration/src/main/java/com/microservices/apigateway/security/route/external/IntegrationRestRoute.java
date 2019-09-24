package com.microservices.apigateway.security.route.external;

import com.microservices.apigateway.security.model.ErrorMessage;
import com.microservices.apigateway.security.model.Product;
import com.microservices.apigateway.security.model.ApiResponse;
import com.microservices.apigateway.security.model.health.springboot1.Health;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.ws.rs.core.MediaType;
import static org.apache.camel.model.rest.RestParamType.body;

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

    @Override
    public void configure() throws Exception {
        // @formatter:off

        // /--------------------------------------------------\
        // | Configure REST endpoint                          |
        // \--------------------------------------------------/

        // Access-Control-Allow-Origin
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
                //.corsAllowCredentials(true)
                //.corsHeaderProperty("Access-Control-Allow-Origin","*")
                .corsHeaderProperty("Access-Control-Allow-Headers", "Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Content-Length");

        // /--------------------------------------------------\
        // | Expose route w/ REST Endpoints                   |
        // \--------------------------------------------------/

        rest("/status").id("status-endpoint")
            .produces(MediaType.APPLICATION_JSON)
            .consumes(MediaType.APPLICATION_JSON)
            .skipBindingOnErrorCode(false) // enable json marshalling for body in case of errors

            .get().description("Health")
                .outType(Health.class)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .responseMessage().code(200).responseModel(Health.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .route().routeId("status")
                    .streamCaching()
                    .to("log:post-list?showHeaders=true&level=DEBUG")
                    .to("direct:internal-integration-health")
                    .unmarshal().json(JsonLibrary.Jackson, Health.class)
                .endRest();

        // /--------------------------------------------------\
        // | Expose route w/ REST Product endpoint            |
        // \--------------------------------------------------/

        rest("/product").id("product-endpoint")
            .produces(MediaType.APPLICATION_JSON)
            .consumes(MediaType.APPLICATION_JSON)
            .skipBindingOnErrorCode(false) // enable json marshalling for body in case of errors

            .get("/status").id("product-status").description("Product Health")
                .outType(com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .responseMessage().code(200).responseModel(com.microservices.apigateway.security.model.health.springboot2.Health.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .route().routeId("status-product")
                    .streamCaching()
                    .to("direct:internal-product-status")
                    .unmarshal().json(JsonLibrary.Jackson, com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .endRest()

            .get().description("List Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .responseMessage().code(200).responseModel(Product.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .route().routeId("list-product")
                    .to("direct:internal-list-products")
                .endRest()

            .get("/{productId}").description("Get Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .param().name("productId").type(RestParamType.path).description("Id of the Product. Must be a valid number.").required(true).endParam()
                .responseMessage().code(200).responseModel(Product.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .outType(Product.class)
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
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .param().name("body").type(body).description("The Product to be created").endParam()
                .responseMessage().code(200).responseModel(Product.class).endResponseMessage()
                .responseMessage().code(400).responseModel(ApiResponse.class).message("Unexpected body").endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .type(Product.class).outType(Product.class)
                .route().routeId("create-product")
                    .streamCaching()
                    .to("direct:internal-create-product")
                .endRest()

            .put().description("Update Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .param().name("body").type(body).description("The Product to be updated").endParam()
                .responseMessage().code(200).responseModel(Product.class).endResponseMessage()
                .responseMessage().code(400).responseModel(ApiResponse.class).message("Unexpected body").endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .type(Product.class).outType(Product.class)
                .route().routeId("update-product")
                    .streamCaching()
                    .onException(HttpOperationFailedException.class)
                        .handled(true).setHeader(Exchange.CONTENT_TYPE, constant(MediaType.APPLICATION_JSON))
                        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                        .transform().constant(new ErrorMessage("Product not found!"))
                        .marshal().json(JsonLibrary.Jackson)
                    .end()
                    .to("direct:internal-update-product")
                .endRest()

            .delete("/{productId}").description("Delete Product")
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .param().name("productId").type(RestParamType.path).description("Id of the Product. Must be a valid number.").required(true).dataType("string").endParam()
                .responseMessage().code(200).responseModel(Product.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .outType(Product.class)
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
        // just a simple call to stock API.

        rest("/stock").id("stock-endpoint")

            .get("/status").id("stock-status").description("Stock Health")
                .outType(com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .responseMessage().code(200).responseModel(com.microservices.apigateway.security.model.health.springboot2.Health.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .route().routeId("status-stock")
                    .streamCaching()
                    .to("direct:internal-stock-status")
                    .unmarshal().json(JsonLibrary.Jackson, com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .endRest()

            .get("/maintenance").description("Call stock API").produces(MediaType.APPLICATION_JSON)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .route().routeId("stock-event")
                    .streamCaching()
                    .to("direct:internal-stock-event")
                .endRest();

        // /--------------------------------------------------\
        // | Expose route w/ REST Supplier endpoint           |
        // \--------------------------------------------------/
        // just a simple call to supplier API.

        rest("/supplier").id("supplier-endpoint")

            .get("/status").id("supplier-status").description("Supplier Health")
                .outType(com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .responseMessage().code(200).responseModel(com.microservices.apigateway.security.model.health.springboot2.Health.class).endResponseMessage()
                .responseMessage().code(500).responseModel(ApiResponse.class).endResponseMessage()
                .route().routeId("status-supplier")
                    .streamCaching()
                    .to("direct:internal-supplier-status")
                    .unmarshal().json(JsonLibrary.Jackson, com.microservices.apigateway.security.model.health.springboot2.Health.class)
                .endRest()

            .get("/maintenance").description("Call supplier API").produces(MediaType.APPLICATION_JSON)
                .param().name("Authorization").type(RestParamType.header).description("Bearer Token").endParam()
                .route().routeId("supplier-event")
                    .streamCaching()
                    .to("direct:internal-supplier-event")
                .endRest();

        // @formatter:on
    }

}
