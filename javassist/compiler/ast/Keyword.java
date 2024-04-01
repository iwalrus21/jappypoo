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
/*    */ public class Keyword
/*    */   extends ASTree
/*    */ {
/*    */   protected int tokenId;
/*    */   
/*    */   public Keyword(int token) {
/* 28 */     this.tokenId = token;
/*    */   }
/*    */   public int get() {
/* 31 */     return this.tokenId;
/*    */   } public String toString() {
/* 33 */     return "id:" + this.tokenId;
/*    */   } public void accept(Visitor v) throws CompileError {
/* 35 */     v.atKeyword(this);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\ast\Keyword.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */