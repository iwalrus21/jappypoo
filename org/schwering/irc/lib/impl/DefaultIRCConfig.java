/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.net.Proxy;
/*     */ import org.schwering.irc.lib.IRCConfig;
/*     */ import org.schwering.irc.lib.IRCExceptionHandler;
/*     */ import org.schwering.irc.lib.IRCRuntimeConfig;
/*     */ import org.schwering.irc.lib.IRCSSLSupport;
/*     */ import org.schwering.irc.lib.IRCServerConfig;
/*     */ import org.schwering.irc.lib.IRCTrafficLogger;
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
/*     */ public class DefaultIRCConfig
/*     */   extends DefaultIRCServerConfig
/*     */   implements IRCConfig
/*     */ {
/*     */   private final boolean autoPong;
/*     */   private final Proxy proxy;
/*     */   private final IRCSSLSupport sslSupport;
/*     */   private final boolean stripColorsEnabled;
/*     */   private final int timeout;
/*     */   private final IRCTrafficLogger trafficLogger;
/*     */   private final IRCExceptionHandler exceptionHandler;
/*     */   
/*     */   public DefaultIRCConfig(IRCConfig config) {
/*  71 */     this(config.getHost(), config.getPorts(), config.getPassword(), config.getNick(), config.getUsername(), config
/*  72 */         .getRealname(), config.getEncoding(), config.getTimeout(), config.isAutoPong(), config
/*  73 */         .isStripColorsEnabled(), new DefaultIRCSSLSupport(config.getSSLSupport()), config.getProxy(), config
/*  74 */         .getTrafficLogger(), config.getExceptionHandler());
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
/*     */   public DefaultIRCConfig(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
/*  87 */     this(serverConfig.getHost(), serverConfig.getPorts(), serverConfig.getPassword(), serverConfig.getNick(), serverConfig
/*  88 */         .getUsername(), serverConfig.getRealname(), serverConfig.getEncoding(), runtimeConfig
/*  89 */         .getTimeout(), runtimeConfig.isAutoPong(), runtimeConfig.isStripColorsEnabled(), new DefaultIRCSSLSupport(runtimeConfig
/*  90 */           .getSSLSupport()), runtimeConfig.getProxy(), runtimeConfig
/*  91 */         .getTrafficLogger(), runtimeConfig.getExceptionHandler());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCConfig(String host, int[] ports, String pass, String nick, String username, String realname, String encoding, int timeout, boolean autoPong, boolean stripColorsEnabled, IRCSSLSupport sslSupport, Proxy proxy, IRCTrafficLogger trafficLogger, IRCExceptionHandler exceptionHandler) {
/* 117 */     super(host, ports, pass, nick, username, realname, encoding);
/* 118 */     this.timeout = timeout;
/* 119 */     this.autoPong = autoPong;
/* 120 */     this.stripColorsEnabled = stripColorsEnabled;
/* 121 */     this.sslSupport = sslSupport;
/* 122 */     this.proxy = proxy;
/* 123 */     this.trafficLogger = trafficLogger;
/* 124 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/* 132 */     return this.proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCSSLSupport getSSLSupport() {
/* 140 */     return this.sslSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTimeout() {
/* 148 */     return this.timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCTrafficLogger getTrafficLogger() {
/* 156 */     return this.trafficLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoPong() {
/* 164 */     return this.autoPong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStripColorsEnabled() {
/* 172 */     return this.stripColorsEnabled;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCExceptionHandler getExceptionHandler() {
/* 179 */     return this.exceptionHandler;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */