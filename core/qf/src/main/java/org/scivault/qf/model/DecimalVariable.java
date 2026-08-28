package org.scivault.qf.model;

import java.math.BigDecimal;

public class DecimalVariable extends Variable {

    private BigDecimal min;
    private BigDecimal max;
    private BigDecimal step;

    public DecimalVariable() {
    }

    @Override
    public String getType() {
        return "decimal";
    }

    public BigDecimal getMin() {
        return min;
    }

    public void setMin(BigDecimal min) {
        this.min = min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public void setMax(BigDecimal max) {
        this.max = max;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }
}
