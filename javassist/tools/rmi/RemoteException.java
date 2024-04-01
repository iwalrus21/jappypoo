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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RemoteException
/*    */   extends RuntimeException
/*    */ {
/*    */   public RemoteException(String msg) {
/* 25 */     super(msg);
/*    */   }
/*    */   
/*    */   public RemoteException(Exception e) {
/* 29 */     super("by " + e.toString());
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\javassist\tools\rmi\RemoteException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */