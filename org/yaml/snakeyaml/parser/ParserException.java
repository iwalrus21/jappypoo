/*    */ package org.yaml.snakeyaml.parser;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
/*    */ import org.yaml.snakeyaml.error.MarkedYAMLException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ParserException
/*    */   extends MarkedYAMLException
/*    */ {
/*    */   private static final long serialVersionUID = -2349253802798398038L;
/*    */   
/*    */   public ParserException(String context, Mark contextMark, String problem, Mark problemMark) {
/* 42 */     super(context, contextMark, problem, problemMark, null, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\parser\ParserException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */