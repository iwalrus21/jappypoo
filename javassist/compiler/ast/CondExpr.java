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
/*    */ public class CondExpr
/*    */   extends ASTList
/*    */ {
/*    */   public CondExpr(ASTree cond, ASTree thenp, ASTree elsep) {
/* 26 */     super(cond, new ASTList(thenp, new ASTList(elsep)));
/*    */   }
/*    */   public ASTree condExpr() {
/* 29 */     return head();
/*    */   } public void setCond(ASTree t) {
/* 31 */     setHead(t);
/*    */   } public ASTree thenExpr() {
/* 33 */     return tail().head();
/*    */   } public void setThen(ASTree t) {
/* 35 */     tail().setHead(t);
/*    */   } public ASTree elseExpr() {
/* 37 */     return tail().tail().head();
/*    */   } public void setElse(ASTree t) {
/* 39 */     tail().tail().setHead(t);
/*    */   } public String getTag() {
/* 41 */     return "?:";
/*    */   } public void accept(Visitor v) throws CompileError {
/* 43 */     v.atCondExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\CondExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */