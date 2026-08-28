package org.scivault.qf.model;

import java.util.ArrayList;
import java.util.List;

public class MultipleSelectQuestion extends ChoiceQuestion {

    private List<String> answer = new ArrayList<>();

    public MultipleSelectQuestion() {
    }

    @Override
    public String getType() {
        return "multiple_select";
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
