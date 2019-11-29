package com.microservices.apigateway.security.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@NamedQueries({
        @NamedQuery(name="extractSorted",
                query="select er " +
                        "from ExtractRecord er order by er.date desc")
})
public class ExtractRecord extends BaseModel {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name="id")
    private Long id;

    @Column(name="date")
    @NotNull
    private LocalDateTime date;

    @Column(name="description")
    @NotNull
    private String description;

    @Column(name="value")
    @DecimalMin("0.01")
    @Digits(integer = 9999999, fraction = 2)
    @NotNull
    private Double value;

    @Column(name="balance")
    @NotNull
    private Double balance;

    @Column(name="status")
    @Size(min = 1, max = 1)
    @Pattern(regexp = "^(?:W|D)$")
    @NotNull
    private String status;

    public ExtractRecord() { }

    public ExtractRecord(String status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtractRecord that = (ExtractRecord) o;
        return id.equals(that.id) &&
                date.equals(that.date) &&
                value.equals(that.value) &&
                status.equals(that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, value, status);
    }
}
