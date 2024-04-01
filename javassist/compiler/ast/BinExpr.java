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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BinExpr
/*    */   extends Expr
/*    */ {
/*    */   private BinExpr(int op, ASTree _head, ASTList _tail) {
/* 34 */     super(op, _head, _tail);
/*    */   }
/*    */   
/*    */   public static BinExpr makeBin(int op, ASTree oprand1, ASTree oprand2) {
/* 38 */     return new BinExpr(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 41 */     v.atBinExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\BinExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */