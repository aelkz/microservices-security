package com.microservices.apigateway.security.model;

public class Result {
    private String apiVersion;
    private Object payload;
    private Meta meta;

    public Result() { }

    public Result(String apiVersion, Object payload, Meta meta) {
        this.apiVersion = apiVersion;
        this.payload = payload;
        this.meta = meta;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public Meta getMeta() {
        return this.meta;
    }
}
