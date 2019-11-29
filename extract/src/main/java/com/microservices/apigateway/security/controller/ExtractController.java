package com.microservices.apigateway.security.controller;

import com.microservices.apigateway.security.controller.validator.ExtractValidator;
import com.microservices.apigateway.security.model.ExtractRecord;
import com.microservices.apigateway.security.repository.ExtractCustomRespository;
import com.microservices.apigateway.security.service.ExtractService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Validated //required for @Valid on method parameters such as @RequesParam, @PathVariable, @RequestHeader
public class ExtractController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final transient Logger logger = LoggerFactory.getLogger(ExtractController.class);

    @Autowired
    ExtractService extractService;

    @RequestMapping(path = "/v1/extract", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all banking records",
            notes = "Returns first N banking records specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    public Page<ExtractRecord> getAll(
            @ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(required = false) Integer page) {

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        if (page == null) {
            page = 0;
        }

        Pageable pageable = new PageRequest(page, size);
        Page<ExtractRecord> products = extractService.findAllSorted(pageable);

        return products;
    }

    @RequestMapping(path = "/v1/record", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Create new banking record",
            notes = "Creates new banking record of Withdrawal or Deposit type. Returns created record with id.",
            response = ExtractCustomRespository.class)
    public ResponseEntity<ExtractRecord> add(
            @Valid @RequestBody ExtractRecord product) {

        product = extractService.save(product);
        return ResponseEntity.ok().body(product);
    }

    @InitBinder("event")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ExtractValidator());
    }
}
