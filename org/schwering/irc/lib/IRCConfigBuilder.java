/*     */ package org.schwering.irc.lib;
/*     */ 
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import org.schwering.irc.lib.impl.DefaultIRCConfig;
/*     */ import org.schwering.irc.lib.util.IRCUtil;
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
/*     */ public final class IRCConfigBuilder
/*     */ {
/*     */   public static final boolean DEFAULT_AUTOPONG = true;
/*     */   public static final String DEFAULT_ENCODING = "utf-8";
/*     */   public static final boolean DEFAULT_STRIP_COLORS = false;
/*     */   public static final int DEFAULT_TIMEOUT = 900000;
/*     */   
/*     */   public static IRCConfigBuilder newBuilder() {
/*  64 */     return new IRCConfigBuilder();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean autoPong = true;
/*     */   
/*  71 */   private String encoding = "utf-8";
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IRCExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */   
/*     */   private String host;
/*     */ 
/*     */ 
/*     */   
/*     */   private String nick;
/*     */ 
/*     */ 
/*     */   
/*     */   private String password;
/*     */ 
/*     */ 
/*     */   
/*  92 */   private final List<Integer> ports = new ArrayList<>();
/*     */ 
/*     */   
/*     */   private Proxy proxy;
/*     */ 
/*     */   
/*     */   private String realname;
/*     */ 
/*     */   
/*     */   private IRCSSLSupport sslSupport;
/*     */ 
/*     */   
/*     */   private boolean stripColors = false;
/*     */ 
/*     */   
/* 107 */   private int timeout = 900000;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private IRCTrafficLogger trafficLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String username;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder autoPong(boolean autoPong) {
/* 133 */     this.autoPong = autoPong;
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfig build() {
/* 142 */     return (IRCConfig)new DefaultIRCConfig(this.host, IRCUtil.toArray(this.ports), this.password, this.nick, this.username, this.realname, this.encoding, this.timeout, this.autoPong, this.stripColors, this.sslSupport, this.proxy, this.trafficLogger, this.exceptionHandler);
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
/*     */   public IRCConfigBuilder config(IRCConfig config) {
/* 155 */     serverConfig(config);
/* 156 */     runtimeConfig(config);
/* 157 */     return this;
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
/*     */   public IRCConfigBuilder encoding(String encoding) {
/* 170 */     this.encoding = encoding;
/* 171 */     return this;
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
/*     */   public IRCConfigBuilder exceptionHandler(IRCExceptionHandler exceptionHandler) {
/* 183 */     this.exceptionHandler = exceptionHandler;
/* 184 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder host(String host) {
/* 194 */     this.host = host;
/* 195 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder nick(String nick) {
/* 206 */     this.nick = nick;
/* 207 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder password(String password) {
/* 217 */     this.password = password;
/* 218 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder port(int port) {
/* 229 */     this.ports.add(Integer.valueOf(port));
/* 230 */     return this;
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
/*     */   public IRCConfigBuilder portRange(int portMin, int portMax) {
/* 245 */     if (portMin > portMax) {
/* 246 */       int tmp = portMin;
/* 247 */       portMin = portMax;
/* 248 */       portMax = tmp;
/*     */     } 
/* 250 */     for (int port = portMin; port <= portMax; port++) {
/* 251 */       this.ports.add(Integer.valueOf(port));
/*     */     }
/* 253 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder ports(int... port) {
/* 264 */     for (int p : port) {
/* 265 */       this.ports.add(Integer.valueOf(p));
/*     */     }
/* 267 */     return this;
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
/*     */   public IRCConfigBuilder realname(String realname) {
/* 279 */     this.realname = realname;
/* 280 */     return this;
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
/*     */   public IRCConfigBuilder runtimeConfig(IRCRuntimeConfig runtimeConfig) {
/* 292 */     this.timeout = runtimeConfig.getTimeout();
/* 293 */     this.autoPong = runtimeConfig.isAutoPong();
/* 294 */     this.stripColors = runtimeConfig.isStripColorsEnabled();
/* 295 */     this.sslSupport = runtimeConfig.getSSLSupport();
/* 296 */     this.proxy = runtimeConfig.getProxy();
/* 297 */     this.trafficLogger = runtimeConfig.getTrafficLogger();
/* 298 */     this.exceptionHandler = runtimeConfig.getExceptionHandler();
/* 299 */     return this;
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
/*     */   public IRCConfigBuilder serverConfig(IRCServerConfig serverConfig) {
/* 311 */     this.host = serverConfig.getHost();
/* 312 */     ports(serverConfig.getPorts());
/* 313 */     this.password = serverConfig.getPassword();
/* 314 */     this.nick = serverConfig.getNick();
/* 315 */     this.username = serverConfig.getUsername();
/* 316 */     this.realname = serverConfig.getRealname();
/* 317 */     this.encoding = serverConfig.getEncoding();
/* 318 */     return this;
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
/*     */   public IRCConfigBuilder socksProxy(String socksProxyHost, int socksProxyPort) {
/* 332 */     if (socksProxyHost == null) {
/* 333 */       throw new IllegalArgumentException("socksProxyHost must be non-null, non-empty");
/*     */     }
/* 335 */     this.proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(socksProxyHost, socksProxyPort));
/* 336 */     return this;
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
/*     */   public IRCConfigBuilder sslSupport(IRCSSLSupport sslSupport) {
/* 348 */     this.sslSupport = sslSupport;
/* 349 */     return this;
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
/*     */   public IRCConfigBuilder stripColors(boolean stripColors) {
/* 362 */     this.stripColors = stripColors;
/* 363 */     return this;
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
/*     */   public IRCConfigBuilder timeout(int millis) {
/* 375 */     this.timeout = millis;
/* 376 */     return this;
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
/*     */   public IRCConfigBuilder trafficLogger(IRCTrafficLogger trafficLogger) {
/* 388 */     this.trafficLogger = trafficLogger;
/* 389 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public IRCConfigBuilder username(String username) {
/* 400 */     this.username = username;
/* 401 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\IRCConfigBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */