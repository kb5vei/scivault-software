package org.scivault.qf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final List<Finding> findings = new ArrayList<>();

    public void addFinding(Finding finding) {
        findings.add(finding);
    }

    public void addError(String code, String message, String path) {
        findings.add(new Finding(
                Finding.Severity.ERROR,
                code,
                message,
                path
        ));
    }

    public void addWarning(String code, String message, String path) {
        findings.add(new Finding(
                Finding.Severity.WARNING,
                code,
                message,
                path
        ));
    }

    public List<Finding> getFindings() {
        return Collections.unmodifiableList(findings);
    }

    public boolean isValid() {
        return findings.stream().noneMatch(Finding::isError);
    }

    public boolean hasErrors() {
        return findings.stream().anyMatch(Finding::isError);
    }

    public boolean hasWarnings() {
        return findings.stream().anyMatch(Finding::isWarning);
    }

    public long getErrorCount() {
        return findings.stream()
                .filter(Finding::isError)
                .count();
    }

    public long getWarningCount() {
        return findings.stream()
                .filter(Finding::isWarning)
                .count();
    }

    public boolean isEmpty() {
        return findings.isEmpty();
    }

    public void add(Finding finding) {
        findings.add(finding);
    }
}
