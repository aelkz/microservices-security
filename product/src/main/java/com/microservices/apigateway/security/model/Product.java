package com.microservices.apigateway.security.model;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name="products",
                query="select p " +
                        "from Product p ")
})
public class Product extends BaseModel {

    protected Product() { }

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 250, message = "Name must have between 3 and 250 characters.")
    @Column(name="name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 3, max = 250, message = "Description must have between 3 and 250 characters.")
    @Column(name="description", nullable = false)
    private String description;

    @NotNull
    @Size(min = 5, max = 10, message = "Code must have between 5 and 10 characters.")
    @Column(name="code", nullable = false)
    private String code;

    @Column(name="price", nullable = false)
    @Digits(integer=3,fraction=2)
    private Double price;

    @Column(name="active", nullable = false)
    private Boolean active;

    @Column(name="created", nullable = false)
    private LocalDateTime created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id) &&
                name.equals(product.name) &&
                Objects.equals(description, product.description) &&
                code.equals(product.code) &&
                Objects.equals(price, product.price) &&
                active.equals(product.active) &&
                created.equals(product.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, code, price, active, created);
    }
}
