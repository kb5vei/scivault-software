package org.scivault.qf;

import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;

import org.junit.jupiter.api.Test;

class ConformanceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void allConformanceFixturesMatchExpectedResults() throws Exception {

        Path schema = Path.of(
                "../../qf/schema/scivault-qf-0.1.schema.json"
        );

        Path manifestPath = Path.of(
                "../../qf/conformance/manifest.json"
        );

        SciVaultQfValidator validator =
                new SciVaultQfValidator(schema);

        JsonNode manifest = MAPPER.readTree(
                Files.readString(manifestPath)
        );

        JsonNode tests = manifest.get("tests");

        assertNotNull(tests, "manifest.json must contain a tests array.");
        assertTrue(tests.isArray(), "manifest tests must be an array.");

        int passed = 0;
        int total = 0;

        Iterator<JsonNode> iterator = tests.elements();

        while (iterator.hasNext()) {

            JsonNode test = iterator.next();
            total++;

            String file = test.get("file").asText();
            boolean expectedValid =
                    test.get("valid").asBoolean();

            Path fixture = manifestPath
                    .getParent()
                    .resolve(file)
                    .normalize();

            ValidationResult result =
                    validator.validate(fixture);

            assertEquals(
                    expectedValid,
                    result.isValid(),
                    () ->
                            "Conformance failure for "
                            + file
                            + "\nFindings: "
                            + result.getFindings()
            );

            passed++;
        }

        System.out.println(
                "SciVault-QF conformance: "
                + passed
                + "/"
                + total
                + " passed."
        );
    }
}
