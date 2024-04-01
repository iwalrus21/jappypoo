/*    */ package org.yaml.snakeyaml.scanner;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScannerException
/*    */   extends MarkedYAMLException
/*    */ {
/*    */   private static final long serialVersionUID = 4782293188600445954L;
/*    */   
/*    */   public ScannerException(String context, Mark contextMark, String problem, Mark problemMark, String note) {
/* 47 */     super(context, contextMark, problem, problemMark, note);
/*    */   }
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
/*    */   public ScannerException(String context, Mark contextMark, String problem, Mark problemMark) {
/* 64 */     this(context, contextMark, problem, problemMark, null);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\scanner\ScannerException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */