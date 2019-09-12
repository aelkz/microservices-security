package com.microservices.apigateway.security.route.external;

import com.microservices.apigateway.security.configuration.RouteConfiguration;
import com.microservices.apigateway.security.model.ErrorMessage;
import com.microservices.apigateway.security.model.Event;
import com.microservices.apigateway.security.model.Meta;
import com.microservices.apigateway.security.model.Result;
import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.http.common.HttpOperationFailedException;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static org.apache.camel.model.rest.RestParamType.body;
import static org.apache.camel.model.rest.RestParamType.path;

@Component
public class EventRestRoute extends RouteBuilder {

    @Value("${api.version}")
    private String apiVersion;

    @Autowired
    private RouteConfiguration routeConfig;

    @Value("${api.hostname}")
    private String apiHostname;

    @Override
    public void configure() throws Exception {
        // @formatter:off
        restConfiguration()
                .contextPath("/api/v" + apiVersion)
                .apiContextPath("/api-docs")
                .apiProperty("api.title", "Agencia Composite Service")
                .apiProperty("api.description", "Composite Service da API de Agencias")
                .apiProperty("api.version", apiVersion)
                .apiProperty("schemes", "https")
                .host(apiHostname)
                .apiProperty("api.version", apiVersion)
                .dataFormatProperty("prettyPrint", "true")
                .bindingMode(RestBindingMode.json)
                .enableCORS(true)
                .corsHeaderProperty("Access-Control-Allow-Headers", "Authorization, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

        rest("/agencia").id("agencia-endpoint")
                .get().description("List Agencia").produces("application/json")
//					.param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .outTypeList(Event.class)
                .route().routeId("listAgencia")
                .to("direct:listAgencias").bean(this, "wrap")
                .endRest()
                .get("/{agenciaId}").description("Get agencia").produces("application/json")
//					.param().name("Authorization").type(RestParamType.header).description("Bearer token").endParam()
                .param().name("agenciaId").type(RestParamType.path).description("Find agencia by id").endParam()
                .outType(Event.class)
                .route().routeId("getAgencia")
                .streamCaching()
                .onException(HttpOperationFailedException.class)
                .handled(true).setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
                .transform().constant(new ErrorMessage("Agencia not found!"))
                .bean(this, "wrap").marshal().json(JsonLibrary.Jackson)
                .end()
                .to("direct:getAgencia").bean(this, "wrap")
                .endRest()
                .post().description("Create agencia").consumes("application/json").produces("application/json")
                .param().name("body").type(body).description("The agencia to be created").endParam()
                .type(Event.class).outType(Event.class)
                .route().routeId("inserirAgencia")
                .streamCaching()
                .to("direct:inserirAgencia").bean(this, "wrap")
                .endRest()
                .put().description("Alterar agencia").consumes("application/json").produces("application/json")
                .param().name("body").type(body).description("The agencia to be updated").endParam()
                .type(Event.class).outType(Event.class)
                .route().routeId("alterarAgencia")
                .streamCaching()
                .to("direct:alterarAgencia").bean(this, "wrap")
                .endRest()
                .delete("/{agenciaId}").description("Delete agencia").produces("application/json")
                .param().name("agenciaId").type(path).description("Delete agencia by id").endParam()
                .outType(Event.class)
                .route().routeId("deleteAgencia")
                .streamCaching()
                .onException(HttpOperationFailedException.class)
                .handled(true)
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404)).transform()
                .constant(new ErrorMessage("Agencia not found!")).bean(this, "wrap").marshal().json(JsonLibrary.Jackson)
                .end()
                .to("direct:deleteAgencia")
                .endRest();
        // @formatter:on
    }

}
