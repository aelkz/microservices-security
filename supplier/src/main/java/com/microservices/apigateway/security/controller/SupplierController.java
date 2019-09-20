package com.microservices.apigateway.security.controller;

import com.microservices.apigateway.security.controller.validator.EventValidator;
import com.microservices.apigateway.security.model.Event;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Validated //required for @Valid on method parameters such as @RequesParam, @PathVariable, @RequestHeader
public class SupplierController extends BaseController {

    private static Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Timed(value = "supplier.sync", description = "Sync Operation")
    @RequestMapping(path = "/v1/sync", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Sync",
            notes = "Sync Supplier",
            response = Event.class)
    @PreAuthorize("hasAuthority('ROLE_SUPPLIER_MAINTAINER')")
    public ResponseEntity<Event> sync(@Valid @RequestBody Event event) {
        Event e = new Event("HELLO: " + event.getStatus());
        return ResponseEntity.ok().body(e);
    }

    @InitBinder("event")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new EventValidator());
    }
}
