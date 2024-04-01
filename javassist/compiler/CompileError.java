/*    */ package javassist.compiler;
/*    */ 
/*    */ import javassist.CannotCompileException;
/*    */ import javassist.NotFoundException;
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
/*    */ public class CompileError
/*    */   extends Exception
/*    */ {
/*    */   private Lex lex;
/*    */   private String reason;
/*    */   
/*    */   public CompileError(String s, Lex l) {
/* 27 */     this.reason = s;
/* 28 */     this.lex = l;
/*    */   }
/*    */   
/*    */   public CompileError(String s) {
/* 32 */     this.reason = s;
/* 33 */     this.lex = null;
/*    */   }
/*    */   
/*    */   public CompileError(CannotCompileException e) {
/* 37 */     this(e.getReason());
/*    */   }
/*    */   
/*    */   public CompileError(NotFoundException e) {
/* 41 */     this("cannot find " + e.getMessage());
/*    */   }
/*    */   public Lex getLex() {
/* 44 */     return this.lex;
/*    */   }
/*    */   public String getMessage() {
/* 47 */     return this.reason;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 51 */     return "compile error: " + this.reason;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\CompileError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */