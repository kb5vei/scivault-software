package org.scivault.qf.model;

public class MultipleChoiceQuestion extends ChoiceQuestion {

    private String answer;

    public MultipleChoiceQuestion() {
    }

    @Override
    public String getType() {
        return "multiple_choice";
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
