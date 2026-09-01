package org.scivault.qf;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.scivault.qf.model.QuestionBank;

import java.io.IOException;
import java.nio.file.Path;

public final class QfWriter {

    private static final ObjectMapper MAPPER =
            new ObjectMapper()
                    .setSerializationInclusion(
                            JsonInclude.Include.NON_NULL
                    )
                    .enable(SerializationFeature.INDENT_OUTPUT);

    private QfWriter() {
    }

    public static void write(
            QuestionBank bank,
            Path path
    ) throws IOException {
        MAPPER.writeValue(path.toFile(), bank);
    }
}
