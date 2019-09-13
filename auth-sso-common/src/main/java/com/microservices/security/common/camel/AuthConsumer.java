package com.microservices.security.common.camel;

import org.apache.camel.Processor;
import org.apache.camel.impl.ScheduledPollConsumer;

public class AuthConsumer extends ScheduledPollConsumer {
    private final AuthEndpoint endpoint;

    public AuthConsumer(AuthEndpoint endpoint, Processor processor) {
        super(endpoint, processor);
        this.endpoint = endpoint;
    }

    @Override
    protected int poll() throws Exception {
        throw new RuntimeException("AuthComponent has no consumer...");
    }
}
