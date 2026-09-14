package org.scivault.qf;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;
import org.scivault.qf.model.Media;
import org.scivault.qf.model.Question;
import org.scivault.qf.model.QuestionBank;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class QfReaderOfficialFieldsTest {

    @Test
    void readsOfficialFieldsExplicitly()
            throws Exception {

        String json =
                "{\n"
                + "  \"format\": \"SciVault-QF\",\n"
                + "  \"format_version\": \"0.1\",\n"
                + "  \"metadata\": {\n"
                + "    \"title\": \"Official Fields Test\",\n"
                + "    \"license\": \"CC0\",\n"
                + "    \"created\": \"2026-09-14T12:00:00Z\",\n"
                + "    \"modified\": \"2026-09-14T13:00:00Z\"\n"
                + "  },\n"
                + "  \"questions\": [\n"
                + "    {\n"
                + "      \"id\": \"q001\",\n"
                + "      \"type\": \"short_answer\",\n"
                + "      \"question\": \"What is the SI unit of force?\",\n"
                + "      \"answer\": \"newton\",\n"
                + "      \"hint\": \"Think of Newton's second law.\",\n"
                + "      \"metadata\": {\n"
                + "        \"source\": \"teacher-created\",\n"
                + "        \"reviewed\": true\n"
                + "      },\n"
                + "      \"media\": [\n"
                + "        {\n"
                + "          \"type\": \"image\",\n"
                + "          \"src\": \"media/newton.png\",\n"
                + "          \"alt\": \"Portrait of Isaac Newton\",\n"
                + "          \"credit\": \"Example credit\"\n"
                + "        }\n"
                + "      ]\n"
                + "    }\n"
                + "  ]\n"
                + "}\n";

        Path file =
                Files.createTempFile(
                        "scivault-qf-official-fields-",
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
                    "2026-09-14T13:00:00Z",
                    bank.getMetadata().getModified()
            );

            Question question =
                    bank.getQuestions().get(0);

            assertEquals(
                    "Think of Newton's second law.",
                    question.getHint()
            );

            Map<String, JsonNode> metadata =
                    question.getMetadata();

            assertNotNull(metadata);

            assertEquals(
                    "teacher-created",
                    metadata.get("source").asText()
            );

            assertTrue(
                    metadata.get("reviewed").asBoolean()
            );

            assertEquals(
                    1,
                    question.getMedia().size()
            );

            Media media =
                    question.getMedia().get(0);

            assertEquals(
                    "Example credit",
                    media.getCredit()
            );

        } finally {

            Files.deleteIfExists(file);
        }
    }
}
