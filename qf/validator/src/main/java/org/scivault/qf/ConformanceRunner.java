package org.scivault.qf;

import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public final class ConformanceRunner {
  private static final ObjectMapper M=new ObjectMapper();
  public static final class CaseResult {final String file;final boolean passed,expectedValid,actualValid;final Set<String> expected,actual;CaseResult(String f,boolean p,boolean ev,boolean av,Set<String>e,Set<String>a){file=f;passed=p;expectedValid=ev;actualValid=av;expected=e;actual=a;}public String getFile(){return file;}public boolean isPassed(){return passed;}public boolean isExpectedValid(){return expectedValid;}public boolean isActualValid(){return actualValid;}public Set<String> getExpected(){return expected;}public Set<String> getActual(){return actual;}}
  public static List<CaseResult> run(Path manifest,Path schema)throws IOException{JsonNode root=M.readTree(manifest.toFile());Path base=manifest.toAbsolutePath().getParent();SciVaultQfValidator v=new SciVaultQfValidator(schema);List<CaseResult> out=new ArrayList<>();for(JsonNode t:root.path("tests")){String f=t.path("file").asText();boolean ev=t.path("valid").asBoolean();Set<String> expected=new HashSet<>();for(JsonNode x:t.path("expected_findings"))expected.add(x.path("severity").asText()+":"+x.path("code").asText());ValidationResult vr=v.validate(base.resolve(f));Set<String> actual=new HashSet<>();for(Finding x:vr.getFindings())actual.add(x.getSeverity()+":"+x.getCode());boolean findings=ev&&expected.isEmpty()?actual.isEmpty():actual.containsAll(expected);out.add(new CaseResult(f,vr.isValid()==ev&&findings,ev,vr.isValid(),expected,actual));}return out;}
}
