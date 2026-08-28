package org.scivault.qf;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

class ExpressionValidatorTest {

    @Test
    void acceptsSimpleArithmetic() {
        assertTrue(ExpressionValidator.isValid(
                "a + b * 2",
                Set.of("a", "b")
        ));
    }

    @Test
    void acceptsAllowedFunctions() {
        assertTrue(ExpressionValidator.isValid(
                "sqrt(x^2 + y^2)",
                Set.of("x", "y")
        ));
    }

    @Test
    void rejectsUnknownVariables() {
        assertFalse(ExpressionValidator.isValid(
                "x + z",
                Set.of("x", "y")
        ));
    }

    @Test
    void rejectsUnknownFunctions() {
        assertFalse(ExpressionValidator.isValid(
                "evil(x)",
                Set.of("x")
        ));
    }

    @Test
    void rejectsInvalidCharacters() {
        assertFalse(ExpressionValidator.isValid(
                "x; System.exit(0)",
                Set.of("x")
        ));
    }

    @Test
    void rejectsBlankExpression() {
        assertFalse(ExpressionValidator.isValid(
                "   ",
                Set.of("x")
        ));
    }
}
