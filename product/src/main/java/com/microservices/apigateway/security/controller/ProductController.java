package com.microservices.apigateway.security.controller;

import com.microservices.apigateway.security.controller.validator.EventValidator;
import com.microservices.apigateway.security.model.Event;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Validated //required for @Valid on method parameters such as @RequesParam, @PathVariable, @RequestHeader
public class ProductController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Timed(value = "product.sync", description = "Sync Operation")
    @RequestMapping(path = "/v1/sync", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Sync",
            notes = "Sync Product",
            response = Event.class)
    public ResponseEntity<Event> sync() {
        Event e = new Event("HELLO");
        return ResponseEntity.ok().body(e);
    }

    @InitBinder("event")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new EventValidator());
    }
}
