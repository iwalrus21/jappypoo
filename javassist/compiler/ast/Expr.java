/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import javassist.compiler.CompileError;
/*    */ import javassist.compiler.TokenId;
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
/*    */ public class Expr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   protected int operatorId;
/*    */   
/*    */   Expr(int op, ASTree _head, ASTList _tail) {
/* 35 */     super(_head, _tail);
/* 36 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   Expr(int op, ASTree _head) {
/* 40 */     super(_head);
/* 41 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public static Expr make(int op, ASTree oprand1, ASTree oprand2) {
/* 45 */     return new Expr(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   
/*    */   public static Expr make(int op, ASTree oprand1) {
/* 49 */     return new Expr(op, oprand1);
/*    */   }
/*    */   public int getOperator() {
/* 52 */     return this.operatorId;
/*    */   } public void setOperator(int op) {
/* 54 */     this.operatorId = op;
/*    */   } public ASTree oprand1() {
/* 56 */     return getLeft();
/*    */   }
/*    */   public void setOprand1(ASTree expr) {
/* 59 */     setLeft(expr);
/*    */   }
/*    */   public ASTree oprand2() {
/* 62 */     return getRight().getLeft();
/*    */   }
/*    */   public void setOprand2(ASTree expr) {
/* 65 */     getRight().setLeft(expr);
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 68 */     v.atExpr(this);
/*    */   }
/*    */   public String getName() {
/* 71 */     int id = this.operatorId;
/* 72 */     if (id < 128)
/* 73 */       return String.valueOf((char)id); 
/* 74 */     if (350 <= id && id <= 371)
/* 75 */       return opNames[id - 350]; 
/* 76 */     if (id == 323) {
/* 77 */       return "instanceof";
/*    */     }
/* 79 */     return String.valueOf(id);
/*    */   }
/*    */   
/*    */   protected String getTag() {
/* 83 */     return "op:" + getName();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Expr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */