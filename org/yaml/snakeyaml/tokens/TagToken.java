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
/*    */ public final class TagToken
/*    */   extends Token
/*    */ {
/*    */   private final TagTuple value;
/*    */   
/*    */   public TagToken(TagTuple value, Mark startMark, Mark endMark) {
/* 24 */     super(startMark, endMark);
/* 25 */     this.value = value;
/*    */   }
/*    */   
/*    */   public TagTuple getValue() {
/* 29 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArguments() {
/* 34 */     return "value=[" + this.value.getHandle() + ", " + this.value.getSuffix() + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public Token.ID getTokenId() {
/* 39 */     return Token.ID.Tag;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\tokens\TagToken.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */