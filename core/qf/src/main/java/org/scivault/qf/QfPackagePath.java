package org.scivault.qf;

/**
 * Utility methods for working with paths inside
 * SciVault-QF package files.
 */
final class QfPackagePath {

    private QfPackagePath() {
    }

    /**
     * Returns true if the supplied path is safe for use
     * as a package-root-relative path inside an SQF archive.
     */
    static boolean isSafe(String path) {

        if (path == null || path.isEmpty()) {
            return false;
        }

        // Package paths always use forward slashes.
        if (path.contains("\\")) {
            return false;
        }

        // Package paths must be relative.
        if (path.startsWith("/")) {
            return false;
        }

        // Reject Windows-style absolute paths such as C:/foo.
        if (path.length() >= 2
                && Character.isLetter(path.charAt(0))
                && path.charAt(1) == ':') {

            return false;
        }

        // Reject current-directory and parent-directory
        // path components.
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
