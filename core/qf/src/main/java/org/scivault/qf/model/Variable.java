package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

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

    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public Variable() {
    }

    public abstract String getType();

    public Map<String, JsonNode> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(
            Map<String, JsonNode> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
}
