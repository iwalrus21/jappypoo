/*    */ package javassist.compiler.ast;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public abstract class ASTree
/*    */   implements Serializable
/*    */ {
/*    */   public ASTree getLeft() {
/* 28 */     return null;
/*    */   } public ASTree getRight() {
/* 30 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setLeft(ASTree _left) {}
/*    */ 
/*    */   
/*    */   public void setRight(ASTree _right) {}
/*    */ 
/*    */   
/*    */   public abstract void accept(Visitor paramVisitor) throws CompileError;
/*    */ 
/*    */   
/*    */   public String toString() {
/* 44 */     StringBuffer sbuf = new StringBuffer();
/* 45 */     sbuf.append('<');
/* 46 */     sbuf.append(getTag());
/* 47 */     sbuf.append('>');
/* 48 */     return sbuf.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getTag() {
/* 56 */     String name = getClass().getName();
/* 57 */     return name.substring(name.lastIndexOf('.') + 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\ASTree.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */