/*    */ package javassist.tools.reflect;
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
/*    */ public class CannotCreateException
/*    */   extends Exception
/*    */ {
/*    */   public CannotCreateException(String s) {
/* 24 */     super(s);
/*    */   }
/*    */   
/*    */   public CannotCreateException(Exception e) {
/* 28 */     super("by " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\reflect\CannotCreateException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */