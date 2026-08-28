package org.scivault.qf;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class SciVaultQfConformanceSmokeTest {

    private final Path schema = Path.of(
            "../../qf/schema/scivault-qf-0.1.schema.json"
    );

    @Test
    void acceptsKnownValidFixture() throws Exception {
        SciVaultQfValidator validator =
                new SciVaultQfValidator(schema);

        ValidationResult result = validator.validate(
                Path.of("../../qf/conformance/valid/basic-multiple-choice.json")
        );

        assertTrue(
                result.isValid(),
                () -> "Expected valid file, but got: " + result.getFindings()
        );
    }

    @Test
    void rejectsKnownInvalidFixture() throws Exception {
        SciVaultQfValidator validator =
                new SciVaultQfValidator(schema);

        ValidationResult result = validator.validate(
                Path.of("../../qf/conformance/invalid/missing-blank.json")
        );

        assertFalse(
                result.isValid(),
                "Expected invalid fixture to fail validation."
        );
    }
}
