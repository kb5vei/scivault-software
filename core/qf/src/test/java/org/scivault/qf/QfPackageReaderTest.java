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
void writerRejectsUnsafeResourcePath() throws Exception {

    Path sourcePackage =
            PACKAGE_ROOT.resolve(
                    "valid/minimal.sqf"
            );

    QuestionBank bank =
            QfPackageReader.read(sourcePackage);

    Path resource =
            java.nio.file.Files.createTempFile(
                    "scivault-resource-",
                    ".svg"
            );

    Path output =
            java.nio.file.Files.createTempFile(
                    "scivault-package-",
                    ".sqf"
            );

    try {

        java.util.Map<String, Path> resources =
                java.util.Map.of(
                        "../evil.svg",
                        resource
                );

        IOException exception =
                assertThrows(
                        IOException.class,
                        () -> QfPackageWriter.write(
                                bank,
                                output,
                                resources
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains(
                                "Unsafe package resource path"
                        )
        );

    } finally {

        java.nio.file.Files.deleteIfExists(
                resource
        );

        java.nio.file.Files.deleteIfExists(
                output
        );
    }
}
@Test
void writerRejectsReservedResourcePath() throws Exception {

    Path sourcePackage =
            PACKAGE_ROOT.resolve(
                    "valid/minimal.sqf"
            );

    QuestionBank bank =
            QfPackageReader.read(sourcePackage);

    Path resource =
            java.nio.file.Files.createTempFile(
                    "scivault-resource-",
                    ".json"
            );

    Path output =
            java.nio.file.Files.createTempFile(
                    "scivault-package-",
                    ".sqf"
            );

    try {

        java.util.Map<String, Path> resources =
                java.util.Map.of(
                        "questions.json",
                        resource
                );

        IOException exception =
                assertThrows(
                        IOException.class,
                        () -> QfPackageWriter.write(
                                bank,
                                output,
                                resources
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains(
                                "reserved package entry"
                        )
        );

    } finally {

        java.nio.file.Files.deleteIfExists(
                resource
        );

        java.nio.file.Files.deleteIfExists(
                output
        );
    }
}
@Test
void writesAndReadsMinimalPackage() throws Exception {

    Path sourcePath =
            PACKAGE_ROOT.resolve(
                    "valid/minimal.sqf"
            );

    QuestionBank original =
            QfPackageReader.read(sourcePath);

    Path output =
            java.nio.file.Files.createTempFile(
                    "scivault-qf-test-",
                    ".sqf"
            );

    try {

        QfPackageWriter.write(
                original,
                output
        );

        ValidationResult validation =
                QfPackageValidator.validate(output);

        assertTrue(
                validation.isValid(),
                () -> "Written package should be valid: "
                        + validation.getFindings()
        );

        QuestionBank restored =
                QfPackageReader.read(output);

        assertEquals(
                original.getFormat(),
                restored.getFormat()
        );

        assertEquals(
                original.getFormatVersion(),
                restored.getFormatVersion()
        );

        assertEquals(
                original.getQuestions().size(),
                restored.getQuestions().size()
        );

        assertEquals(
                original.getQuestions().get(0).getId(),
                restored.getQuestions().get(0).getId()
        );

    } finally {

        java.nio.file.Files.deleteIfExists(
                output
        );
    }
}
@Test
void writesAndReadsPackageWithMedia() throws Exception {

    Path sourcePackage =
            PACKAGE_ROOT.resolve(
                    "valid/with-media.sqf"
            );

    QuestionBank bank =
            QfPackageReader.read(sourcePackage);

    byte[] originalMedia =
            QfPackageReader.readResource(
                    sourcePackage,
                    "media/reference-diagram.svg"
            );

    Path tempDirectory =
            java.nio.file.Files.createTempDirectory(
                    "scivault-qf-media-test-"
            );

    Path mediaFile =
            tempDirectory.resolve(
                    "reference-diagram.svg"
            );

    Path outputPackage =
            tempDirectory.resolve(
                    "output.sqf"
            );

    try {

        java.nio.file.Files.write(
                mediaFile,
                originalMedia
        );

        java.util.Map<String, Path> resources =
                java.util.Map.of(
                        "media/reference-diagram.svg",
                        mediaFile
                );

        QfPackageWriter.write(
                bank,
                outputPackage,
                resources
        );

        ValidationResult validation =
                QfPackageValidator.validate(
                        outputPackage
                );

        assertTrue(
                validation.isValid(),
                () -> "Written package should be valid: "
                        + validation.getFindings()
        );

        assertFalse(
                validation.hasWarnings(),
                () -> "Written package should have no warnings: "
                        + validation.getFindings()
        );

        QuestionBank restored =
                QfPackageReader.read(
                        outputPackage
                );

        assertEquals(
                bank.getQuestions().size(),
                restored.getQuestions().size()
        );

        byte[] restoredMedia =
                QfPackageReader.readResource(
                        outputPackage,
                        "media/reference-diagram.svg"
                );

        assertArrayEquals(
                originalMedia,
                restoredMedia
        );

    } finally {

        java.nio.file.Files.deleteIfExists(
                outputPackage
        );

        java.nio.file.Files.deleteIfExists(
                mediaFile
        );

        java.nio.file.Files.deleteIfExists(
                tempDirectory
        );
    }
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
