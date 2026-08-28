package org.scivault.qf.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.LinkedHashMap;
import java.util.Map;

public class Media {
    private String id;
    private String src;
    private String type;
    private String alt;
    private String caption;
    private String license;

    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public Media() {
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Map<String, JsonNode> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(
            Map<String, JsonNode> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }
    public String getId() {
    return id;
}

public void setId(String id) {
    this.id = id;
}
}
