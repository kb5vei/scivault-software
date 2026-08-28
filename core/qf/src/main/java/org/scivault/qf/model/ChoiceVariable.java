package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class ChoiceVariable extends Variable {

    private List<JsonNode> choices = new ArrayList<>();

    public ChoiceVariable() {
    }

    @Override
    public String getType() {
        return "choice";
    }

    public List<JsonNode> getChoices() {
        return choices;
    }

    public void setChoices(List<JsonNode> choices) {
        this.choices = choices;
    }
}
