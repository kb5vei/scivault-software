package org.scivault.qf;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scivault.qf.model.QuestionBank;

import java.io.IOException;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class QfPackageReader {

    private static final ObjectMapper MAPPER =
            new ObjectMapper();

    private QfPackageReader() {
    }

    public static QuestionBank read(Path path)
            throws IOException {

        try (ZipFile zip = new ZipFile(path.toFile())) {

            ZipEntry manifestEntry =
                    zip.getEntry("manifest.json");

            if (manifestEntry == null) {
                throw new IOException(
                        "Package does not contain manifest.json."
                );
            }

            QfPackageManifest manifest =
                    MAPPER.readValue(
                            zip.getInputStream(manifestEntry),
                            QfPackageManifest.class
                    );

            String contentPath =
                    manifest.getContent();

            if (contentPath == null
                    || contentPath.isEmpty()) {

                throw new IOException(
                        "Package manifest does not specify content."
                );
            }

            ZipEntry contentEntry =
                    zip.getEntry(contentPath);

            if (contentEntry == null) {
                throw new IOException(
                        "Package content is missing: "
                                + contentPath
                );
            }

            return MAPPER.readValue(
                    zip.getInputStream(contentEntry),
                    QuestionBank.class
            );
        }
    }

    public static byte[] readResource(
            Path packagePath,
            String resourcePath)
            throws IOException {

        if (!QfPackagePath.isSafe(resourcePath)) {
            throw new IOException(
                    "Unsafe package resource path: "
                            + resourcePath
            );
        }

        try (ZipFile zip =
                     new ZipFile(packagePath.toFile())) {

            ZipEntry resourceEntry =
                    zip.getEntry(resourcePath);

            if (resourceEntry == null
                    || resourceEntry.isDirectory()) {

                throw new IOException(
                        "Package resource is missing: "
                                + resourcePath
                );
            }

            try (java.io.InputStream input =
                         zip.getInputStream(resourceEntry)) {

                return input.readAllBytes();
            }
        }
    }
}
