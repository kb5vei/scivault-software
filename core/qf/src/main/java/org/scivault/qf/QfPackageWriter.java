package org.scivault.qf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.scivault.qf.model.QuestionBank;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public final class QfPackageWriter {

    private static final ObjectMapper MAPPER =
            new ObjectMapper()
                    .setSerializationInclusion(
                            JsonInclude.Include.NON_NULL
                    )
                    .enable(
                            SerializationFeature.INDENT_OUTPUT
                    );

    private QfPackageWriter() {
    }

    public static void write(
            QuestionBank bank,
            Path packagePath)
            throws IOException {

        write(
                bank,
                packagePath,
                Map.of()
        );
    }

    public static void write(
            QuestionBank bank,
            Path packagePath,
            Map<String, Path> resources)
            throws IOException {

        QfPackageManifest manifest =
                new QfPackageManifest();

        manifest.setFormat("SciVault-QF");
        manifest.setFormatVersion("0.1");
        manifest.setContent("questions.json");

        /*
         * Validate resource paths before creating the package.
         * This avoids leaving a partially written package when
         * a bad resource path is supplied.
         */
        for (Map.Entry<String, Path> resource
                : resources.entrySet()) {

            String resourcePath =
                    resource.getKey();

            Path sourcePath =
                    resource.getValue();

            if (!QfPackagePath.isSafe(resourcePath)) {
                throw new IOException(
                        "Unsafe package resource path: "
                                + resourcePath
                );
            }

            if ("manifest.json".equals(resourcePath)
                    || "questions.json".equals(resourcePath)) {

                throw new IOException(
                        "Resource path conflicts with "
                                + "reserved package entry: "
                                + resourcePath
                );
            }

            if (sourcePath == null
                    || !Files.isRegularFile(sourcePath)) {

                throw new IOException(
                        "Resource file does not exist "
                                + "or is not a regular file: "
                                + sourcePath
                );
            }
        }

        try (OutputStream output =
                     Files.newOutputStream(packagePath);
             ZipOutputStream zip =
                     new ZipOutputStream(output)) {

            writeJsonEntry(
                    zip,
                    "manifest.json",
                    manifest
            );

            writeJsonEntry(
                    zip,
                    "questions.json",
                    bank
            );

            for (Map.Entry<String, Path> resource
                    : resources.entrySet()) {

                writeFileEntry(
                        zip,
                        resource.getKey(),
                        resource.getValue()
                );
            }
        }
    }

    private static void writeJsonEntry(
            ZipOutputStream zip,
            String entryName,
            Object value)
            throws IOException {

        byte[] data =
                MAPPER.writeValueAsBytes(value);

        ZipEntry entry =
                new ZipEntry(entryName);

        zip.putNextEntry(entry);
        zip.write(data);
        zip.closeEntry();
    }

    private static void writeFileEntry(
            ZipOutputStream zip,
            String entryName,
            Path sourcePath)
            throws IOException {

        ZipEntry entry =
                new ZipEntry(entryName);

        zip.putNextEntry(entry);

        Files.copy(
                sourcePath,
                zip
        );

        zip.closeEntry();
    }
}
