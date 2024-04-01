/*    */ package org.schwering.irc.lib;
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
/*    */ public interface IRCExceptionHandler
/*    */ {
/* 24 */   public static final IRCExceptionHandler PRINT_STACK_TRACE = new IRCExceptionHandler()
/*    */     {
/*    */ 
/*    */ 
/*    */       
/*    */       public void exception(IRCConnection connection, Throwable e)
/*    */       {
/* 31 */         e.printStackTrace();
/*    */       }
/*    */     };
/*    */   
/*    */   void exception(IRCConnection paramIRCConnection, Throwable paramThrowable);
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */