package org.scivault.qf;

import org.junit.jupiter.api.Test;
import org.scivault.qf.model.MultipleChoiceQuestion;
import org.scivault.qf.model.QuestionBank;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class QfPackageReaderTest {

    private static final Path PACKAGE_ROOT =
            findPackageRoot();

    @Test
void readsPackageWithMediaReference() throws Exception {

    Path packagePath =
            PACKAGE_ROOT.resolve(
                    "valid/with-media.sqf"
            );

    QuestionBank bank =
            QfPackageReader.read(packagePath);

    assertNotNull(bank);
    assertEquals(1, bank.getQuestions().size());

    MultipleChoiceQuestion question =
            (MultipleChoiceQuestion)
                    bank.getQuestions().get(0);

    assertNotNull(question.getMedia());
    assertEquals(1, question.getMedia().size());

    assertEquals(
            "media/reference-diagram.svg",
            question.getMedia().get(0).getSrc()
    );
}
@Test
void rejectsUnsafeResourcePath() {

    Path packagePath =
            PACKAGE_ROOT.resolve(
                    "valid/minimal.sqf"
            );

    IOException exception =
            assertThrows(
                    IOException.class,
                    () -> QfPackageReader.readResource(
                            packagePath,
                            "../questions.json"
                    )
            );

    assertTrue(
            exception.getMessage()
                    .contains("Unsafe package resource path")
    );
}
@Test
void readsBundledMediaResource() throws Exception {

    Path packagePath =
            PACKAGE_ROOT.resolve(
                    "valid/with-media.sqf"
            );

    byte[] data =
            QfPackageReader.readResource(
                    packagePath,
                    "media/reference-diagram.svg"
            );

    assertNotNull(data);
    assertTrue(data.length > 0);

    String svg = new String(
            data,
            java.nio.charset.StandardCharsets.UTF_8
    );

    assertTrue(svg.contains("<svg"));
}
    @Test
    void readsMinimalPackage() throws Exception {

        Path packagePath =
                PACKAGE_ROOT.resolve(
                        "valid/minimal.sqf"
                );

        QuestionBank bank =
                QfPackageReader.read(packagePath);

        assertNotNull(bank);

        assertEquals(
                "SciVault-QF",
                bank.getFormat()
        );

        assertEquals(
                "0.1",
                bank.getFormatVersion()
        );

        assertNotNull(bank.getQuestions());

        assertEquals(
                1,
                bank.getQuestions().size()
        );

        assertTrue(
                bank.getQuestions().get(0)
                        instanceof MultipleChoiceQuestion
        );

        MultipleChoiceQuestion question =
                (MultipleChoiceQuestion)
                        bank.getQuestions().get(0);

        assertEquals(
                "q001",
                question.getId()
        );

        assertEquals(
                "What is $2+2$?",
                question.getQuestion()
        );

        assertEquals(
                "B",
                question.getAnswer()
        );

        assertEquals(
                3,
                question.getChoices().size()
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
