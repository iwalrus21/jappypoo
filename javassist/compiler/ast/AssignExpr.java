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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AssignExpr
/*    */   extends Expr
/*    */ {
/*    */   private AssignExpr(int op, ASTree _head, ASTList _tail) {
/* 30 */     super(op, _head, _tail);
/*    */   }
/*    */ 
/*    */   
/*    */   public static AssignExpr makeAssign(int op, ASTree oprand1, ASTree oprand2) {
/* 35 */     return new AssignExpr(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   
/*    */   public void accept(Visitor v) throws CompileError {
/* 39 */     v.atAssignExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\AssignExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */