package org.scivault.qf;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ExpressionValidator {

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            "\\s*(" +
            "\\d+(?:\\.\\d+)?" +
            "|[A-Za-z_][A-Za-z0-9_]*" +
            "|[()+\\-*/^,]" +
            ")\\s*"
    );

    private static final Set<String> ALLOWED_FUNCTIONS = Set.of(
            "abs",
            "sqrt",
            "sin",
            "cos",
            "tan",
            "asin",
            "acos",
            "atan",
            "log",
            "ln",
            "exp",
            "min",
            "max"
    );

    private ExpressionValidator() {
    }

    public static Result validate(String expression) {
        if (expression == null || expression.isBlank()) {
            return Result.invalid("Expression is blank.");
        }

        Matcher matcher = TOKEN_PATTERN.matcher(expression);

        int position = 0;
        Set<String> variables = new LinkedHashSet<>();

        while (matcher.find()) {
            if (matcher.start() != position) {
                return Result.invalid(
                        "Invalid token near position " + position + "."
                );
            }

            String token = matcher.group(1);

            if (isIdentifier(token)
                    && !ALLOWED_FUNCTIONS.contains(token)) {
                variables.add(token);
            }

            position = matcher.end();
        }

        if (position != expression.length()) {
            return Result.invalid(
                    "Invalid token near position " + position + "."
            );
        }

        return Result.valid(variables);
    }

    public static boolean isValid(
            String expression,
            Set<String> allowedVariables) {

        Result result = validate(expression);

        if (!result.isValid()) {
            return false;
        }

        return allowedVariables.containsAll(result.getVariables());
    }

    private static boolean isIdentifier(String token) {
        return token.matches("[A-Za-z_][A-Za-z0-9_]*");
    }

    public static final class Result {

        private final boolean valid;
        private final String error;
        private final Set<String> variables;

        private Result(
                boolean valid,
                String error,
                Set<String> variables) {

            this.valid = valid;
            this.error = error;
            this.variables = variables;
        }

        public static Result valid(Set<String> variables) {
            return new Result(
                    true,
                    null,
                    Collections.unmodifiableSet(
                            new LinkedHashSet<>(variables)
                    )
            );
        }

        public static Result invalid(String error) {
            return new Result(
                    false,
                    error,
                    Collections.emptySet()
            );
        }

        public boolean isValid() {
            return valid;
        }

        public String getError() {
            return error;
        }

        public Set<String> getVariables() {
            return variables;
        }
    }
}
