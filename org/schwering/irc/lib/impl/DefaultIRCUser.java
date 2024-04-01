/*    */ package org.schwering.irc.lib.impl;
/*    */ 
/*    */ import org.schwering.irc.lib.IRCUser;
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
/*    */ public class DefaultIRCUser
/*    */   implements IRCUser
/*    */ {
/*    */   private final String nick;
/*    */   private final String username;
/*    */   private final String host;
/*    */   
/*    */   public DefaultIRCUser(String nick, String username, String host) {
/* 53 */     this.nick = nick;
/* 54 */     this.username = username;
/* 55 */     this.host = host;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getNick() {
/* 64 */     return this.nick;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getUsername() {
/* 72 */     return this.username;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getHost() {
/* 81 */     return this.host;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 89 */     return getNick();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCUser.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */