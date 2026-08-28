package org.scivault.qf.model;

public class NumericalQuestion extends Question {

    private NumericalAnswer answer;

    public NumericalQuestion() {
    }

    @Override
    public String getType() {
        return "numerical";
    }

    public NumericalAnswer getAnswer() {
        return answer;
    }

    public void setAnswer(NumericalAnswer answer) {
        this.answer = answer;
    }
}
