package org.scivault.qf;

import org.junit.jupiter.api.Test;
import org.scivault.qf.model.ChoiceVariable;
import org.scivault.qf.model.QuestionBank;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class QfReaderChoiceVariableTest {

    @Test
    void readsChoiceVariableValues()
            throws Exception {

        String json =
                "{\n"
                + "  \"format\": \"SciVault-QF\",\n"
                + "  \"format_version\": \"0.1\",\n"
                + "  \"metadata\": {\n"
                + "    \"title\": \"Choice Variable Test\",\n"
                + "    \"license\": \"CC0\"\n"
                + "  },\n"
                + "  \"questions\": [\n"
                + "    {\n"
                + "      \"id\": \"q001\",\n"
                + "      \"type\": \"short_answer\",\n"
                + "      \"question\": \"What color was selected?\",\n"
                + "      \"answer\": \"red\",\n"
                + "      \"variables\": {\n"
                + "        \"color\": {\n"
                + "          \"type\": \"choice\",\n"
                + "          \"values\": [\n"
                + "            \"red\",\n"
                + "            \"green\",\n"
                + "            \"blue\"\n"
                + "          ]\n"
                + "        }\n"
                + "      }\n"
                + "    }\n"
                + "  ]\n"
                + "}\n";

        Path file =
                Files.createTempFile(
                        "scivault-qf-choice-variable-",
                        ".json"
                );

        try {

            Files.writeString(
                    file,
                    json
            );

            QuestionBank bank =
                    QfReader.read(file);

            assertNotNull(bank);

            assertEquals(
                    1,
                    bank.getQuestions().size()
            );

            assertNotNull(
                    bank.getQuestions().get(0)
                            .getVariables()
            );

            assertTrue(
                    bank.getQuestions().get(0)
                            .getVariables()
                            .get("color")
                            instanceof ChoiceVariable
            );

            ChoiceVariable variable =
                    (ChoiceVariable)
                            bank.getQuestions().get(0)
                                    .getVariables()
                                    .get("color");

            assertNotNull(
                    variable.getValues()
            );

            assertEquals(
                    3,
                    variable.getValues().size()
            );

            assertEquals(
                    "red",
                    variable.getValues()
                            .get(0)
                            .asText()
            );

            assertEquals(
                    "green",
                    variable.getValues()
                            .get(1)
                            .asText()
            );

            assertEquals(
                    "blue",
                    variable.getValues()
                            .get(2)
                            .asText()
            );

        } finally {

            Files.deleteIfExists(file);
        }
    }
}
