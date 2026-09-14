package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.List;

public class FillInTheBlankQuestion extends Question {

    @JsonFormat(
            with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY
    )
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
