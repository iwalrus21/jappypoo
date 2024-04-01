/*    */ package javassist.tools.rmi;
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
/*    */ public class ObjectNotFoundException
/*    */   extends Exception
/*    */ {
/*    */   public ObjectNotFoundException(String name) {
/* 21 */     super(name + " is not exported");
/*    */   }
/*    */   
/*    */   public ObjectNotFoundException(String name, Exception e) {
/* 25 */     super(name + " because of " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\ObjectNotFoundException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */