package com.microservices.apigateway.security.model.health.springboot1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "route:status",
        "route:status-product",
        "route:list-product",
        "route:get-product",
        "route:create-product",
        "route:update-product",
        "route:delete-product",
        "route:status-stock",
        "route:stock-event",
        "route:status-supplier",
        "route:supplier-event",
        "route:route1",
        "route:direct-integration-health",
        "route:route2",
        "route:route3",
        "route:route4",
        "route:route5",
        "route:route6",
        "route:route7",
        "route:route8",
        "route:route9",
        "route:direct-status-supplier",
        "route:direct-supplier-event"
})
public class CamelHealthChecks implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("route:status")
    private String routeStatus;
    @JsonProperty("route:status-product")
    private String routeStatusProduct;
    @JsonProperty("route:list-product")
    private String routeListProduct;
    @JsonProperty("route:get-product")
    private String routeGetProduct;
    @JsonProperty("route:create-product")
    private String routeCreateProduct;
    @JsonProperty("route:update-product")
    private String routeUpdateProduct;
    @JsonProperty("route:delete-product")
    private String routeDeleteProduct;
    @JsonProperty("route:status-stock")
    private String routeStatusStock;
    @JsonProperty("route:stock-event")
    private String routeStockEvent;
    @JsonProperty("route:status-supplier")
    private String routeStatusSupplier;
    @JsonProperty("route:supplier-event")
    private String routeSupplierEvent;
    @JsonProperty("route:route1")
    private String routeRoute1;
    @JsonProperty("route:direct-integration-health")
    private String routeDirectIntegrationHealth;
    @JsonProperty("route:route2")
    private String routeRoute2;
    @JsonProperty("route:route3")
    private String routeRoute3;
    @JsonProperty("route:route4")
    private String routeRoute4;
    @JsonProperty("route:route5")
    private String routeRoute5;
    @JsonProperty("route:route6")
    private String routeRoute6;
    @JsonProperty("route:route7")
    private String routeRoute7;
    @JsonProperty("route:route8")
    private String routeRoute8;
    @JsonProperty("route:route9")
    private String routeRoute9;
    @JsonProperty("route:direct-status-supplier")
    private String routeDirectStatusSupplier;
    @JsonProperty("route:direct-supplier-event")
    private String routeDirectSupplierEvent;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2132306290093423152L;

    /**
     * No args constructor for use in serialization
     */
    public CamelHealthChecks() {
    }

    /**
     * @param routeDirectIntegrationHealth
     * @param routeDeleteProduct
     * @param routeRoute1
     * @param routeRoute2
     * @param routeRoute3
     * @param routeSupplierEvent
     * @param routeRoute4
     * @param routeRoute5
     * @param status
     * @param routeRoute6
     * @param routeStatusSupplier
     * @param routeRoute7
     * @param routeRoute8
     * @param routeUpdateProduct
     * @param routeRoute9
     * @param routeCreateProduct
     * @param routeGetProduct
     * @param routeStatusProduct
     * @param routeDirectSupplierEvent
     * @param routeListProduct
     * @param routeStatusStock
     * @param routeDirectStatusSupplier
     * @param routeStatus
     * @param routeStockEvent
     */
    public CamelHealthChecks(String status, String routeStatus, String routeStatusProduct, String routeListProduct, String routeGetProduct, String routeCreateProduct, String routeUpdateProduct, String routeDeleteProduct, String routeStatusStock, String routeStockEvent, String routeStatusSupplier, String routeSupplierEvent, String routeRoute1, String routeDirectIntegrationHealth, String routeRoute2, String routeRoute3, String routeRoute4, String routeRoute5, String routeRoute6, String routeRoute7, String routeRoute8, String routeRoute9, String routeDirectStatusSupplier, String routeDirectSupplierEvent) {
        super();
        this.status = status;
        this.routeStatus = routeStatus;
        this.routeStatusProduct = routeStatusProduct;
        this.routeListProduct = routeListProduct;
        this.routeGetProduct = routeGetProduct;
        this.routeCreateProduct = routeCreateProduct;
        this.routeUpdateProduct = routeUpdateProduct;
        this.routeDeleteProduct = routeDeleteProduct;
        this.routeStatusStock = routeStatusStock;
        this.routeStockEvent = routeStockEvent;
        this.routeStatusSupplier = routeStatusSupplier;
        this.routeSupplierEvent = routeSupplierEvent;
        this.routeRoute1 = routeRoute1;
        this.routeDirectIntegrationHealth = routeDirectIntegrationHealth;
        this.routeRoute2 = routeRoute2;
        this.routeRoute3 = routeRoute3;
        this.routeRoute4 = routeRoute4;
        this.routeRoute5 = routeRoute5;
        this.routeRoute6 = routeRoute6;
        this.routeRoute7 = routeRoute7;
        this.routeRoute8 = routeRoute8;
        this.routeRoute9 = routeRoute9;
        this.routeDirectStatusSupplier = routeDirectStatusSupplier;
        this.routeDirectSupplierEvent = routeDirectSupplierEvent;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("route:status")
    public String getRouteStatus() {
        return routeStatus;
    }

    @JsonProperty("route:status")
    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }

    @JsonProperty("route:status-product")
    public String getRouteStatusProduct() {
        return routeStatusProduct;
    }

    @JsonProperty("route:status-product")
    public void setRouteStatusProduct(String routeStatusProduct) {
        this.routeStatusProduct = routeStatusProduct;
    }

    @JsonProperty("route:list-product")
    public String getRouteListProduct() {
        return routeListProduct;
    }

    @JsonProperty("route:list-product")
    public void setRouteListProduct(String routeListProduct) {
        this.routeListProduct = routeListProduct;
    }

    @JsonProperty("route:get-product")
    public String getRouteGetProduct() {
        return routeGetProduct;
    }

    @JsonProperty("route:get-product")
    public void setRouteGetProduct(String routeGetProduct) {
        this.routeGetProduct = routeGetProduct;
    }

    @JsonProperty("route:create-product")
    public String getRouteCreateProduct() {
        return routeCreateProduct;
    }

    @JsonProperty("route:create-product")
    public void setRouteCreateProduct(String routeCreateProduct) {
        this.routeCreateProduct = routeCreateProduct;
    }

    @JsonProperty("route:update-product")
    public String getRouteUpdateProduct() {
        return routeUpdateProduct;
    }

    @JsonProperty("route:update-product")
    public void setRouteUpdateProduct(String routeUpdateProduct) {
        this.routeUpdateProduct = routeUpdateProduct;
    }

    @JsonProperty("route:delete-product")
    public String getRouteDeleteProduct() {
        return routeDeleteProduct;
    }

    @JsonProperty("route:delete-product")
    public void setRouteDeleteProduct(String routeDeleteProduct) {
        this.routeDeleteProduct = routeDeleteProduct;
    }

    @JsonProperty("route:status-stock")
    public String getRouteStatusStock() {
        return routeStatusStock;
    }

    @JsonProperty("route:status-stock")
    public void setRouteStatusStock(String routeStatusStock) {
        this.routeStatusStock = routeStatusStock;
    }

    @JsonProperty("route:stock-event")
    public String getRouteStockEvent() {
        return routeStockEvent;
    }

    @JsonProperty("route:stock-event")
    public void setRouteStockEvent(String routeStockEvent) {
        this.routeStockEvent = routeStockEvent;
    }

    @JsonProperty("route:status-supplier")
    public String getRouteStatusSupplier() {
        return routeStatusSupplier;
    }

    @JsonProperty("route:status-supplier")
    public void setRouteStatusSupplier(String routeStatusSupplier) {
        this.routeStatusSupplier = routeStatusSupplier;
    }

    @JsonProperty("route:supplier-event")
    public String getRouteSupplierEvent() {
        return routeSupplierEvent;
    }

    @JsonProperty("route:supplier-event")
    public void setRouteSupplierEvent(String routeSupplierEvent) {
        this.routeSupplierEvent = routeSupplierEvent;
    }

    @JsonProperty("route:route1")
    public String getRouteRoute1() {
        return routeRoute1;
    }

    @JsonProperty("route:route1")
    public void setRouteRoute1(String routeRoute1) {
        this.routeRoute1 = routeRoute1;
    }

    @JsonProperty("route:direct-integration-health")
    public String getRouteDirectIntegrationHealth() {
        return routeDirectIntegrationHealth;
    }

    @JsonProperty("route:direct-integration-health")
    public void setRouteDirectIntegrationHealth(String routeDirectIntegrationHealth) {
        this.routeDirectIntegrationHealth = routeDirectIntegrationHealth;
    }

    @JsonProperty("route:route2")
    public String getRouteRoute2() {
        return routeRoute2;
    }

    @JsonProperty("route:route2")
    public void setRouteRoute2(String routeRoute2) {
        this.routeRoute2 = routeRoute2;
    }

    @JsonProperty("route:route3")
    public String getRouteRoute3() {
        return routeRoute3;
    }

    @JsonProperty("route:route3")
    public void setRouteRoute3(String routeRoute3) {
        this.routeRoute3 = routeRoute3;
    }

    @JsonProperty("route:route4")
    public String getRouteRoute4() {
        return routeRoute4;
    }

    @JsonProperty("route:route4")
    public void setRouteRoute4(String routeRoute4) {
        this.routeRoute4 = routeRoute4;
    }

    @JsonProperty("route:route5")
    public String getRouteRoute5() {
        return routeRoute5;
    }

    @JsonProperty("route:route5")
    public void setRouteRoute5(String routeRoute5) {
        this.routeRoute5 = routeRoute5;
    }

    @JsonProperty("route:route6")
    public String getRouteRoute6() {
        return routeRoute6;
    }

    @JsonProperty("route:route6")
    public void setRouteRoute6(String routeRoute6) {
        this.routeRoute6 = routeRoute6;
    }

    @JsonProperty("route:route7")
    public String getRouteRoute7() {
        return routeRoute7;
    }

    @JsonProperty("route:route7")
    public void setRouteRoute7(String routeRoute7) {
        this.routeRoute7 = routeRoute7;
    }

    @JsonProperty("route:route8")
    public String getRouteRoute8() {
        return routeRoute8;
    }

    @JsonProperty("route:route8")
    public void setRouteRoute8(String routeRoute8) {
        this.routeRoute8 = routeRoute8;
    }

    @JsonProperty("route:route9")
    public String getRouteRoute9() {
        return routeRoute9;
    }

    @JsonProperty("route:route9")
    public void setRouteRoute9(String routeRoute9) {
        this.routeRoute9 = routeRoute9;
    }

    @JsonProperty("route:direct-status-supplier")
    public String getRouteDirectStatusSupplier() {
        return routeDirectStatusSupplier;
    }

    @JsonProperty("route:direct-status-supplier")
    public void setRouteDirectStatusSupplier(String routeDirectStatusSupplier) {
        this.routeDirectStatusSupplier = routeDirectStatusSupplier;
    }

    @JsonProperty("route:direct-supplier-event")
    public String getRouteDirectSupplierEvent() {
        return routeDirectSupplierEvent;
    }

    @JsonProperty("route:direct-supplier-event")
    public void setRouteDirectSupplierEvent(String routeDirectSupplierEvent) {
        this.routeDirectSupplierEvent = routeDirectSupplierEvent;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("routeStatus", routeStatus).append("routeStatusProduct", routeStatusProduct).append("routeListProduct", routeListProduct).append("routeGetProduct", routeGetProduct).append("routeCreateProduct", routeCreateProduct).append("routeUpdateProduct", routeUpdateProduct).append("routeDeleteProduct", routeDeleteProduct).append("routeStatusStock", routeStatusStock).append("routeStockEvent", routeStockEvent).append("routeStatusSupplier", routeStatusSupplier).append("routeSupplierEvent", routeSupplierEvent).append("routeRoute1", routeRoute1).append("routeDirectIntegrationHealth", routeDirectIntegrationHealth).append("routeRoute2", routeRoute2).append("routeRoute3", routeRoute3).append("routeRoute4", routeRoute4).append("routeRoute5", routeRoute5).append("routeRoute6", routeRoute6).append("routeRoute7", routeRoute7).append("routeRoute8", routeRoute8).append("routeRoute9", routeRoute9).append("routeDirectStatusSupplier", routeDirectStatusSupplier).append("routeDirectSupplierEvent", routeDirectSupplierEvent).append("additionalProperties", additionalProperties).toString();
    }
}
