/*    */ package org.json;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JSONException
/*    */   extends Exception
/*    */ {
/*    */   private static final long serialVersionUID = 0L;
/*    */   private Throwable cause;
/*    */   
/*    */   public JSONException(String message) {
/* 17 */     super(message);
/*    */   }
/*    */   
/*    */   public JSONException(Throwable cause) {
/* 21 */     super(cause.getMessage());
/* 22 */     this.cause = cause;
/*    */   }
/*    */   
/*    */   public Throwable getCause() {
/* 26 */     return this.cause;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\json\JSONException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */