package org.scivault.qf;

import java.util.*;
import java.util.stream.Collectors;

public final class ValidationResult {
    private final List<Finding> findings=new ArrayList<>();
    public void add(Finding f){findings.add(f);} public List<Finding> getFindings(){return new ArrayList<>(findings);}
    public boolean isValid(){return findings.stream().noneMatch(f->"error".equals(f.getSeverity()));}
    public Map<String,Object> toMap(){Map<String,Object> m=new LinkedHashMap<>();m.put("valid",isValid());m.put("findings",findings.stream().map(Finding::toMap).collect(Collectors.toList()));return m;}
}
