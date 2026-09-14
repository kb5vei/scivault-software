package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;

public class ChoiceVariable extends Variable {

    private List<JsonNode> values = new ArrayList<>();

    public ChoiceVariable() {
    }

    @Override
    public String getType() {
        return "choice";
    }

    public List<JsonNode> getValues() {
        return values;
    }

    public void setValues(List<JsonNode> values) {
        this.values = values;
    }
}
