/*    */ package javassist;
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
/*    */ public class NotFoundException
/*    */   extends Exception
/*    */ {
/*    */   public NotFoundException(String msg) {
/* 24 */     super(msg);
/*    */   }
/*    */   
/*    */   public NotFoundException(String msg, Exception e) {
/* 28 */     super(msg + " because of " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\NotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */