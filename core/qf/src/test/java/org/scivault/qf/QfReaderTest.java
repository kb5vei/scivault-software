package org.scivault.qf;

import org.junit.jupiter.api.Test;
import org.scivault.qf.model.MultipleChoiceQuestion;
import org.scivault.qf.model.QuestionBank;
import org.scivault.qf.model.MultipleSelectQuestion;
import org.scivault.qf.model.TrueFalseQuestion;
import org.scivault.qf.model.ShortAnswerQuestion;
import org.scivault.qf.model.FillInTheBlankQuestion;
import org.scivault.qf.model.NumericalQuestion;
import org.scivault.qf.model.IntegerVariable;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class QfReaderTest {

    private static final boolean VERBOSE =
            Boolean.getBoolean("qf.verbose");

    @Test
    void readsBasicMultipleChoiceFixture() throws Exception {

        Path fixture = Path.of(
                "../../qf/conformance/valid/basic-multiple-choice.json"
        );

        QuestionBank bank = QfReader.read(fixture);

        assertNotNull(bank);

        assertEquals("SciVault-QF", bank.getFormat());
        assertEquals("0.1", bank.getFormatVersion());

        assertNotNull(bank.getMetadata());
        assertEquals(
                "org.scivault.conformance.basic-multiple-choice",
                bank.getMetadata().getId()
        );
        assertEquals(
                "Basic Multiple-Choice Conformance Test",
                bank.getMetadata().getTitle()
        );
        assertEquals("SciVault", bank.getMetadata().getAuthor());
        assertEquals(
                "2026-08-19T12:28:00-06:00",
                bank.getMetadata().getCreated()
        );

        assertNotNull(bank.getQuestions());
        assertEquals(1, bank.getQuestions().size());

        assertInstanceOf(
                MultipleChoiceQuestion.class,
                bank.getQuestions().get(0)
        );

        MultipleChoiceQuestion question =
                (MultipleChoiceQuestion) bank.getQuestions().get(0);

        assertEquals("q001", question.getId());
        assertEquals("multiple_choice", question.getType());
        assertEquals("What is $2+2$?", question.getQuestion());
        assertEquals(0, question.getDifficulty());

        assertEquals(3, question.getChoices().size());

        assertEquals("A", question.getChoices().get(0).getId());
        assertEquals("3", question.getChoices().get(0).getText());

        assertEquals("B", question.getChoices().get(1).getId());
        assertEquals("4", question.getChoices().get(1).getText());

        assertEquals("C", question.getChoices().get(2).getId());
        assertEquals("5", question.getChoices().get(2).getText());

        assertEquals("B", question.getAnswer());

        assertNotNull(question.getSettings());
        assertEquals(
                Boolean.TRUE,
                question.getSettings().getShuffleChoices()
        );

        if (VERBOSE) {
            System.out.println();
            System.out.println(
                    "=== QfReader: basic-multiple-choice.json ==="
            );
            System.out.println("Format: " + bank.getFormat());
            System.out.println(
                    "Format version: " + bank.getFormatVersion()
            );

            System.out.println();
            System.out.println("Metadata:");
            System.out.println(
                    "  ID: " + bank.getMetadata().getId()
            );
            System.out.println(
                    "  Title: " + bank.getMetadata().getTitle()
            );
            System.out.println(
                    "  Author: " + bank.getMetadata().getAuthor()
            );
            System.out.println(
                    "  Created: " + bank.getMetadata().getCreated()
            );

            System.out.println();
            System.out.println(
                    "Questions: " + bank.getQuestions().size()
            );

            System.out.println();
            System.out.println("Question 1:");
            System.out.println(
                    "  Java class: "
                            + question.getClass().getSimpleName()
            );
            System.out.println("  ID: " + question.getId());
            System.out.println("  Type: " + question.getType());
            System.out.println(
                    "  Text: " + question.getQuestion()
            );
            System.out.println(
                    "  Difficulty: " + question.getDifficulty()
            );

            System.out.println("  Choices:");
            question.getChoices().forEach(choice ->
                    System.out.println(
                            "    "
                                    + choice.getId()
                                    + ": "
                                    + choice.getText()
                    )
            );

            System.out.println(
                    "  Answer: " + question.getAnswer()
            );

            System.out.println(
                    "  Shuffle choices: "
                            + question.getSettings()
                                      .getShuffleChoices()
            );
        }
    }
    @Test
void readsMixedQuestionTypesFixture() throws Exception {

    Path fixture = Path.of(
            "../../qf/conformance/valid/mixed-question-types.json"
    );

    QuestionBank bank = QfReader.read(fixture);

    assertNotNull(bank);
    assertEquals("SciVault-QF", bank.getFormat());
    assertEquals("0.1", bank.getFormatVersion());

    assertNotNull(bank.getQuestions());
    assertEquals(5, bank.getQuestions().size());

    /*
     * Question 1: multiple select
     */
    assertInstanceOf(
            MultipleSelectQuestion.class,
            bank.getQuestions().get(0)
    );

    MultipleSelectQuestion q1 =
            (MultipleSelectQuestion) bank.getQuestions().get(0);

    assertEquals("q001", q1.getId());
    assertEquals("multiple_select", q1.getType());
    assertEquals(4, q1.getChoices().size());
    assertEquals(2, q1.getAnswer().size());
    assertEquals("A", q1.getAnswer().get(0));
    assertEquals("C", q1.getAnswer().get(1));

    /*
     * Question 2: true/false
     */
    assertInstanceOf(
            TrueFalseQuestion.class,
            bank.getQuestions().get(1)
    );

    TrueFalseQuestion q2 =
            (TrueFalseQuestion) bank.getQuestions().get(1);

    assertEquals("q002", q2.getId());
    assertEquals("true_false", q2.getType());
    assertEquals(2, q2.getChoices().size());
    assertEquals("true", q2.getAnswer());

    /*
     * Question 3: short answer
     */
    assertInstanceOf(
            ShortAnswerQuestion.class,
            bank.getQuestions().get(2)
    );

    ShortAnswerQuestion q3 =
            (ShortAnswerQuestion) bank.getQuestions().get(2);

    assertEquals("q003", q3.getId());
    assertEquals("short_answer", q3.getType());
    assertEquals(3, q3.getAnswer().size());
    assertEquals("newton", q3.getAnswer().get(0));
    assertEquals("Newton", q3.getAnswer().get(1));
    assertEquals("N", q3.getAnswer().get(2));

    /*
     * Question 4: fill in the blank
     */
    assertInstanceOf(
            FillInTheBlankQuestion.class,
            bank.getQuestions().get(3)
    );

    FillInTheBlankQuestion q4 =
            (FillInTheBlankQuestion) bank.getQuestions().get(3);

    assertEquals("q004", q4.getId());
    assertEquals("fill_in_the_blank", q4.getType());
    assertEquals(4, q4.getAnswer().size());
    assertEquals("ampere", q4.getAnswer().get(0));
    assertEquals("Ampere", q4.getAnswer().get(1));
    assertEquals("amp", q4.getAnswer().get(2));
    assertEquals("A", q4.getAnswer().get(3));

    /*
     * Question 5: numerical
     */
    assertInstanceOf(
            NumericalQuestion.class,
            bank.getQuestions().get(4)
    );

    NumericalQuestion q5 =
            (NumericalQuestion) bank.getQuestions().get(4);

    assertEquals("q005", q5.getId());
    assertEquals("numerical", q5.getType());
    assertNotNull(q5.getAnswer());

    if (VERBOSE) {
        System.out.println();
        System.out.println(
                "=== QfReader: mixed-question-types.json ==="
        );
        System.out.println(
                "Questions: " + bank.getQuestions().size()
        );

        System.out.println();
        System.out.println("Question 1:");
        System.out.println(
                "  Java class: " + q1.getClass().getSimpleName()
        );
        System.out.println("  Type: " + q1.getType());
        System.out.println("  Answer: " + q1.getAnswer());

        System.out.println();
        System.out.println("Question 2:");
        System.out.println(
                "  Java class: " + q2.getClass().getSimpleName()
        );
        System.out.println("  Type: " + q2.getType());
        System.out.println("  Answer: " + q2.getAnswer());

        System.out.println();
        System.out.println("Question 3:");
        System.out.println(
                "  Java class: " + q3.getClass().getSimpleName()
        );
        System.out.println("  Type: " + q3.getType());
        System.out.println("  Answers: " + q3.getAnswer());

        System.out.println();
        System.out.println("Question 4:");
        System.out.println(
                "  Java class: " + q4.getClass().getSimpleName()
        );
        System.out.println("  Type: " + q4.getType());
        System.out.println("  Answers: " + q4.getAnswer());

        System.out.println();
        System.out.println("Question 5:");
        System.out.println(
                "  Java class: " + q5.getClass().getSimpleName()
        );
        System.out.println("  Type: " + q5.getType());
        System.out.println(
                "  Value: " + q5.getAnswer().getValue()
        );
        System.out.println(
                "  Tolerance: " + q5.getAnswer().getTolerance()
        );
        System.out.println(
                "  Unit: " + q5.getAnswer().getUnit()
        );
    }
}
	@Test
void readsDynamicAndMediaFixture() throws Exception {

    Path fixture = Path.of(
            "../../qf/conformance/valid/dynamic-and-media.json"
    );

    QuestionBank bank = QfReader.read(fixture);

    assertNotNull(bank);
    assertEquals("SciVault-QF", bank.getFormat());
    assertEquals("0.1", bank.getFormatVersion());

    assertNotNull(bank.getQuestions());
    assertEquals(2, bank.getQuestions().size());

    /*
     * Question 1: dynamic numerical question
     */
    assertInstanceOf(
            NumericalQuestion.class,
            bank.getQuestions().get(0)
    );

    NumericalQuestion q1 =
            (NumericalQuestion) bank.getQuestions().get(0);

    assertEquals("q001", q1.getId());
    assertEquals("numerical", q1.getType());

    assertNotNull(q1.getVariables());
    assertEquals(2, q1.getVariables().size());

    assertInstanceOf(
            IntegerVariable.class,
            q1.getVariables().get("a")
    );

    assertInstanceOf(
            IntegerVariable.class,
            q1.getVariables().get("t")
    );

    IntegerVariable a =
            (IntegerVariable) q1.getVariables().get("a");

    IntegerVariable t =
            (IntegerVariable) q1.getVariables().get("t");

    assertEquals(1L, a.getMin());
    assertEquals(5L, a.getMax());
    assertEquals(1L, a.getStep());

    assertEquals(2L, t.getMin());
    assertEquals(10L, t.getMax());
    assertEquals(1L, t.getStep());

    assertNotNull(q1.getAnswer());
    assertEquals("a * t", q1.getAnswer().getExpression());
    assertEquals(
            0,
            q1.getAnswer()
              .getTolerance()
              .compareTo(new java.math.BigDecimal("0.01"))
    );
    assertEquals("m/s", q1.getAnswer().getUnit());

    /*
     * Question 2: media and extensions
     */
    assertInstanceOf(
            MultipleChoiceQuestion.class,
            bank.getQuestions().get(1)
    );

    MultipleChoiceQuestion q2 =
            (MultipleChoiceQuestion) bank.getQuestions().get(1);

    assertEquals("q002", q2.getId());
    assertEquals("multiple_choice", q2.getType());

    assertNotNull(q2.getMedia());
    assertEquals(1, q2.getMedia().size());

    assertEquals(
            "prompt-diagram",
            q2.getMedia().get(0).getId()
    );

    assertEquals(
            "image",
            q2.getMedia().get(0).getType()
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

    if (VERBOSE) {
        System.out.println();
        System.out.println(
                "=== QfReader: dynamic-and-media.json ==="
        );

        System.out.println();
        System.out.println("Question 1:");
        System.out.println(
                "  Java class: "
                        + q1.getClass().getSimpleName()
        );
        System.out.println(
                "  Variables: " + q1.getVariables().keySet()
        );
        System.out.println(
                "  a: min=" + a.getMin()
                        + ", max=" + a.getMax()
                        + ", step=" + a.getStep()
        );
        System.out.println(
                "  t: min=" + t.getMin()
                        + ", max=" + t.getMax()
                        + ", step=" + t.getStep()
        );
        System.out.println(
                "  Expression: "
                        + q1.getAnswer().getExpression()
        );
        System.out.println(
                "  Tolerance: "
                        + q1.getAnswer().getTolerance()
        );
        System.out.println(
                "  Unit: "
                        + q1.getAnswer().getUnit()
        );

        System.out.println();
        System.out.println("Question 2:");
        System.out.println(
                "  Java class: "
                        + q2.getClass().getSimpleName()
        );
        System.out.println(
                "  Media ID: "
                        + q2.getMedia().get(0).getId()
        );
        System.out.println(
                "  Media type: "
                        + q2.getMedia().get(0).getType()
        );
        System.out.println(
                "  Media src: "
                        + q2.getMedia().get(0).getSrc()
        );
        System.out.println(
                "  Extensions: "
                        + q2.getExtensions().getValues()
        );
    }
}
}
