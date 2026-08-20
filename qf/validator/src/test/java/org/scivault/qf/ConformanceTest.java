package org.scivault.qf;

import org.junit.jupiter.api.Test;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class ConformanceTest {
  @Test void suitePassesWhenRepositoryLayoutIsPresent() throws Exception {
    Path qf=Path.of("").toAbsolutePath().getParent(); if(qf==null)return;
    Path manifest=qf.resolve("conformance/manifest.json"), schema=qf.resolve("schema/scivault-qf-0.1.schema.json");
    if(!Files.exists(manifest)||!Files.exists(schema))return;
    for(ConformanceRunner.CaseResult r:ConformanceRunner.run(manifest,schema)) assertTrue(r.isPassed(),()->"Conformance failed: "+r.getFile()+" expected="+r.getExpected()+" actual="+r.getActual());
  }
}
