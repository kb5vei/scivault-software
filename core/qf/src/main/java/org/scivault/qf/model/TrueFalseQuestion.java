package org.scivault.qf.model;

public class TrueFalseQuestion extends ChoiceQuestion {

    private String answer;

    public TrueFalseQuestion() {
    }

    @Override
    public String getType() {
        return "true_false";
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
