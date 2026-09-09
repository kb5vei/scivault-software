package org.scivault.qf;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class QfPackageValidatorTest {

    private static final Path PACKAGE_FIXTURES =
            Paths.get("../../qf/conformance/package");

    @Test
    void minimalPackageIsValid() {
        ValidationResult result = QfPackageValidator.validate(
                PACKAGE_FIXTURES.resolve("valid/minimal.sqf")
        );

        assertTrue(
                result.isValid(),
                () -> "Expected valid package, but got: "
                        + result.getFindings()
        );

        assertTrue(result.isEmpty());
    }

    @Test
    void missingManifestIsInvalid() {
        ValidationResult result = QfPackageValidator.validate(
                PACKAGE_FIXTURES.resolve(
                        "invalid/missing-manifest.sqf"
                )
        );

        assertFalse(result.isValid());

        assertHasFinding(
                result,
                "PACKAGE_MISSING_MANIFEST"
        );
    }

    @Test
    void invalidManifestIsInvalid() {
        ValidationResult result = QfPackageValidator.validate(
                PACKAGE_FIXTURES.resolve(
                        "invalid/invalid-manifest.sqf"
                )
        );

        assertFalse(result.isValid());

        assertHasFinding(
                result,
                "PACKAGE_INVALID_MANIFEST"
        );
    }
@Test
void unsafeContentPathIsInvalid() {
    ValidationResult result = QfPackageValidator.validate(
            PACKAGE_FIXTURES.resolve(
                    "invalid/unsafe-path.sqf"
            )
    );

    assertFalse(result.isValid());

    assertHasFinding(
            result,
            "PACKAGE_UNSAFE_PATH"
    );
}
@Test
void duplicateEntryIsInvalid() {
    ValidationResult result = QfPackageValidator.validate(
            PACKAGE_FIXTURES.resolve(
                    "invalid/duplicate-entry.sqf"
            )
    );

    assertFalse(result.isValid());

    assertHasFinding(
            result,
            "PACKAGE_DUPLICATE_ENTRY"
    );
}
@Test
void missingMediaIsInvalid() {
    ValidationResult result = QfPackageValidator.validate(
            PACKAGE_FIXTURES.resolve(
                    "invalid/missing-media.sqf"
            )
    );

    assertFalse(result.isValid());

    assertHasFinding(
            result,
            "PACKAGE_MISSING_MEDIA"
    );
}
@Test
void unreferencedResourceProducesWarning() {
    ValidationResult result = QfPackageValidator.validate(
            PACKAGE_FIXTURES.resolve(
                    "valid-with-warnings/unreferenced-resource.sqf"
            )
    );

    assertTrue(
            result.isValid(),
            () -> "Warnings should not make the package invalid: "
                    + result.getFindings()
    );

    assertTrue(result.hasWarnings());

    assertHasFinding(
            result,
            "PACKAGE_UNREFERENCED_RESOURCE"
    );
}
@Test
void packageWithMediaIsValid() {
    ValidationResult result = QfPackageValidator.validate(
            PACKAGE_FIXTURES.resolve(
                    "valid/with-media.sqf"
            )
    );

    assertTrue(
            result.isValid(),
            () -> "Expected valid package, but got: "
                    + result.getFindings()
    );

    assertTrue(result.isEmpty());
}
    @Test
    void missingContentIsInvalid() {
        ValidationResult result = QfPackageValidator.validate(
                PACKAGE_FIXTURES.resolve(
                        "invalid/missing-content.sqf"
                )
        );

        assertFalse(result.isValid());

        assertHasFinding(
                result,
                "PACKAGE_MISSING_CONTENT"
        );
    }

    private static void assertHasFinding(
            ValidationResult result,
            String code) {

        boolean found = result.getFindings()
                .stream()
                .anyMatch(finding ->
                        code.equals(finding.getCode()));

        assertTrue(
                found,
                () -> "Expected finding "
                        + code
                        + ", but got: "
                        + result.getFindings()
        );
    }
}
