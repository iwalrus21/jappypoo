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
/*    */ public class FieldDecl
/*    */   extends ASTList
/*    */ {
/*    */   public FieldDecl(ASTree _head, ASTList _tail) {
/* 23 */     super(_head, _tail);
/*    */   }
/*    */   public ASTList getModifiers() {
/* 26 */     return (ASTList)getLeft();
/*    */   } public Declarator getDeclarator() {
/* 28 */     return (Declarator)tail().head();
/*    */   } public ASTree getInit() {
/* 30 */     return sublist(2).head();
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 33 */     v.atFieldDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\FieldDecl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */