/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.net.Socket;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import javax.net.ssl.SSLContext;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ import org.schwering.irc.lib.IRCSSLSupport;
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
/*     */ public class SocketFactory
/*     */ {
/*     */   private final Proxy proxy;
/*     */   private SSLSocketFactory sslSocketFactory;
/*     */   private final int timeout;
/*     */   
/*     */   public SocketFactory(int timeout, Proxy proxy, IRCSSLSupport sslSupport) throws KeyManagementException, NoSuchAlgorithmException {
/*  63 */     this.timeout = timeout;
/*  64 */     this.proxy = (proxy == null) ? Proxy.NO_PROXY : proxy;
/*  65 */     if (sslSupport != null) {
/*  66 */       SSLContext sslContext = SSLContext.getInstance("SSL");
/*  67 */       sslContext.init(sslSupport.getKeyManagers(), sslSupport.getTrustManagers(), sslSupport.getSecureRandom());
/*  68 */       this.sslSocketFactory = sslContext.getSocketFactory();
/*     */     } else {
/*  70 */       this.sslSocketFactory = null;
/*     */     } 
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
/*     */   public Socket createSocket(String host, int port) throws IOException {
/*     */     Socket result;
/*  88 */     if (this.sslSocketFactory == null) {
/*     */       
/*  90 */       result = new Socket(this.proxy);
/*  91 */       result.connect(new InetSocketAddress(host, port), this.timeout);
/*  92 */     } else if (this.proxy == Proxy.NO_PROXY) {
/*     */       
/*  94 */       SSLSocket sslResult = (SSLSocket)this.sslSocketFactory.createSocket(host, port);
/*  95 */       sslResult.startHandshake();
/*  96 */       result = sslResult;
/*     */     } else {
/*     */       
/*  99 */       Socket proxySocket = new Socket(this.proxy);
/* 100 */       SSLSocket sslResult = (SSLSocket)this.sslSocketFactory.createSocket(proxySocket, host, port, true);
/* 101 */       sslResult.startHandshake();
/* 102 */       result = sslResult;
/*     */     } 
/* 104 */     result.setSoTimeout(this.timeout);
/* 105 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\SocketFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */