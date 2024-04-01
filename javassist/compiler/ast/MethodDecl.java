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
/*    */ public class MethodDecl
/*    */   extends ASTList
/*    */ {
/*    */   public static final String initName = "<init>";
/*    */   
/*    */   public MethodDecl(ASTree _head, ASTList _tail) {
/* 25 */     super(_head, _tail);
/*    */   }
/*    */   
/*    */   public boolean isConstructor() {
/* 29 */     Symbol sym = getReturn().getVariable();
/* 30 */     return (sym != null && "<init>".equals(sym.get()));
/*    */   }
/*    */   public ASTList getModifiers() {
/* 33 */     return (ASTList)getLeft();
/*    */   } public Declarator getReturn() {
/* 35 */     return (Declarator)tail().head();
/*    */   } public ASTList getParams() {
/* 37 */     return (ASTList)sublist(2).head();
/*    */   } public ASTList getThrows() {
/* 39 */     return (ASTList)sublist(3).head();
/*    */   } public Stmnt getBody() {
/* 41 */     return (Stmnt)sublist(4).head();
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 44 */     v.atMethodDecl(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\MethodDecl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */