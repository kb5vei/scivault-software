package org.scivault.qf;

import org.junit.jupiter.api.Test;
import org.scivault.qf.model.FillInTheBlankQuestion;
import org.scivault.qf.model.QuestionBank;
import org.scivault.qf.model.ShortAnswerQuestion;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class QfReaderSingleStringAnswerTest {

    @Test
    void readsShortAnswerWithSingleStringAnswer()
            throws Exception {

        String json =
                "{\n"
                + "  \"format\": \"SciVault-QF\",\n"
                + "  \"format_version\": \"0.1\",\n"
                + "  \"metadata\": {\n"
                + "    \"title\": \"Single String Answer Test\",\n"
                + "    \"license\": \"CC0\"\n"
                + "  },\n"
                + "  \"questions\": [\n"
                + "    {\n"
                + "      \"id\": \"q001\",\n"
                + "      \"type\": \"short_answer\",\n"
                + "      \"question\": "
                + "\"Who formulated the laws of motion?\",\n"
                + "      \"answer\": \"Newton\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n";

        Path file =
                Files.createTempFile(
                        "scivault-qf-single-answer-",
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

            assertTrue(
                    bank.getQuestions().get(0)
                            instanceof ShortAnswerQuestion
            );

            ShortAnswerQuestion question =
                    (ShortAnswerQuestion)
                            bank.getQuestions().get(0);

            assertNotNull(
                    question.getAnswer()
            );

            assertEquals(
                    1,
                    question.getAnswer().size()
            );

            assertEquals(
                    "Newton",
                    question.getAnswer().get(0)
            );

        } finally {

            Files.deleteIfExists(file);
        }
    }

    @Test
    void readsFillInTheBlankWithSingleStringAnswer()
            throws Exception {

        String json =
                "{\n"
                + "  \"format\": \"SciVault-QF\",\n"
                + "  \"format_version\": \"0.1\",\n"
                + "  \"metadata\": {\n"
                + "    \"title\": \"Single String Answer Test\",\n"
                + "    \"license\": \"CC0\"\n"
                + "  },\n"
                + "  \"questions\": [\n"
                + "    {\n"
                + "      \"id\": \"q001\",\n"
                + "      \"type\": \"fill_in_the_blank\",\n"
                + "      \"question\": "
                + "\"The SI unit of force is the ____.\",\n"
                + "      \"answer\": \"newton\"\n"
                + "    }\n"
                + "  ]\n"
                + "}\n";

        Path file =
                Files.createTempFile(
                        "scivault-qf-single-fill-answer-",
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

            assertTrue(
                    bank.getQuestions().get(0)
                            instanceof FillInTheBlankQuestion
            );

            FillInTheBlankQuestion question =
                    (FillInTheBlankQuestion)
                            bank.getQuestions().get(0);

            assertNotNull(
                    question.getAnswer()
            );

            assertEquals(
                    1,
                    question.getAnswer().size()
            );

            assertEquals(
                    "newton",
                    question.getAnswer().get(0)
            );

        } finally {

            Files.deleteIfExists(file);
        }
    }
}
