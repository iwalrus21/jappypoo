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
/*    */ public class Stmnt
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   protected int operatorId;
/*    */   
/*    */   public Stmnt(int op, ASTree _head, ASTList _tail) {
/* 29 */     super(_head, _tail);
/* 30 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public Stmnt(int op, ASTree _head) {
/* 34 */     super(_head);
/* 35 */     this.operatorId = op;
/*    */   }
/*    */   
/*    */   public Stmnt(int op) {
/* 39 */     this(op, null);
/*    */   }
/*    */   
/*    */   public static Stmnt make(int op, ASTree oprand1, ASTree oprand2) {
/* 43 */     return new Stmnt(op, oprand1, new ASTList(oprand2));
/*    */   }
/*    */   
/*    */   public static Stmnt make(int op, ASTree op1, ASTree op2, ASTree op3) {
/* 47 */     return new Stmnt(op, op1, new ASTList(op2, new ASTList(op3)));
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 50 */     v.atStmnt(this);
/*    */   } public int getOperator() {
/* 52 */     return this.operatorId;
/*    */   }
/*    */   protected String getTag() {
/* 55 */     if (this.operatorId < 128) {
/* 56 */       return "stmnt:" + (char)this.operatorId;
/*    */     }
/* 58 */     return "stmnt:" + this.operatorId;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Stmnt.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */