package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Extensions {

    private final Map<String, JsonNode> values =
            new LinkedHashMap<>();

    public Extensions() {
    }

    @JsonAnyGetter
    public Map<String, JsonNode> getValues() {
        return values;
    }

    @JsonAnySetter
    public void put(String key, JsonNode value) {
        values.put(key, value);
    }

    public JsonNode get(String key) {
        return values.get(key);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
