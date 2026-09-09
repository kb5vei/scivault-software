package org.scivault.qf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class QfPackageConformanceTest {

    private static final boolean VERBOSE =
            Boolean.getBoolean("qf.verbose");

    private static final Path PACKAGE_ROOT =
            findPackageRoot();

    private static final Path MANIFEST =
            PACKAGE_ROOT.resolve("manifest.json");

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    @Test
    void packageConformanceSuite() throws IOException {

        JsonNode manifest = MAPPER.readTree(
                MANIFEST.toFile()
        );

        JsonNode tests = manifest.get("tests");

        assertNotNull(
                tests,
                "Package conformance manifest has no tests field."
        );

        assertTrue(
                tests.isArray(),
                "Package conformance manifest tests field is not an array."
        );

        int passed = 0;
        int total = tests.size();

        for (JsonNode test : tests) {

            String file = test.get("file").asText();
            String expected = test.get("expected").asText();

            String expectedFinding =
                    test.has("finding")
                            ? test.get("finding").asText()
                            : null;

            Path packagePath =
                    PACKAGE_ROOT.resolve(file);

            ValidationResult result =
                    QfPackageValidator.validate(packagePath);

            boolean success;

            switch (expected) {

                case "valid":
                    success =
                            result.isValid()
                                    && !result.hasWarnings();
                    break;

                case "valid-with-warnings":
                    success =
                            result.isValid()
                                    && result.hasWarnings()
                                    && hasFinding(
                                            result,
                                            expectedFinding
                                    );
                    break;

                case "invalid":
                    success =
                            result.hasErrors()
                                    && hasFinding(
                                            result,
                                            expectedFinding
                                    );
                    break;

                default:
                    fail(
                            "Unknown expected result '"
                                    + expected
                                    + "' for "
                                    + file
                    );

                    return;
            }

            if (VERBOSE) {

                if (success) {
                    System.out.println(
                            "PASS " + file
                    );
                } else {

                    System.out.println(
                            "FAIL " + file
                    );

                    System.out.println(
                            "  expected: "
                                    + expected
                                    + (
                                    expectedFinding == null
                                            ? ""
                                            : " " + expectedFinding
                                    )
                    );

                    System.out.println(
                            "  actual:   "
                                    + result.getFindings()
                    );
                }
            }

            assertTrue(
                    success,
                    () -> "Package conformance failure for "
                            + file
                            + ". Expected: "
                            + expected
                            + (
                            expectedFinding == null
                                    ? ""
                                    : " " + expectedFinding
                            )
                            + ". Actual findings: "
                            + result.getFindings()
            );

            passed++;
        }

        if (VERBOSE) {

            System.out.println();

            System.out.println(
                    passed
                            + "/"
                            + total
                            + " package conformance cases passed"
            );
        }
    }

    private static boolean hasFinding(
            ValidationResult result,
            String code) {

        if (code == null) {
            return false;
        }

        return result.getFindings()
                .stream()
                .anyMatch(
                        finding ->
                                code.equals(
                                        finding.getCode()
                                )
                );
    }

    private static Path findPackageRoot() {

        Path current = Paths.get("")
                .toAbsolutePath()
                .normalize();

        while (current != null) {

            Path candidate = current.resolve(
                    "qf/conformance/package"
            );

            if (candidate.resolve("manifest.json")
                    .toFile()
                    .exists()) {

                return candidate;
            }

            current = current.getParent();
        }

        throw new IllegalStateException(
                "Could not locate "
                        + "qf/conformance/package/manifest.json"
        );
    }
}
