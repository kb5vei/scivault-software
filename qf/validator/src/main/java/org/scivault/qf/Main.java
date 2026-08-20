package org.scivault.qf;

import com.fasterxml.jackson.databind.*;
import java.nio.file.Path;
import java.util.*;

public final class Main {
  private static final ObjectMapper M=new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
  public static void main(String[] a)throws Exception{if(a.length<1){usage();System.exit(2);}if("validate".equals(a[0]))validate(a);else if("conformance".equals(a[0]))conformance(a);else{usage();System.exit(2);}}
  private static void validate(String[]a)throws Exception{if(a.length<4||!"--schema".equals(a[2])){usage();System.exit(2);}ValidationResult r=new SciVaultQfValidator(Path.of(a[3])).validate(Path.of(a[1]));boolean json=a.length>4&&"--json".equals(a[4]);if(json)System.out.println(M.writeValueAsString(r.toMap()));else{System.out.println(r.isValid()?"VALID":"INVALID");for(Finding f:r.getFindings())System.out.println(f.getSeverity().toUpperCase()+": "+f.getCode()+(f.getQuestionId()!=null?" ("+f.getQuestionId()+")":"")+(f.getPath()!=null?" ["+f.getPath()+"]":"")+": "+f.getMessage());}System.exit(r.isValid()?0:1);}
  private static void conformance(String[]a)throws Exception{if(a.length!=4||!"--schema".equals(a[2])){usage();System.exit(2);}List<ConformanceRunner.CaseResult> rs=ConformanceRunner.run(Path.of(a[1]),Path.of(a[3]));int fail=0;for(ConformanceRunner.CaseResult r:rs){System.out.println((r.isPassed()?"PASS: ":"FAIL: ")+r.getFile());if(!r.isPassed()){fail++;System.out.println("  expected valid: "+r.isExpectedValid());System.out.println("  actual valid:   "+r.isActualValid());System.out.println("  expected:       "+r.getExpected());System.out.println("  actual:         "+r.getActual());}}System.out.println();System.out.println((rs.size()-fail)+"/"+rs.size()+" conformance tests passed.");System.exit(fail==0?0:1);}
  private static void usage(){System.err.println("SciVault-QF Validator 0.1");System.err.println("  validate FILE --schema SCHEMA [--json]");System.err.println("  conformance MANIFEST --schema SCHEMA");}
}
