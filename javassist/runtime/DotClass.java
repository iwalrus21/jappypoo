/*    */ package javassist.runtime;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DotClass
/*    */ {
/*    */   public static NoClassDefFoundError fail(ClassNotFoundException e) {
/* 27 */     return new NoClassDefFoundError(e.getMessage());
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\runtime\DotClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */