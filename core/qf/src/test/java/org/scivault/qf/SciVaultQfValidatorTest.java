package org.scivault.qf;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;

class SciVaultQfValidatorTest {

    @Test
    void validatorLoadsSchema() throws Exception {
        Path schema = Path.of(
                "../../qf/schema/scivault-qf-0.1.schema.json"
        );

        SciVaultQfValidator validator =
                new SciVaultQfValidator(schema);

        assertNotNull(validator);
    }
}
