/*    */ package javassist.tools.web;
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
/*    */ public class BadHttpRequest
/*    */   extends Exception
/*    */ {
/*    */   private Exception e;
/*    */   
/*    */   public BadHttpRequest() {
/* 25 */     this.e = null;
/*    */   } public BadHttpRequest(Exception _e) {
/* 27 */     this.e = _e;
/*    */   }
/*    */   public String toString() {
/* 30 */     if (this.e == null) {
/* 31 */       return super.toString();
/*    */     }
/* 33 */     return this.e.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\web\BadHttpRequest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */