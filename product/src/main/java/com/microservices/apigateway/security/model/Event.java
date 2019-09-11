package com.microservices.apigateway.security.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Event extends BaseModel{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="status", nullable = true)
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
