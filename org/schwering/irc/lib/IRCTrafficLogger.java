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
/*    */ 
/*    */ public interface IRCTrafficLogger
/*    */ {
/* 25 */   public static final IRCTrafficLogger SYSTEM_OUT = new IRCTrafficLogger()
/*    */     {
/*    */       public void out(String line) {
/* 28 */         System.out.println("< " + line);
/*    */       }
/*    */ 
/*    */       
/*    */       public void in(String line) {
/* 33 */         System.out.println("> " + line);
/*    */       }
/*    */     };
/*    */   
/*    */   void in(String paramString);
/*    */   
/*    */   void out(String paramString);
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCTrafficLogger.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */