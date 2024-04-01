/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public class WhitespaceToken
/*    */   extends Token
/*    */ {
/*    */   public WhitespaceToken(Mark startMark, Mark endMark) {
/* 22 */     super(startMark, endMark);
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 27 */     return Token.ID.Whitespace;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\tokens\WhitespaceToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */