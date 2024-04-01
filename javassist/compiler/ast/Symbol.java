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
/*    */ public class Symbol
/*    */   extends ASTree
/*    */ {
/*    */   protected String identifier;
/*    */   
/*    */   public Symbol(String sym) {
/* 28 */     this.identifier = sym;
/*    */   }
/*    */   public String get() {
/* 31 */     return this.identifier;
/*    */   } public String toString() {
/* 33 */     return this.identifier;
/*    */   } public void accept(Visitor v) throws CompileError {
/* 35 */     v.atSymbol(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Symbol.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */