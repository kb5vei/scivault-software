package org.scivault.qf.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;


public class QuestionBank {

    private String format;
     @JsonProperty("format_version")
    private String formatVersion;
    private SetMetadata metadata;
    private List<Question> questions = new ArrayList<>();
    private Extensions extensions;

    public QuestionBank() {
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public SetMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(SetMetadata metadata) {
        this.metadata = metadata;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }
}
