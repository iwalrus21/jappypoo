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
/*    */ public class Pair
/*    */   extends ASTree
/*    */ {
/*    */   protected ASTree left;
/*    */   protected ASTree right;
/*    */   
/*    */   public Pair(ASTree _left, ASTree _right) {
/* 29 */     this.left = _left;
/* 30 */     this.right = _right;
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 33 */     v.atPair(this);
/*    */   }
/*    */   public String toString() {
/* 36 */     StringBuffer sbuf = new StringBuffer();
/* 37 */     sbuf.append("(<Pair> ");
/* 38 */     sbuf.append((this.left == null) ? "<null>" : this.left.toString());
/* 39 */     sbuf.append(" . ");
/* 40 */     sbuf.append((this.right == null) ? "<null>" : this.right.toString());
/* 41 */     sbuf.append(')');
/* 42 */     return sbuf.toString();
/*    */   }
/*    */   public ASTree getLeft() {
/* 45 */     return this.left;
/*    */   } public ASTree getRight() {
/* 47 */     return this.right;
/*    */   } public void setLeft(ASTree _left) {
/* 49 */     this.left = _left;
/*    */   } public void setRight(ASTree _right) {
/* 51 */     this.right = _right;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Pair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */