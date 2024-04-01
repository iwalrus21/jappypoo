/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.schwering.irc.lib.IRCServerConfig;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIRCServerConfig
/*     */   implements IRCServerConfig
/*     */ {
/*     */   private final String encoding;
/*     */   private final String host;
/*     */   private final String nick;
/*     */   private final String pass;
/*     */   private final int[] ports;
/*     */   private final String realname;
/*     */   private final String username;
/*     */   
/*     */   public DefaultIRCServerConfig(IRCServerConfig serverConfig) {
/*  71 */     this(serverConfig.getHost(), serverConfig.getPorts(), serverConfig.getPassword(), serverConfig.getNick(), serverConfig
/*  72 */         .getUsername(), serverConfig.getRealname(), serverConfig.getEncoding());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCServerConfig(String host, int[] ports, String pass, String nick, String username, String realname, String encoding) {
/*  91 */     if (host == null || ports == null || ports.length == 0) {
/*  92 */       throw new IllegalArgumentException("Host and ports may not be null.");
/*     */     }
/*  94 */     this.host = host;
/*  95 */     this.ports = ports;
/*  96 */     this.pass = (pass != null && pass.length() == 0) ? null : pass;
/*  97 */     this.nick = nick;
/*  98 */     this.username = username;
/*  99 */     this.realname = realname;
/* 100 */     this.encoding = encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getEncoding() {
/* 108 */     return this.encoding;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHost() {
/* 116 */     return this.host;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNick() {
/* 124 */     return this.nick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getPassword() {
/* 132 */     return this.pass;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPortAt(int index) {
/* 140 */     return this.ports[index];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int[] getPorts() {
/* 148 */     return (this.ports == null) ? new int[0] : Arrays.copyOf(this.ports, this.ports.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPortsCount() {
/* 156 */     return (this.ports != null) ? this.ports.length : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRealname() {
/* 164 */     return this.realname;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getUsername() {
/* 172 */     return this.username;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCServerConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */