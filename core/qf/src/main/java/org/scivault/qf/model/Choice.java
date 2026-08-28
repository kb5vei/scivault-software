package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Choice {

    private String id;
    private String text;

    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public Choice() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Map<String, JsonNode> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(
            Map<String, JsonNode> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
