/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SecureRandom;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import javax.net.ssl.KeyManager;
/*     */ import javax.net.ssl.TrustManager;
/*     */ import javax.net.ssl.X509TrustManager;
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
/*     */ public class DefaultIRCSSLSupport
/*     */   implements IRCSSLSupport
/*     */ {
/*  36 */   protected static final X509Certificate[] EMPTY_X509_CERTIFICATES = new X509Certificate[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IRCSSLSupport INSECURE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  47 */   public static final X509TrustManager INSECURE_TRUST_MANAGER = new X509TrustManager()
/*     */     {
/*     */       public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       public X509Certificate[] getAcceptedIssuers() {
/*  60 */         return DefaultIRCSSLSupport.EMPTY_X509_CERTIFICATES;
/*     */       }
/*     */     };
/*     */   static {
/*     */     try {
/*  65 */       INSECURE = new DefaultIRCSSLSupport(new KeyManager[0], new TrustManager[] { INSECURE_TRUST_MANAGER }, SecureRandom.getInstanceStrong());
/*  66 */     } catch (NoSuchAlgorithmException e) {
/*  67 */       throw new RuntimeException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final KeyManager[] keyManagers;
/*     */ 
/*     */ 
/*     */   
/*     */   private final SecureRandom secureRandom;
/*     */ 
/*     */ 
/*     */   
/*     */   private final TrustManager[] trustManagers;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCSSLSupport(IRCSSLSupport sslSupport) {
/*  88 */     this(sslSupport.getKeyManagers(), sslSupport.getTrustManagers(), sslSupport.getSecureRandom());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCSSLSupport(KeyManager[] keyManagers, TrustManager[] trustManagers, SecureRandom secureRandom) {
/*  98 */     this.keyManagers = Arrays.<KeyManager>copyOf(keyManagers, keyManagers.length);
/*  99 */     this.trustManagers = Arrays.<TrustManager>copyOf(trustManagers, trustManagers.length);
/* 100 */     this.secureRandom = secureRandom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public KeyManager[] getKeyManagers() {
/* 108 */     return Arrays.<KeyManager>copyOf(this.keyManagers, this.keyManagers.length);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public SecureRandom getSecureRandom() {
/* 116 */     return this.secureRandom;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TrustManager[] getTrustManagers() {
/* 124 */     return Arrays.<TrustManager>copyOf(this.trustManagers, this.trustManagers.length);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCSSLSupport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */