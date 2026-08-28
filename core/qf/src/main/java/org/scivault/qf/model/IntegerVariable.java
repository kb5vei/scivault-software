package org.scivault.qf.model;

public class IntegerVariable extends Variable {

    private Long min;
    private Long max;
    private Long step;

    public IntegerVariable() {
    }

    @Override
    public String getType() {
        return "integer";
    }

    public Long getMin() {
        return min;
    }

    public void setMin(Long min) {
        this.min = min;
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    public Long getStep() {
        return step;
    }

    public void setStep(Long step) {
        this.step = step;
    }
}
