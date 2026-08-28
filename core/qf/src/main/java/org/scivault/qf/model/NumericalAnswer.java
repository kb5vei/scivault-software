package org.scivault.qf.model;

import java.math.BigDecimal;

public class NumericalAnswer {

    private BigDecimal value;
    private String expression;
    private BigDecimal tolerance;
    private String unit;

    public NumericalAnswer() {
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public BigDecimal getTolerance() {
        return tolerance;
    }

    public void setTolerance(BigDecimal tolerance) {
        this.tolerance = tolerance;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
