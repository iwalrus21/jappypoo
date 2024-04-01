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
/*    */ public class InstanceOfExpr
/*    */   extends CastExpr
/*    */ {
/*    */   public InstanceOfExpr(ASTList className, int dim, ASTree expr) {
/* 26 */     super(className, dim, expr);
/*    */   }
/*    */   
/*    */   public InstanceOfExpr(int type, int dim, ASTree expr) {
/* 30 */     super(type, dim, expr);
/*    */   }
/*    */   
/*    */   public String getTag() {
/* 34 */     return "instanceof:" + this.castType + ":" + this.arrayDim;
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 38 */     v.atInstanceOfExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\InstanceOfExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */