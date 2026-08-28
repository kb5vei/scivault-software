package org.scivault.qf;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.scivault.qf.model.QuestionBank;

import java.io.IOException;
import java.nio.file.Path;

public final class QfReader {

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .configure(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                    false
            );

    private QfReader() {
    }

    public static QuestionBank read(Path path) throws IOException {
        return MAPPER.readValue(
                path.toFile(),
                QuestionBank.class
        );
    }
}
