package com.zaruba.entity;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

import static com.zaruba.util.Constants.EXPRESSION_VALUE;
import static com.zaruba.util.Constants.RESULT;


@Entity
public class Expression {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank(message = "Expression cant be empty")
    @Column(name = EXPRESSION_VALUE, nullable = false)
    private String value;

    @NotNull
    @Column(name = RESULT, nullable = false)
    private Double result;

    public Expression() {
    }

    public Expression(String value, Double result) {
        this.value = value;
        this.result = result;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Expression { ").append(" ")
                .append("id = ").append(id).append(" ")
                .append("value = ").append(value).append(" ")
                .append("result = ").append(result).append(" ").append("}").append("\n").toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Expression that = (Expression) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(value, that.value) &&
                Objects.equals(result, that.result);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, result);
    }


}
