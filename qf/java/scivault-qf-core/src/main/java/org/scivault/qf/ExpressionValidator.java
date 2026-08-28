package org.scivault.qf;

import java.util.*;

public final class ExpressionValidator {
    public static final class Result { private final Set<String> vars; private final String error; Result(Set<String>v,String e){vars=v;error=e;} public Set<String> getVariables(){return vars;} public String getError(){return error;} public boolean isValid(){return error==null;} }
    private final String s; private int p=0; private final Set<String> vars=new LinkedHashSet<>();
    private ExpressionValidator(String s){this.s=s;}
    public static Result validate(String s){try{ExpressionValidator p=new ExpressionValidator(s);p.expr();p.ws();if(p.p!=s.length())throw new IllegalArgumentException("Unexpected token at position "+p.p);return new Result(p.vars,null);}catch(Exception e){return new Result(new LinkedHashSet<>(),e.getMessage());}}
    private void expr(){term();for(;;){ws();if(take('+')||take('-'))term();else return;}}
    private void term(){power();for(;;){ws();if(starts("**"))return;if(take('*')||take('/')||take('%'))power();else return;}}
    private void power(){unary();ws();if(starts("**")){p+=2;power();}}
    private void unary(){ws();if(take('+')||take('-'))unary();else primary();}
    private void primary(){ws();if(take('(')){expr();ws();if(!take(')'))throw new IllegalArgumentException("Missing closing parenthesis");return;} if(p>=s.length())throw new IllegalArgumentException("Unexpected end of expression"); char c=s.charAt(p); if(Character.isDigit(c)||c=='.'){number();return;} if(Character.isLetter(c)||c=='_'){name();return;} throw new IllegalArgumentException("Forbidden or unsupported character '"+c+"'");}
    private void number(){int start=p;boolean digit=false,dot=false;while(p<s.length()){char c=s.charAt(p);if(Character.isDigit(c)){digit=true;p++;}else if(c=='.'&&!dot){dot=true;p++;}else break;}if(!digit)throw new IllegalArgumentException("Invalid numeric literal");if(p<s.length()&&(s.charAt(p)=='e'||s.charAt(p)=='E')){p++;if(p<s.length()&&(s.charAt(p)=='+'||s.charAt(p)=='-'))p++;int e=p;while(p<s.length()&&Character.isDigit(s.charAt(p)))p++;if(e==p)throw new IllegalArgumentException("Invalid exponent");} if(start==p)throw new IllegalArgumentException("Invalid number");}
    private void name(){int start=p++;while(p<s.length()&&(Character.isLetterOrDigit(s.charAt(p))||s.charAt(p)=='_'))p++;vars.add(s.substring(start,p));}
    private boolean take(char c){ws();if(p<s.length()&&s.charAt(p)==c){p++;return true;}return false;} private boolean starts(String x){ws();return s.startsWith(x,p);} private void ws(){while(p<s.length()&&Character.isWhitespace(s.charAt(p)))p++;}
}
