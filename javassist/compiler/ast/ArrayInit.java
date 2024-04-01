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
/*    */ public class ArrayInit
/*    */   extends ASTList
/*    */ {
/*    */   public ArrayInit(ASTree firstElement) {
/* 26 */     super(firstElement);
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 29 */     v.atArrayInit(this);
/*    */   } public String getTag() {
/* 31 */     return "array";
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\ArrayInit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */