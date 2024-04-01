/*    */ package javassist.compiler;
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
/*    */ public class SyntaxError
/*    */   extends CompileError
/*    */ {
/*    */   public SyntaxError(Lex lexer) {
/* 21 */     super("syntax error near \"" + lexer.getTextAround() + "\"", lexer);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\compiler\SyntaxError.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */