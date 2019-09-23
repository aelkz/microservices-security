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
        "camel",
        "camel-health-checks",
        "diskSpace"
})
public class Health implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("camel")
    private Camel camel;
    @JsonProperty("camel-health-checks")
    private CamelHealthChecks camelHealthChecks;
    @JsonProperty("diskSpace")
    private DiskSpace diskSpace;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -3965002318778296118L;

    public Health() { }

    /**
     * @param camelHealthChecks
     * @param camel
     * @param status
     * @param diskSpace
     */
    public Health(String status, Camel camel, CamelHealthChecks camelHealthChecks, DiskSpace diskSpace) {
        super();
        this.status = status;
        this.camel = camel;
        this.camelHealthChecks = camelHealthChecks;
        this.diskSpace = diskSpace;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("camel")
    public Camel getCamel() {
        return camel;
    }

    @JsonProperty("camel")
    public void setCamel(Camel camel) {
        this.camel = camel;
    }

    @JsonProperty("camel-health-checks")
    public CamelHealthChecks getCamelHealthChecks() {
        return camelHealthChecks;
    }

    @JsonProperty("camel-health-checks")
    public void setCamelHealthChecks(CamelHealthChecks camelHealthChecks) {
        this.camelHealthChecks = camelHealthChecks;
    }

    @JsonProperty("diskSpace")
    public DiskSpace getDiskSpace() {
        return diskSpace;
    }

    @JsonProperty("diskSpace")
    public void setDiskSpace(DiskSpace diskSpace) {
        this.diskSpace = diskSpace;
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
        return new ToStringBuilder(this).append("status", status).append("camel", camel).append("camelHealthChecks", camelHealthChecks).append("diskSpace", diskSpace).append("additionalProperties", additionalProperties).toString();
    }

}
