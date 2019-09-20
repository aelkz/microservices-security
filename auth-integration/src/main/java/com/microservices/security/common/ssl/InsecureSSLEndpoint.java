package com.microservices.security.common.ssl;

import org.apache.camel.api.management.ManagedResource;
import org.apache.camel.component.http4.HttpEndpoint;
import org.apache.camel.spi.UriEndpoint;

@UriEndpoint(
        firstVersion = "1.0.0",
        scheme = "httpsinsecure4",
        title = "HTTPSINSECURE4",
        syntax = "httpsinsecure4:httpUri",
        producerOnly = true,
        label = "custom https",
        lenientProperties = true
)
@ManagedResource(
        description = "Managed HttpEndpoint"
)
public class InsecureSSLEndpoint extends HttpEndpoint {

    public InsecureSSLEndpoint() {
        super();
    }

}
