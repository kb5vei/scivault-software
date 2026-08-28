package org.scivault.qf.model;

import java.util.ArrayList;
import java.util.List;

public class FillInTheBlankQuestion extends Question {

    private List<String> answer = new ArrayList<>();

    public FillInTheBlankQuestion() {
    }

    @Override
    public String getType() {
        return "fill_in_the_blank";
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
