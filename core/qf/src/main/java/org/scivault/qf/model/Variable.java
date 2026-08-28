package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = IntegerVariable.class,
                name = "integer"
        ),
        @JsonSubTypes.Type(
                value = DecimalVariable.class,
                name = "decimal"
        ),
        @JsonSubTypes.Type(
                value = ChoiceVariable.class,
                name = "choice"
        )
})
public abstract class Variable {

    /*
     * Allows variable properties not explicitly modeled by the
     * core library to be preserved when a QF file is read and written.
     */
    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public Variable() {
    }

    public abstract String getType();

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
