package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Choice {

    private String id;
    private String text;

    /*
     * Allows choice properties not explicitly modeled by the
     * core library to be preserved when a QF file is read and written.
     */
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

    @JsonAnyGetter
    public Map<String, JsonNode> getAdditionalProperties() {
        return additionalProperties;
    }

    @JsonAnySetter
    public void putAdditionalProperty(String name, JsonNode value) {
        additionalProperties.put(name, value);
    }

    public void setAdditionalProperties(
            Map<String, JsonNode> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
