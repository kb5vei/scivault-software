package org.scivault.qf.model;

import java.util.ArrayList;
import java.util.List;

public class ShortAnswerQuestion extends Question {

    private List<String> answer = new ArrayList<>();

    public ShortAnswerQuestion() {
    }

    @Override
    public String getType() {
        return "short_answer";
    }

    public List<String> getAnswer() {
        return answer;
    }

    public void setAnswer(List<String> answer) {
        this.answer = answer;
    }
}
