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
        "name",
        "version",
        "contextStatus"
})
public class Camel implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("name")
    private String name;
    @JsonProperty("version")
    private String version;
    @JsonProperty("contextStatus")
    private String contextStatus;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -6354519934154815232L;

    public Camel() { }

    /**
     * @param status
     * @param name
     * @param contextStatus
     * @param version
     */
    public Camel(String status, String name, String version, String contextStatus) {
        super();
        this.status = status;
        this.name = name;
        this.version = version;
        this.contextStatus = contextStatus;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("version")
    public String getVersion() {
        return version;
    }

    @JsonProperty("version")
    public void setVersion(String version) {
        this.version = version;
    }

    @JsonProperty("contextStatus")
    public String getContextStatus() {
        return contextStatus;
    }

    @JsonProperty("contextStatus")
    public void setContextStatus(String contextStatus) {
        this.contextStatus = contextStatus;
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
        return new ToStringBuilder(this).append("status", status).append("name", name).append("version", version).append("contextStatus", contextStatus).append("additionalProperties", additionalProperties).toString();
    }
}
