package org.scivault.qf.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type",
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(
                value = MultipleChoiceQuestion.class,
                name = "multiple_choice"
        ),
        @JsonSubTypes.Type(
                value = MultipleSelectQuestion.class,
                name = "multiple_select"
        ),
        @JsonSubTypes.Type(
                value = TrueFalseQuestion.class,
                name = "true_false"
        ),
        @JsonSubTypes.Type(
                value = ShortAnswerQuestion.class,
                name = "short_answer"
        ),
        @JsonSubTypes.Type(
                value = FillInTheBlankQuestion.class,
                name = "fill_in_the_blank"
        ),
        @JsonSubTypes.Type(
                value = NumericalQuestion.class,
                name = "numerical"
        )
})
public abstract class Question {

    private String id;
    private String question;
    private String explanation;
    private Integer difficulty;

    private List<String> tags = new ArrayList<>();
    private List<String> objectives = new ArrayList<>();
    private List<Media> media = new ArrayList<>();

    private Map<String, Variable> variables = new LinkedHashMap<>();

    private QuestionSettings settings;
    private Extensions extensions;

    /*
     * Allows question properties not explicitly modeled by the
     * core library to be preserved when a QF file is read and written.
     */
    private Map<String, JsonNode> additionalProperties =
            new LinkedHashMap<>();

    public Question() {
    }

    public abstract String getType();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Integer difficulty) {
        this.difficulty = difficulty;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<String> objectives) {
        this.objectives = objectives;
    }

    public List<Media> getMedia() {
        return media;
    }

    public void setMedia(List<Media> media) {
        this.media = media;
    }

    public Map<String, Variable> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, Variable> variables) {
        this.variables = variables;
    }

    public QuestionSettings getSettings() {
        return settings;
    }

    public void setSettings(QuestionSettings settings) {
        this.settings = settings;
    }

    public Extensions getExtensions() {
        return extensions;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
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
