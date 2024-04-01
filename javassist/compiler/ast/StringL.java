/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
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
/*    */ public class StringL
/*    */   extends ASTree
/*    */ {
/*    */   protected String text;
/*    */   
/*    */   public StringL(String t) {
/* 28 */     this.text = t;
/*    */   }
/*    */   public String get() {
/* 31 */     return this.text;
/*    */   } public String toString() {
/* 33 */     return "\"" + this.text + "\"";
/*    */   } public void accept(Visitor v) throws CompileError {
/* 35 */     v.atStringL(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\StringL.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */