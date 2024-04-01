/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.net.Proxy;
/*     */ import org.schwering.irc.lib.IRCExceptionHandler;
/*     */ import org.schwering.irc.lib.IRCRuntimeConfig;
/*     */ import org.schwering.irc.lib.IRCSSLSupport;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultIRCRuntimeConfig
/*     */   implements IRCRuntimeConfig
/*     */ {
/*     */   private final boolean autoPong;
/*     */   private final IRCExceptionHandler exceptionHandler;
/*     */   private final Proxy proxy;
/*     */   private final IRCSSLSupport sslSupport;
/*     */   private final boolean stripColorsEnabled;
/*     */   private final int timeout;
/*     */   private final IRCTrafficLogger trafficLogger;
/*     */   
/*     */   public DefaultIRCRuntimeConfig(int timeout, boolean autoPong, boolean stripColorsEnabled, IRCSSLSupport sslSupport, Proxy proxy, IRCTrafficLogger trafficLogger, IRCExceptionHandler exceptionHandler) {
/*  75 */     this.timeout = timeout;
/*  76 */     this.autoPong = autoPong;
/*  77 */     this.stripColorsEnabled = stripColorsEnabled;
/*  78 */     this.sslSupport = sslSupport;
/*  79 */     this.proxy = proxy;
/*  80 */     this.trafficLogger = trafficLogger;
/*  81 */     this.exceptionHandler = exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCRuntimeConfig(IRCRuntimeConfig runtimeConfig) {
/*  92 */     this(runtimeConfig.getTimeout(), runtimeConfig.isAutoPong(), runtimeConfig.isStripColorsEnabled(), runtimeConfig
/*  93 */         .getSSLSupport(), runtimeConfig.getProxy(), runtimeConfig.getTrafficLogger(), runtimeConfig
/*  94 */         .getExceptionHandler());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCExceptionHandler getExceptionHandler() {
/* 101 */     return this.exceptionHandler;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Proxy getProxy() {
/* 109 */     return this.proxy;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCSSLSupport getSSLSupport() {
/* 117 */     return this.sslSupport;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTimeout() {
/* 125 */     return this.timeout;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCTrafficLogger getTrafficLogger() {
/* 133 */     return this.trafficLogger;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isAutoPong() {
/* 141 */     return this.autoPong;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isStripColorsEnabled() {
/* 149 */     return this.stripColorsEnabled;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCRuntimeConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */