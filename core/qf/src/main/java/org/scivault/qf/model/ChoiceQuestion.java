package org.scivault.qf.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ChoiceQuestion extends Question {

    private List<Choice> choices = new ArrayList<>();

    public ChoiceQuestion() {
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
