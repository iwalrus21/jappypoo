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
/*    */ public class CastExpr
/*    */   extends ASTList
/*    */   implements TokenId
/*    */ {
/*    */   protected int castType;
/*    */   protected int arrayDim;
/*    */   
/*    */   public CastExpr(ASTList className, int dim, ASTree expr) {
/* 30 */     super(className, new ASTList(expr));
/* 31 */     this.castType = 307;
/* 32 */     this.arrayDim = dim;
/*    */   }
/*    */   
/*    */   public CastExpr(int type, int dim, ASTree expr) {
/* 36 */     super(null, new ASTList(expr));
/* 37 */     this.castType = type;
/* 38 */     this.arrayDim = dim;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getType() {
/* 43 */     return this.castType;
/*    */   } public int getArrayDim() {
/* 45 */     return this.arrayDim;
/*    */   } public ASTList getClassName() {
/* 47 */     return (ASTList)getLeft();
/*    */   } public ASTree getOprand() {
/* 49 */     return getRight().getLeft();
/*    */   } public void setOprand(ASTree t) {
/* 51 */     getRight().setLeft(t);
/*    */   } public String getTag() {
/* 53 */     return "cast:" + this.castType + ":" + this.arrayDim;
/*    */   } public void accept(Visitor v) throws CompileError {
/* 55 */     v.atCastExpr(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\CastExpr.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */