/*    */ package javassist.compiler;
/*    */ 
/*    */ import javassist.compiler.ast.ASTree;
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
/*    */ public class NoFieldException
/*    */   extends CompileError
/*    */ {
/*    */   private String fieldName;
/*    */   private ASTree expr;
/*    */   
/*    */   public NoFieldException(String name, ASTree e) {
/* 28 */     super("no such field: " + name);
/* 29 */     this.fieldName = name;
/* 30 */     this.expr = e;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getField() {
/* 35 */     return this.fieldName;
/*    */   }
/*    */   
/*    */   public ASTree getExpr() {
/* 39 */     return this.expr;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\NoFieldException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */