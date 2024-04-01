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
/*    */ public class Variable
/*    */   extends Symbol
/*    */ {
/*    */   protected Declarator declarator;
/*    */   
/*    */   public Variable(String sym, Declarator d) {
/* 28 */     super(sym);
/* 29 */     this.declarator = d;
/*    */   }
/*    */   public Declarator getDeclarator() {
/* 32 */     return this.declarator;
/*    */   }
/*    */   public String toString() {
/* 35 */     return this.identifier + ":" + this.declarator.getType();
/*    */   }
/*    */   public void accept(Visitor v) throws CompileError {
/* 38 */     v.atVariable(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Variable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */