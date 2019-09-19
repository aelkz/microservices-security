package com.microservices.apigateway.security.controller;

import com.microservices.apigateway.security.controller.validator.ProductValidator;
import com.microservices.apigateway.security.model.Product;
import com.microservices.apigateway.security.service.ProductService;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
@Validated //required for @Valid on method parameters such as @RequesParam, @PathVariable, @RequestHeader
@CrossOrigin(origins = {"https://localhost:4200", "http://localhost:4200"})
public class ProductController extends BaseController {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final transient Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    ProductService productService;

    @RequestMapping(path = "/v1/products", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get all products",
            notes = "Returns first N products specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    @PreAuthorize("hasAnyAuthority('PRODUCT_BASIC','PRODUCT_ADMIN','CHECK_PRODUCT_STATUS')")
    public Page<Product> getAll(
            @ApiParam("The size of the page to be returned") @RequestParam(required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(required = false) Integer page) {

        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }

        if (page == null) {
            page = 0;
        }

        Pageable pageable = new PageRequest(page, size);
        Page<Product> products = productService.findAll(pageable);

        return products;
    }

    @RequestMapping(path = "/v1/product/{id}", method = RequestMethod.GET)
    @ApiOperation(
            value = "Get product by id",
            notes = "Returns product for id specified.",
            response = Product.class)
    @ApiResponses(value = {@ApiResponse(code = 404, message = "Product not found") })
    @PreAuthorize("hasAnyAuthority('PRODUCT_BASIC','PRODUCT_ADMIN','CHECK_PRODUCT_STATUS')")
    public ResponseEntity<Product> get(@ApiParam("Product id") @PathVariable("id") Long id) {

        Product product = productService.findOne(id);
        return (product == null ? ResponseEntity.status(HttpStatus.NOT_FOUND) : ResponseEntity.ok()).body(product);
    }

    @RequestMapping(path = "/v1/product", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Create new product",
            notes = "Creates new product. Returns created product with id.",
            response = Product.class)
    @PreAuthorize("hasAnyAuthority('PRODUCT_ADMIN')")
    public ResponseEntity<Product> add(
            @Valid @RequestBody Product product) {

        product = productService.save(product);
        return ResponseEntity.ok().body(product);
    }

    @RequestMapping(path = "/v1/product/{id}", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Update product",
            notes = "Update product. Returns updated product.",
            response = Product.class)
    @PreAuthorize("hasAnyAuthority('PRODUCT_ADMIN')")
    public ResponseEntity<Product> update(
            @Valid @RequestBody Product product) {

        product = productService.update(product);
        return ResponseEntity.ok().body(product);
    }

    @RequestMapping(path = "/v1/product/{id}", method = RequestMethod.DELETE, consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
    @ApiOperation(
            value = "Delete product",
            notes = "Delete product. Returns 204 no content.",
            response = Product.class)
    @PreAuthorize("hasAnyAuthority('PRODUCT_ADMIN')")
    public ResponseEntity<Product> delete(@ApiParam("Product id") @PathVariable("id") Long id) {

        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @InitBinder("product")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(new ProductValidator());
    }
    
}
