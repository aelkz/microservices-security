package com.microservices.apigateway.security.model;

import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;

public class Event extends BaseModel {

    @ApiModelProperty(readOnly=true, example="100")
    private Long id;

    @ApiModelProperty(readOnly=true, example="OK")
    private String status;

    public Event(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return id.equals(event.id) &&
                status.equals(event.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, status);
    }
}
