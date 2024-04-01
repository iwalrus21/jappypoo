/*    */ package org.schwering.irc.lib;
/*    */ 
/*    */ import org.schwering.irc.lib.impl.DefaultIRCConnection;
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
/*    */ public class IRCConnectionFactory
/*    */ {
/*    */   public static IRCConnection newConnection(IRCConfig config) {
/* 25 */     return (IRCConnection)new DefaultIRCConnection(config, config);
/*    */   }
/*    */   public static IRCConnection newConnection(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
/* 28 */     return (IRCConnection)new DefaultIRCConnection(serverConfig, runtimeConfig);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCConnectionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */