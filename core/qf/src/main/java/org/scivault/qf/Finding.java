package org.scivault.qf;

import java.util.Objects;

public class Finding {

    public enum Severity {
        ERROR,
        WARNING
    }

    private final Severity severity;
    private final String code;
    private final String message;
    private final String questionId;
    private final String path;

    public Finding(
            Severity severity,
            String code,
            String message,
            String questionId,
            String path) {

        this.severity = Objects.requireNonNull(severity, "severity");
        this.code = Objects.requireNonNull(code, "code");
        this.message = Objects.requireNonNull(message, "message");
        this.questionId = questionId;
        this.path = path;
    }
    public Finding(
        Severity severity,
        String code,
        String message,
        String path) {

    this(severity, code, message, null, path);
}

    public static Finding of(
            String severity,
            String code,
            String message,
            String questionId,
            String path) {

        Severity parsedSeverity =
                "error".equalsIgnoreCase(severity)
                        ? Severity.ERROR
                        : Severity.WARNING;

        return new Finding(
                parsedSeverity,
                code,
                message,
                questionId,
                path
        );
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getQuestionId() {
        return questionId;
    }

    public String getPath() {
        return path;
    }

    public boolean isError() {
        return severity == Severity.ERROR;
    }

    public boolean isWarning() {
        return severity == Severity.WARNING;
    }

    @Override
    public String toString() {
        String location =
                (path == null || path.isBlank())
                        ? ""
                        : " [" + path + "]";

        return severity + " " + code + location + ": " + message;
    }
    
}
