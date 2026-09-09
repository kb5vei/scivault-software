package org.scivault.qf;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QfPackageManifest {

    private String format;

    @JsonProperty("format_version")
    private String formatVersion;

    private String content;

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormatVersion() {
        return formatVersion;
    }

    public void setFormatVersion(String formatVersion) {
        this.formatVersion = formatVersion;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
