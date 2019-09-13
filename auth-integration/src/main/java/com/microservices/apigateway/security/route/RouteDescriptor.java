package com.microservices.apigateway.security.route;

public enum RouteDescriptor {

    ROUTE_01( "/resource", "resource-api", "fuse-resource-integration-api");

    private String uri;
    private String id;
    private String description;

    RouteDescriptor(String uri, String id, String description) {
        this.uri = uri;
        this.id = id;
        this.description = description;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
