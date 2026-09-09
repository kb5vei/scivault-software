package org.scivault.qf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public final class QfPackageValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private QfPackageValidator() {
    }

    public static ValidationResult validate(Path path) {
        ValidationResult result = new ValidationResult();

        try (ZipFile zip = new ZipFile(path.toFile())) {

            Set<String> names = new HashSet<>();
            boolean hasManifest = false;

            for (ZipEntry entry : Collections.list(zip.entries())) {
                String name = entry.getName();

                if (!names.add(name)) {
                    result.addError(
                            "PACKAGE_DUPLICATE_ENTRY",
                            "Package contains duplicate entry: " + name,
                            name
                    );
                }

                if ("manifest.json".equals(name)) {
                    hasManifest = true;
                }
            }

            if (!hasManifest) {
                result.addError(
                        "PACKAGE_MISSING_MANIFEST",
                        "Package does not contain manifest.json.",
                        "/"
                );
                return result;
            }

            ZipEntry manifestEntry = zip.getEntry("manifest.json");

            QfPackageManifest manifest;

            try {
                manifest = MAPPER.readValue(
                        zip.getInputStream(manifestEntry),
                        QfPackageManifest.class
                );
            } catch (IOException e) {
                result.addError(
                        "PACKAGE_INVALID_MANIFEST",
                        "Package manifest could not be read.",
                        "manifest.json"
                );
                return result;
            }

            if (!"SciVault-QF".equals(manifest.getFormat())
                    || !"0.1".equals(manifest.getFormatVersion())
                    || manifest.getContent() == null
                    || manifest.getContent().isEmpty()) {

                result.addError(
                        "PACKAGE_INVALID_MANIFEST",
                        "Package manifest is invalid.",
                        "manifest.json"
                );
                return result;
            }

            String contentPath = manifest.getContent();

            if (!isSafePackagePath(contentPath)) {
                result.addError(
                        "PACKAGE_UNSAFE_PATH",
                        "Package contains unsafe path: " + contentPath,
                        "manifest.json"
                );
                return result;
            }

            ZipEntry contentEntry = zip.getEntry(contentPath);

            if (contentEntry == null) {
                result.addError(
                        "PACKAGE_MISSING_CONTENT",
                        "Package content document is missing: "
                                + contentPath,
                        contentPath
                );
                return result;
            }

            JsonNode document;

            try {
                document = MAPPER.readTree(
                        zip.getInputStream(contentEntry)
                );
            } catch (IOException e) {
                result.addError(
                        "PACKAGE_INVALID_CONTENT",
                        "Package content document could not be read.",
                        contentPath
                );
                return result;
            }

            Set<String> referencedResources = new HashSet<>();

            referencedResources.add("manifest.json");
            referencedResources.add(contentPath);

            validateMediaReferences(
                    document,
                    names,
                    referencedResources,
                    result
            );

            /*
             * Any ordinary file in the package that is neither the
             * manifest, content document, nor referenced media is
             * allowed, but produces a warning.
             */
            for (ZipEntry entry : Collections.list(zip.entries())) {

                if (entry.isDirectory()) {
                    continue;
                }

                String name = entry.getName();

                if (!referencedResources.contains(name)) {
                    result.addWarning(
                            "PACKAGE_UNREFERENCED_RESOURCE",
                            "Package contains unreferenced resource: "
                                    + name,
                            name
                    );
                }
            }

        } catch (IOException e) {
            result.addError(
                    "PACKAGE_INVALID_ZIP",
                    "Package could not be opened as a ZIP archive.",
                    "/"
            );
        }

        return result;
    }

    private static void validateMediaReferences(
            JsonNode document,
            Set<String> packageEntries,
            Set<String> referencedResources,
            ValidationResult result) {

        JsonNode questions = document.get("questions");

        if (questions == null || !questions.isArray()) {
            return;
        }

        for (JsonNode question : questions) {

            validateMediaArray(
                    question.get("media"),
                    packageEntries,
                    referencedResources,
                    result
            );

            JsonNode choices = question.get("choices");

            if (choices != null && choices.isArray()) {
                for (JsonNode choice : choices) {
                    validateMediaArray(
                            choice.get("media"),
                            packageEntries,
                            referencedResources,
                            result
                    );
                }
            }
        }
    }

    private static void validateMediaArray(
            JsonNode mediaArray,
            Set<String> packageEntries,
            Set<String> referencedResources,
            ValidationResult result) {

        if (mediaArray == null || !mediaArray.isArray()) {
            return;
        }

        for (JsonNode media : mediaArray) {

            JsonNode srcNode = media.get("src");

            if (srcNode == null || !srcNode.isTextual()) {
                continue;
            }

            String src = srcNode.asText();

            /*
             * External resources are not package resources.
             */
            if (isExternalUri(src)) {
                continue;
            }

            if (!isSafePackagePath(src)) {
                result.addError(
                        "PACKAGE_UNSAFE_PATH",
                        "Media reference contains unsafe path: " + src,
                        src
                );
                continue;
            }

            referencedResources.add(src);

            if (!packageEntries.contains(src)) {
                result.addError(
                        "PACKAGE_MISSING_MEDIA",
                        "Referenced media resource is missing: " + src,
                        src
                );
            }
        }
    }

    private static boolean isExternalUri(String src) {

        if (src == null) {
            return false;
        }

        return src.matches(
                "^[A-Za-z][A-Za-z0-9+.-]*://.*"
        );
    }

    private static boolean isSafePackagePath(String path) {

        if (path == null || path.isEmpty()) {
            return false;
        }

        if (path.contains("\\")) {
            return false;
        }

        if (path.startsWith("/")) {
            return false;
        }

        if (path.length() >= 2
                && Character.isLetter(path.charAt(0))
                && path.charAt(1) == ':') {
            return false;
        }

        String[] components = path.split("/");

        for (String component : components) {
            if (".".equals(component)
                    || "..".equals(component)) {
                return false;
            }
        }

        return true;
    }
}
