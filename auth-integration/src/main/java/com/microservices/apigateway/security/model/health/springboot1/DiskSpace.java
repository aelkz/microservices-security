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
        "total",
        "free",
        "threshold"
})
public class DiskSpace implements Serializable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("total")
    private Long total;
    @JsonProperty("free")
    private Long free;
    @JsonProperty("threshold")
    private Long threshold;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 2794561675280930840L;

    public DiskSpace() { }

    /**
     * @param total
     * @param free
     * @param status
     * @param threshold
     */
    public DiskSpace(String status, Long total, Long free, Long threshold) {
        super();
        this.status = status;
        this.total = total;
        this.free = free;
        this.threshold = threshold;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("total")
    public Long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Long total) {
        this.total = total;
    }

    @JsonProperty("free")
    public Long getFree() {
        return free;
    }

    @JsonProperty("free")
    public void setFree(Long free) {
        this.free = free;
    }

    @JsonProperty("threshold")
    public Long getThreshold() {
        return threshold;
    }

    @JsonProperty("threshold")
    public void setThreshold(Long threshold) {
        this.threshold = threshold;
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
        return new ToStringBuilder(this).append("status", status).append("total", total).append("free", free).append("threshold", threshold).append("additionalProperties", additionalProperties).toString();
    }
}
