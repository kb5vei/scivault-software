package org.scivault.qf;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Finding {
    private final String severity, code, message, questionId, path;
    private final Map<String,Object> detail;

    public Finding(String severity, String code, String message, String questionId, String path, Map<String,Object> detail) {
        this.severity=severity; this.code=code; this.message=message; this.questionId=questionId; this.path=path; this.detail=detail;
    }
    public static Finding of(String s,String c,String m,String q,String p){ return new Finding(s,c,m,q,p,null); }
    public static Finding of(String s,String c,String m,String q,String p,Map<String,Object>d){ return new Finding(s,c,m,q,p,d); }
    public String getSeverity(){return severity;} public String getCode(){return code;} public String getMessage(){return message;}
    public String getQuestionId(){return questionId;} public String getPath(){return path;} public Map<String,Object> getDetail(){return detail;}
    public Map<String,Object> toMap(){ Map<String,Object> out=new LinkedHashMap<>(); out.put("severity",severity); out.put("code",code); out.put("message",message); if(questionId!=null)out.put("question_id",questionId); if(path!=null)out.put("path",path); if(detail!=null&&!detail.isEmpty())out.put("detail",detail); return out; }
}
