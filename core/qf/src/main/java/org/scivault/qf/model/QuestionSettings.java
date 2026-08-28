package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuestionSettings {

    @JsonProperty("shuffle_choices")
    private Boolean shuffleChoices;

    /*
     * Allows settings not explicitly modeled by the core library
     * to be preserved when a QF file is read and written.
     */
    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public QuestionSettings() {
    }

    public Boolean getShuffleChoices() {
        return shuffleChoices;
    }

    public void setShuffleChoices(Boolean shuffleChoices) {
        this.shuffleChoices = shuffleChoices;
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
