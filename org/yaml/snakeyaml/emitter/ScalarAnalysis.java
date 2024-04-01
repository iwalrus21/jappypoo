/*    */ package org.yaml.snakeyaml.emitter;
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
/*    */ public final class ScalarAnalysis
/*    */ {
/*    */   public String scalar;
/*    */   public boolean empty;
/*    */   public boolean multiline;
/*    */   public boolean allowFlowPlain;
/*    */   public boolean allowBlockPlain;
/*    */   public boolean allowSingleQuoted;
/*    */   public boolean allowBlock;
/*    */   
/*    */   public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain, boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowBlock) {
/* 29 */     this.scalar = scalar;
/* 30 */     this.empty = empty;
/* 31 */     this.multiline = multiline;
/* 32 */     this.allowFlowPlain = allowFlowPlain;
/* 33 */     this.allowBlockPlain = allowBlockPlain;
/* 34 */     this.allowSingleQuoted = allowSingleQuoted;
/* 35 */     this.allowBlock = allowBlock;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\yaml\snakeyaml\emitter\ScalarAnalysis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */