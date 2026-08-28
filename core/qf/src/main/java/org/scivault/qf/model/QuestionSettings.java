package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class QuestionSettings {
	@JsonProperty("shuffle_choices")
	private Boolean shuffleChoices;
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

    public Map<String, JsonNode> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(
            Map<String, JsonNode> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
