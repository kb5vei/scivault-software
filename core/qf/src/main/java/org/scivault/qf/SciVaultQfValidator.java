package org.scivault.qf;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.Error;
import com.networknt.schema.InputFormat;
import com.networknt.schema.Schema;
import com.networknt.schema.SchemaRegistry;
import com.networknt.schema.SpecificationVersion;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public final class SciVaultQfValidator {
  private static final ObjectMapper MAPPER=new ObjectMapper();
  private static final Pattern PH=Pattern.compile("\\{\\{([A-Za-z_][A-Za-z0-9_]*)\\}\\}");
  private final Schema schema;

  public SciVaultQfValidator(Path schemaPath)throws IOException{
    String schemaText=Files.readString(schemaPath);
    SchemaRegistry registry=SchemaRegistry.withDefaultDialect(SpecificationVersion.DRAFT_2020_12);
    schema=registry.getSchema(schemaText, InputFormat.JSON); schema.initializeValidators();
  }

  public ValidationResult validate(Path input){ ValidationResult r=new ValidationResult(); String text; JsonNode doc;
    try{text=Files.readString(input);doc=MAPPER.readTree(text);}catch(Exception e){r.add(Finding.of("error","JSON_PARSE_ERROR",String.valueOf(e.getMessage()),null,"$"));return r;}
    List<Error> errors=schema.validate(text,InputFormat.JSON,c->c.executionConfig(ec->ec.formatAssertionsEnabled(true)));
    for(Error e:errors){String path=e.getInstanceLocation()==null?"$":"$"+e.getInstanceLocation();r.add(Finding.of("error","SCHEMA_VALIDATION_ERROR",e.getMessage(),null,path));}
    semantic(doc,input,r); return r;
  }

  private void semantic(JsonNode doc,Path source,ValidationResult r){ JsonNode md=doc.path("metadata"); if(md.isObject()){if(!md.has("id"))r.add(Finding.of("warning","MISSING_SET_ID","Question set does not define metadata.id.",null,"$.metadata.id")); if(!md.has("version"))r.add(Finding.of("warning","MISSING_SET_VERSION","Question set does not define metadata.version.",null,"$.metadata.version"));}
    JsonNode qs=doc.path("questions"); if(!qs.isArray())return; Set<String> seen=new HashSet<>();
    for(int i=0;i<qs.size();i++){JsonNode q=qs.get(i);String id=text(q.get("id"));if(id!=null&&!seen.add(id))r.add(Finding.of("error","DUPLICATE_QUESTION_ID","Question ID '"+id+"' is duplicated.",id,"$.questions["+i+"].id"));}
    for(int i=0;i<qs.size();i++) if(qs.get(i).isObject()) question(qs.get(i),i,source,r);
  }

  private void question(JsonNode q,int i,Path source,ValidationResult r){String qp="$.questions["+i+"]", id=text(q.get("id")), type=text(q.get("type"));
    if(!q.has("explanation"))r.add(Finding.of("warning","MISSING_EXPLANATION","Question does not define an explanation.",id,qp+".explanation"));
    if(!q.has("difficulty"))r.add(Finding.of("warning","MISSING_DIFFICULTY","Question does not define a difficulty value.",id,qp+".difficulty"));
    if(!nonempty(q.get("tags")))r.add(Finding.of("warning","MISSING_TAGS","Question does not define any tags.",id,qp+".tags"));
    if(!nonempty(q.get("objectives")))r.add(Finding.of("warning","MISSING_OBJECTIVES","Question does not define any learning objectives.",id,qp+".objectives"));

    JsonNode choices=q.get("choices"); Set<String> ids=new LinkedHashSet<>(); Map<String,Integer> texts=new HashMap<>();
    if(choices!=null&&choices.isArray())for(int j=0;j<choices.size();j++){JsonNode c=choices.get(j);String cid=text(c.get("id"));if(cid!=null&&!ids.add(cid))r.add(Finding.of("error","DUPLICATE_CHOICE_ID","Choice ID '"+cid+"' is duplicated.",id,qp+".choices["+j+"].id"));String ct=text(c.get("text"));if(ct!=null){ct=ct.trim();if(texts.containsKey(ct))r.add(Finding.of("warning","DUPLICATE_CHOICE_TEXT","Choice text '"+ct+"' is duplicated.",id,qp+".choices["+j+"].text"));else texts.put(ct,j);}}
    if("multiple_choice".equals(type)){String a=text(q.get("answer"));if(a!=null&&!ids.contains(a))r.add(Finding.of("error","ANSWER_UNKNOWN_CHOICE","Answer '"+a+"' does not reference an existing choice.",id,qp+".answer"));}
    else if("multiple_select".equals(type)){JsonNode a=q.get("answer");if(a!=null&&a.isArray()){List<String> miss=new ArrayList<>();for(JsonNode x:a)if(x.isTextual()&&!ids.contains(x.asText()))miss.add(x.asText());if(!miss.isEmpty())r.add(Finding.of("error","ANSWER_UNKNOWN_CHOICE","Answer references unknown choice ID(s): "+String.join(", ",miss)+".",id,qp+".answer"));}}
    else if("true_false".equals(type)){Set<String> exp=new HashSet<>(Arrays.asList("true","false"));if(!ids.equals(exp))r.add(Finding.of("error","INVALID_TRUE_FALSE_CHOICES","True/false questions must use exactly the choice IDs 'true' and 'false'.",id,qp+".choices"));}
    else if("fill_in_the_blank".equals(type)){String prompt=text(q.get("question"));if(prompt!=null&&!prompt.contains("{{blank}}"))r.add(Finding.of("error","MISSING_BLANK","Fill-in-the-blank question does not contain '{{blank}}'.",id,qp+".question"));}

    Set<String> defined=new LinkedHashSet<>(); JsonNode vars=q.get("variables"); if(vars!=null&&vars.isObject()){Iterator<Map.Entry<String,JsonNode>> it=vars.fields();while(it.hasNext()){Map.Entry<String,JsonNode> e=it.next();defined.add(e.getKey());JsonNode d=e.getValue();String vt=text(d.get("type"));if(("integer".equals(vt)||"decimal".equals(vt))&&d.path("min").isNumber()&&d.path("max").isNumber()&&d.path("min").decimalValue().compareTo(d.path("max").decimalValue())>0)r.add(Finding.of("error","INVALID_VARIABLE_RANGE","Variable '"+e.getKey()+"' has min greater than max.",id,qp+".variables."+e.getKey()));}}
    Set<String> refs=placeholders(q), exprRefs=new LinkedHashSet<>(), undef=new LinkedHashSet<>();for(String x:refs)if(!defined.contains(x))undef.add(x);
    JsonNode ans=q.get("answer");if(ans!=null&&ans.isObject()&&ans.path("expression").isTextual()){ExpressionValidator.Result ev=ExpressionValidator.validate(ans.path("expression").asText());if(!ev.isValid())r.add(Finding.of("error","INVALID_EXPRESSION",ev.getError(),id,qp+".answer.expression"));else{exprRefs.addAll(ev.getVariables());for(String x:exprRefs)if(!defined.contains(x))undef.add(x);}}
    for(String x:undef)r.add(Finding.of("error","UNDEFINED_VARIABLE","Variable '"+x+"' is referenced but not defined.",id,qp)); Set<String> used=new HashSet<>(refs);used.addAll(exprRefs);for(String x:defined)if(!used.contains(x))r.add(Finding.of("warning","UNUSED_VARIABLE","Variable '"+x+"' is defined but never referenced.",id,qp+".variables."+x));
    media(q.get("media"),source,id,qp+".media",r); if(choices!=null&&choices.isArray())for(int j=0;j<choices.size();j++)media(choices.get(j).get("media"),source,id,qp+".choices["+j+"].media",r);
  }

  private static Set<String> placeholders(JsonNode q){Set<String>s=new LinkedHashSet<>();scan(q.get("question"),s);scan(q.get("explanation"),s);scan(q.get("hint"),s);JsonNode cs=q.get("choices");if(cs!=null&&cs.isArray())for(JsonNode c:cs){scan(c.get("text"),s);scan(c.get("feedback"),s);}return s;}
  private static void scan(JsonNode n,Set<String>s){if(n==null||!n.isTextual())return;Matcher m=PH.matcher(n.asText());while(m.find())if(!"blank".equals(m.group(1)))s.add(m.group(1));}
  private static void media(JsonNode arr,Path source,String id,String bp,ValidationResult r){if(arr==null||!arr.isArray())return;for(int i=0;i<arr.size();i++){String src=text(arr.get(i).get("src"));if(src==null||src.contains("://")||src.startsWith("data:"))continue;Path resolved=source.toAbsolutePath().getParent().resolve(src).normalize();if(!Files.exists(resolved))r.add(Finding.of("error","MISSING_MEDIA","Local media resource '"+src+"' does not exist.",id,bp+"["+i+"].src"));}}
  private static String text(JsonNode n){return n!=null&&n.isTextual()?n.asText():null;} private static boolean nonempty(JsonNode n){return n!=null&&n.isArray()&&n.size()>0;}
}
