package org.scivault.qf;

import org.junit.jupiter.api.Test;
import org.scivault.qf.model.IntegerVariable;
import org.scivault.qf.model.MultipleChoiceQuestion;
import org.scivault.qf.model.MultipleSelectQuestion;
import org.scivault.qf.model.NumericalQuestion;
import org.scivault.qf.model.QuestionBank;
import org.scivault.qf.model.ShortAnswerQuestion;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class QfWriterTest {

    private static final boolean VERBOSE =
            Boolean.getBoolean("qf.verbose");

    @Test
    void writesAndReadsBasicMultipleChoiceFixture() throws Exception {

        Path fixture = Path.of(
                "../../qf/conformance/valid/basic-multiple-choice.json"
        );

        QuestionBank original = QfReader.read(fixture);

        Path tempFile = Files.createTempFile(
                "scivault-qf-writer-",
                ".json"
        );

        try {
            QfWriter.write(original, tempFile);

            assertTrue(Files.exists(tempFile));
            assertTrue(Files.size(tempFile) > 0);

            if (VERBOSE) {
                printOutput(
                        "basic-multiple-choice.json",
                        tempFile
                );
            }

            QuestionBank roundTrip = QfReader.read(tempFile);

            assertNotNull(roundTrip);

            assertEquals(
                    original.getFormat(),
                    roundTrip.getFormat()
            );

            assertEquals(
                    original.getFormatVersion(),
                    roundTrip.getFormatVersion()
            );

            assertNotNull(roundTrip.getMetadata());

            assertEquals(
                    original.getMetadata().getId(),
                    roundTrip.getMetadata().getId()
            );

            assertEquals(
                    original.getMetadata().getTitle(),
                    roundTrip.getMetadata().getTitle()
            );

            assertEquals(1, roundTrip.getQuestions().size());

            assertInstanceOf(
                    MultipleChoiceQuestion.class,
                    roundTrip.getQuestions().get(0)
            );

            MultipleChoiceQuestion question =
                    (MultipleChoiceQuestion)
                            roundTrip.getQuestions().get(0);

            assertEquals("q001", question.getId());
            assertEquals(
                    "multiple_choice",
                    question.getType()
            );
            assertEquals(
                    "What is $2+2$?",
                    question.getQuestion()
            );

            assertEquals(3, question.getChoices().size());
            assertEquals("B", question.getAnswer());

            assertNotNull(question.getSettings());
            assertEquals(
                    Boolean.TRUE,
                    question.getSettings().getShuffleChoices()
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void preservesUnknownPropertiesThroughRoundTrip() throws Exception {

        String json =
                "{\n" +
                "  \"format\": \"SciVault-QF\",\n" +
                "  \"format_version\": \"0.1\",\n" +
                "  \"metadata\": {\n" +
                "    \"id\": \"org.scivault.test.writer-unknown-properties\",\n" +
                "    \"title\": \"Writer Unknown Property Test\",\n" +
                "    \"future_metadata_field\": \"preserve me\"\n" +
                "  },\n" +
                "  \"questions\": [\n" +
                "    {\n" +
                "      \"id\": \"q001\",\n" +
                "      \"type\": \"multiple_choice\",\n" +
                "      \"question\": \"Test question\",\n" +
                "      \"choices\": [\n" +
                "        {\n" +
                "          \"id\": \"A\",\n" +
                "          \"text\": \"Answer A\",\n" +
                "          \"future_choice_field\": \"choice data\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"id\": \"B\",\n" +
                "          \"text\": \"Answer B\"\n" +
                "        }\n" +
                "      ],\n" +
                "      \"answer\": \"A\",\n" +
                "      \"settings\": {\n" +
                "        \"shuffle_choices\": true,\n" +
                "        \"future_setting\": \"setting data\"\n" +
                "      },\n" +
                "      \"future_question_field\": 42\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        Path inputFile = Files.createTempFile(
                "scivault-qf-writer-input-",
                ".json"
        );

        Path outputFile = Files.createTempFile(
                "scivault-qf-writer-output-",
                ".json"
        );

        try {
            Files.writeString(inputFile, json);

            QuestionBank firstRead = QfReader.read(inputFile);

            QfWriter.write(firstRead, outputFile);

            if (VERBOSE) {
                printOutput(
                        "unknown property round trip",
                        outputFile
                );
            }

            QuestionBank secondRead = QfReader.read(outputFile);

            assertEquals(
                    "preserve me",
                    secondRead.getMetadata()
                              .getAdditionalProperties()
                              .get("future_metadata_field")
                              .asText()
            );

            MultipleChoiceQuestion question =
                    (MultipleChoiceQuestion)
                            secondRead.getQuestions().get(0);

            assertEquals(
                    42,
                    question.getAdditionalProperties()
                            .get("future_question_field")
                            .asInt()
            );

            assertEquals(
                    "setting data",
                    question.getSettings()
                            .getAdditionalProperties()
                            .get("future_setting")
                            .asText()
            );

            assertEquals(
                    "choice data",
                    question.getChoices()
                            .get(0)
                            .getAdditionalProperties()
                            .get("future_choice_field")
                            .asText()
            );

        } finally {
            Files.deleteIfExists(inputFile);
            Files.deleteIfExists(outputFile);
        }
    }

    @Test
    void writesAndReadsMixedQuestionTypesFixture() throws Exception {

        Path fixture = Path.of(
                "../../qf/conformance/valid/mixed-question-types.json"
        );

        Path tempFile = Files.createTempFile(
                "scivault-qf-writer-mixed-",
                ".json"
        );

        try {
            QuestionBank original = QfReader.read(fixture);

            QfWriter.write(original, tempFile);

            assertTrue(Files.size(tempFile) > 0);

            if (VERBOSE) {
                printOutput(
                        "mixed-question-types.json",
                        tempFile
                );
            }

            QuestionBank roundTrip = QfReader.read(tempFile);

            assertEquals(5, roundTrip.getQuestions().size());

            assertInstanceOf(
                    MultipleSelectQuestion.class,
                    roundTrip.getQuestions().get(0)
            );

            MultipleSelectQuestion q1 =
                    (MultipleSelectQuestion)
                            roundTrip.getQuestions().get(0);

            assertEquals(2, q1.getAnswer().size());
            assertEquals("A", q1.getAnswer().get(0));
            assertEquals("C", q1.getAnswer().get(1));

            assertInstanceOf(
                    ShortAnswerQuestion.class,
                    roundTrip.getQuestions().get(2)
            );

            ShortAnswerQuestion q3 =
                    (ShortAnswerQuestion)
                            roundTrip.getQuestions().get(2);

            assertEquals(3, q3.getAnswer().size());
            assertEquals("newton", q3.getAnswer().get(0));

            assertInstanceOf(
                    NumericalQuestion.class,
                    roundTrip.getQuestions().get(4)
            );

            NumericalQuestion q5 =
                    (NumericalQuestion)
                            roundTrip.getQuestions().get(4);

            assertNotNull(q5.getAnswer());

            assertEquals(
                    "m/s",
                    q5.getAnswer().getUnit()
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    void writesAndReadsDynamicAndMediaFixture() throws Exception {

        Path fixture = Path.of(
                "../../qf/conformance/valid/dynamic-and-media.json"
        );

        Path tempFile = Files.createTempFile(
                "scivault-qf-writer-dynamic-",
                ".json"
        );

        try {
            QuestionBank original = QfReader.read(fixture);

            QfWriter.write(original, tempFile);

            assertTrue(Files.size(tempFile) > 0);

            if (VERBOSE) {
                printOutput(
                        "dynamic-and-media.json",
                        tempFile
                );
            }

            QuestionBank roundTrip = QfReader.read(tempFile);

            assertEquals(2, roundTrip.getQuestions().size());

            /*
             * Dynamic numerical question
             */
            assertInstanceOf(
                    NumericalQuestion.class,
                    roundTrip.getQuestions().get(0)
            );

            NumericalQuestion q1 =
                    (NumericalQuestion)
                            roundTrip.getQuestions().get(0);

            assertEquals(2, q1.getVariables().size());

            assertInstanceOf(
                    IntegerVariable.class,
                    q1.getVariables().get("a")
            );

            IntegerVariable a =
                    (IntegerVariable)
                            q1.getVariables().get("a");

            assertEquals(1L, a.getMin());
            assertEquals(5L, a.getMax());
            assertEquals(1L, a.getStep());

            assertEquals(
                    "a * t",
                    q1.getAnswer().getExpression()
            );

            assertEquals(
                    "m/s",
                    q1.getAnswer().getUnit()
            );

            /*
             * Media and extensions question
             */
            assertInstanceOf(
                    MultipleChoiceQuestion.class,
                    roundTrip.getQuestions().get(1)
            );

            MultipleChoiceQuestion q2 =
                    (MultipleChoiceQuestion)
                            roundTrip.getQuestions().get(1);

            assertNotNull(q2.getMedia());
            assertEquals(1, q2.getMedia().size());

            assertEquals(
                    "prompt-diagram",
                    q2.getMedia().get(0).getId()
            );

            assertEquals(
                    "media/reference-diagram.svg",
                    q2.getMedia().get(0).getSrc()
            );

            assertNotNull(q2.getExtensions());

            assertTrue(
                    q2.getExtensions().contains(
                            "org.scivault.peer_instruction"
                    )
            );

            assertEquals(
                    120,
                    q2.getExtensions()
                      .get("org.scivault.peer_instruction")
                      .get("recommended_discussion_time")
                      .asInt()
            );

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    /*
     * Print generated QF JSON when -Dqf.verbose=true is used.
     */
    private static void printOutput(
            String name,
            Path file
    ) throws Exception {

        System.out.println();
        System.out.println(
                "=== QfWriter: " + name + " ==="
        );
        System.out.println(
                "Temporary file: " + file.toAbsolutePath()
        );
        System.out.println();
        System.out.println(Files.readString(file));
    }
}
