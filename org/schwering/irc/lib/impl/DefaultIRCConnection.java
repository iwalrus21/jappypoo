/*     */ package org.schwering.irc.lib.impl;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.security.KeyManagementException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import org.schwering.irc.lib.IRCConnection;
/*     */ import org.schwering.irc.lib.IRCEventListener;
/*     */ import org.schwering.irc.lib.IRCExceptionHandler;
/*     */ import org.schwering.irc.lib.IRCRuntimeConfig;
/*     */ import org.schwering.irc.lib.IRCServerConfig;
/*     */ import org.schwering.irc.lib.IRCTrafficLogger;
/*     */ import org.schwering.irc.lib.IRCUser;
/*     */ import org.schwering.irc.lib.util.IRCModeParser;
/*     */ import org.schwering.irc.lib.util.IRCParser;
/*     */ import org.schwering.irc.lib.util.IRCUtil;
/*     */ import org.schwering.irc.lib.util.LoggingReader;
/*     */ import org.schwering.irc.lib.util.LoggingWriter;
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
/*     */ public class DefaultIRCConnection
/*     */   implements IRCConnection
/*     */ {
/*     */   private Socket socket;
/*     */   
/*     */   protected class Consumer
/*     */     implements Runnable
/*     */   {
/*     */     public void run() {
/*     */       try {
/*     */         String line;
/*  73 */         while ((line = DefaultIRCConnection.this.in.readLine()) != null) {
/*  74 */           DefaultIRCConnection.this.get(line);
/*     */         }
/*  76 */       } catch (IOException exc) {
/*  77 */         DefaultIRCConnection.this.handleException(exc);
/*  78 */         DefaultIRCConnection.this.close();
/*     */       } finally {
/*  80 */         DefaultIRCConnection.this.close();
/*     */       } 
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 101 */   private byte level = 0;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private BufferedReader in;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private PrintWriter out;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 116 */   private IRCEventListener[] listeners = new IRCEventListener[0];
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IRCTrafficLogger trafficLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IRCExceptionHandler exceptionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private final IRCServerConfig serverConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   private final IRCRuntimeConfig runtimeConfig;
/*     */ 
/*     */ 
/*     */   
/*     */   private String nick;
/*     */ 
/*     */ 
/*     */   
/*     */   private Thread thread;
/*     */ 
/*     */ 
/*     */   
/*     */   private int remotePort;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIRCConnection(IRCServerConfig serverConfig, IRCRuntimeConfig runtimeConfig) {
/* 153 */     int[] ports = serverConfig.getPorts();
/* 154 */     if (serverConfig.getHost() == null || ports == null || ports.length == 0) {
/* 155 */       throw new IllegalArgumentException("Host and ports may not be null.");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     this.serverConfig = (serverConfig instanceof DefaultIRCConfig || serverConfig instanceof DefaultIRCServerConfig) ? serverConfig : new DefaultIRCServerConfig(serverConfig);
/*     */ 
/*     */     
/* 164 */     this.runtimeConfig = (runtimeConfig instanceof DefaultIRCConfig || runtimeConfig instanceof DefaultIRCRuntimeConfig) ? runtimeConfig : new DefaultIRCRuntimeConfig(runtimeConfig);
/*     */ 
/*     */     
/* 167 */     this.nick = serverConfig.getNick();
/* 168 */     this.trafficLogger = runtimeConfig.getTrafficLogger();
/* 169 */     this.exceptionHandler = runtimeConfig.getExceptionHandler();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect() throws IOException, KeyManagementException, NoSuchAlgorithmException {
/* 177 */     if (this.level != 0) {
/* 178 */       throw new SocketException("Socket closed or already open (" + this.level + ")");
/*     */     }
/* 180 */     SocketFactory socketFactory = new SocketFactory(this.runtimeConfig.getTimeout(), this.runtimeConfig.getProxy(), this.runtimeConfig.getSSLSupport());
/*     */     
/* 182 */     IOException exception = null;
/* 183 */     String host = this.serverConfig.getHost();
/* 184 */     for (int i = 0; i < this.serverConfig.getPortsCount() && this.socket == null; i++) {
/*     */       try {
/* 186 */         int port = this.serverConfig.getPortAt(i);
/* 187 */         this.socket = socketFactory.createSocket(host, port);
/* 188 */         this.remotePort = port;
/* 189 */         exception = null;
/* 190 */       } catch (IOException exc) {
/* 191 */         if (this.socket != null) {
/* 192 */           this.socket.close();
/*     */         }
/* 194 */         this.socket = null;
/* 195 */         exception = exc;
/*     */       } 
/*     */     } 
/* 198 */     if (exception != null)
/*     */     {
/* 200 */       throw exception;
/*     */     }
/*     */     
/* 203 */     this.level = 1;
/* 204 */     String encoding = this.serverConfig.getEncoding();
/* 205 */     if (this.trafficLogger != null) {
/* 206 */       this.in = (BufferedReader)new LoggingReader(new InputStreamReader(this.socket.getInputStream(), encoding), this.trafficLogger);
/* 207 */       this.out = (PrintWriter)new LoggingWriter(new OutputStreamWriter(this.socket.getOutputStream(), encoding), this.trafficLogger);
/*     */     } else {
/* 209 */       this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream(), encoding));
/* 210 */       this.out = new PrintWriter(new OutputStreamWriter(this.socket.getOutputStream(), encoding));
/*     */     } 
/*     */     
/* 213 */     this.thread = createThread();
/* 214 */     this.thread.start();
/* 215 */     register();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Thread createThread() {
/* 222 */     return new Thread(createConsumer(), "irc://" + this.serverConfig.getUsername() + "@" + this.serverConfig.getHost() + ":" + this.remotePort);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected Runnable createConsumer() {
/* 230 */     return new Consumer();
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
/*     */   private void register() {
/* 244 */     String pass = this.serverConfig.getPassword();
/* 245 */     if (pass != null) {
/* 246 */       send("PASS " + pass);
/*     */     }
/* 248 */     send("NICK " + this.serverConfig.getNick());
/* 249 */     send("USER " + this.serverConfig.getUsername() + " " + this.socket.getLocalAddress().getHostAddress() + " " + this.serverConfig
/* 250 */         .getHost() + " :" + this.serverConfig.getRealname());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void send(String line) {
/*     */     try {
/* 259 */       this.out.write(line + "\r\n");
/* 260 */       this.out.flush();
/* 261 */       if (this.level == 1) {
/* 262 */         IRCParser p = new IRCParser(line);
/* 263 */         if ("NICK".equalsIgnoreCase(p.getCommand()))
/* 264 */           this.nick = p.getParameter(1).trim(); 
/*     */       } 
/* 266 */     } catch (Exception exc) {
/* 267 */       handleException(exc);
/* 268 */       throw new RuntimeException(exc);
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
/*     */   private synchronized void get(String line) {
/*     */     IRCParser p;
/*     */     try {
/* 283 */       p = new IRCParser(line, this.runtimeConfig.isStripColorsEnabled());
/* 284 */     } catch (Exception exc) {
/*     */       return;
/*     */     } 
/* 287 */     String command = p.getCommand();
/*     */ 
/*     */     
/* 290 */     if ("PRIVMSG".equalsIgnoreCase(command)) {
/*     */       
/* 292 */       IRCUser user = p.getUser();
/* 293 */       String middle = p.getMiddle();
/* 294 */       String trailing = p.getTrailing();
/* 295 */       for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 296 */         this.listeners[i].onPrivmsg(middle, user, trailing);
/*     */       }
/* 298 */     } else if ("MODE".equalsIgnoreCase(command)) {
/*     */       
/* 300 */       String chan = p.getParameter(1);
/* 301 */       if (IRCUtil.isChan(chan)) {
/* 302 */         IRCUser user = p.getUser();
/* 303 */         String param2 = p.getParameter(2);
/* 304 */         String paramsFrom3 = p.getParametersFrom(3);
/* 305 */         for (int i = this.listeners.length - 1; i >= 0; i--)
/* 306 */           this.listeners[i].onMode(chan, user, new IRCModeParser(param2, paramsFrom3)); 
/*     */       } else {
/* 308 */         IRCUser user = p.getUser();
/* 309 */         String paramsFrom2 = p.getParametersFrom(2);
/* 310 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 311 */           this.listeners[i].onMode(user, chan, paramsFrom2);
/*     */         }
/*     */       } 
/* 314 */     } else if ("PING".equalsIgnoreCase(command)) {
/*     */       
/* 316 */       String ping = p.getTrailing();
/* 317 */       if (this.runtimeConfig.isAutoPong()) {
/* 318 */         doPong(ping);
/*     */       } else {
/* 320 */         for (int i = this.listeners.length - 1; i >= 0; i--)
/* 321 */           this.listeners[i].onPing(ping); 
/*     */       } 
/* 323 */       if (this.level == 1) {
/* 324 */         this.level = 2;
/* 325 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 326 */           this.listeners[i].onRegistered();
/*     */         }
/*     */       } 
/* 329 */     } else if ("JOIN".equalsIgnoreCase(command)) {
/*     */       
/* 331 */       IRCUser user = p.getUser();
/* 332 */       String trailing = p.getTrailing();
/* 333 */       for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 334 */         this.listeners[i].onJoin(trailing, user);
/*     */       }
/* 336 */     } else if ("NICK".equalsIgnoreCase(command)) {
/*     */       
/* 338 */       IRCUser user = p.getUser();
/* 339 */       String changingNick = p.getNick();
/* 340 */       String newNick = p.getTrailing();
/* 341 */       if (changingNick.equalsIgnoreCase(this.nick))
/* 342 */         this.nick = newNick; 
/* 343 */       for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 344 */         this.listeners[i].onNick(user, newNick);
/*     */       }
/* 346 */     } else if ("QUIT".equalsIgnoreCase(command)) {
/*     */       
/* 348 */       IRCUser user = p.getUser();
/* 349 */       String trailing = p.getTrailing();
/* 350 */       for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 351 */         this.listeners[i].onQuit(user, trailing);
/*     */       }
/* 353 */     } else if ("PART".equalsIgnoreCase(command)) {
/*     */       
/* 355 */       IRCUser user = p.getUser();
/* 356 */       String chan = p.getParameter(1);
/* 357 */       String msg = (p.getParameterCount() > 1) ? p.getTrailing() : "";
/*     */ 
/*     */ 
/*     */       
/* 361 */       for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 362 */         this.listeners[i].onPart(chan, user, msg);
/*     */       }
/* 364 */     } else if ("NOTICE".equalsIgnoreCase(command)) {
/*     */       
/* 366 */       IRCUser user = p.getUser();
/* 367 */       String middle = p.getMiddle();
/* 368 */       String trailing = p.getTrailing();
/* 369 */       for (int i = this.listeners.length - 1; i >= 0; i--)
/* 370 */         this.listeners[i].onNotice(middle, user, trailing); 
/*     */     } else {
/* 372 */       int reply; if ((reply = IRCUtil.parseInt(command)) >= 1 && reply < 400) {
/*     */         
/* 374 */         String potNick = p.getParameter(1);
/* 375 */         if ((this.level == 1 || this.level == 2) && this.nick.length() > potNick.length() && this.nick
/* 376 */           .substring(0, potNick.length()).equalsIgnoreCase(potNick)) {
/* 377 */           this.nick = potNick;
/* 378 */           if (this.level == 2) {
/* 379 */             this.level = 3;
/*     */           }
/*     */         } 
/* 382 */         if (this.level == 1 && this.nick.equals(potNick)) {
/* 383 */           this.level = 2;
/* 384 */           for (int j = this.listeners.length - 1; j >= 0; j--) {
/* 385 */             this.listeners[j].onRegistered();
/*     */           }
/*     */         } 
/* 388 */         String middle = p.getMiddle();
/* 389 */         String trailing = p.getTrailing();
/* 390 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 391 */           this.listeners[i].onReply(reply, middle, trailing);
/*     */         }
/* 393 */       } else if (reply >= 400 && reply < 600) {
/*     */         
/* 395 */         String trailing = p.getTrailing();
/* 396 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 397 */           this.listeners[i].onError(reply, trailing);
/*     */         }
/* 399 */       } else if ("KICK".equalsIgnoreCase(command)) {
/*     */         
/* 401 */         IRCUser user = p.getUser();
/* 402 */         String param1 = p.getParameter(1);
/* 403 */         String param2 = p.getParameter(2);
/* 404 */         String msg = (p.getParameterCount() > 2) ? p.getTrailing() : "";
/* 405 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 406 */           this.listeners[i].onKick(param1, user, param2, msg);
/*     */         }
/* 408 */       } else if ("INVITE".equalsIgnoreCase(command)) {
/*     */         
/* 410 */         IRCUser user = p.getUser();
/* 411 */         String middle = p.getMiddle();
/* 412 */         String trailing = p.getTrailing();
/* 413 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 414 */           this.listeners[i].onInvite(trailing, user, middle);
/*     */         }
/* 416 */       } else if ("TOPIC".equalsIgnoreCase(command)) {
/*     */         
/* 418 */         IRCUser user = p.getUser();
/* 419 */         String middle = p.getMiddle();
/* 420 */         String trailing = p.getTrailing();
/* 421 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 422 */           this.listeners[i].onTopic(middle, user, trailing);
/*     */         }
/* 424 */       } else if ("ERROR".equalsIgnoreCase(command)) {
/*     */         
/* 426 */         String trailing = p.getTrailing();
/* 427 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 428 */           this.listeners[i].onError(trailing);
/*     */         }
/*     */       } else {
/*     */         
/* 432 */         String prefix = p.getPrefix();
/* 433 */         String middle = p.getMiddle();
/* 434 */         String trailing = p.getTrailing();
/* 435 */         for (int i = this.listeners.length - 1; i >= 0; i--) {
/* 436 */           this.listeners[i].unknown(prefix, command, middle, trailing);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void close() {
/*     */     try {
/* 447 */       if (!this.thread.isInterrupted())
/* 448 */         this.thread.interrupt(); 
/* 449 */     } catch (Exception exc) {
/* 450 */       handleException(exc);
/*     */     } 
/*     */     try {
/* 453 */       if (this.socket != null)
/* 454 */         this.socket.close(); 
/* 455 */     } catch (Exception exc) {
/* 456 */       handleException(exc);
/*     */     } 
/*     */     try {
/* 459 */       if (this.out != null)
/* 460 */         this.out.close(); 
/* 461 */     } catch (Exception exc) {
/* 462 */       handleException(exc);
/*     */     } 
/*     */     try {
/* 465 */       if (this.in != null)
/* 466 */         this.in.close(); 
/* 467 */     } catch (Exception exc) {
/* 468 */       handleException(exc);
/*     */     } 
/* 470 */     if (this.level != -1) {
/* 471 */       this.level = -1;
/* 472 */       for (int i = this.listeners.length - 1; i >= 0; i--)
/* 473 */         this.listeners[i].onDisconnected(); 
/*     */     } 
/* 475 */     this.socket = null;
/* 476 */     this.in = null;
/* 477 */     this.out = null;
/* 478 */     this.listeners = new IRCEventListener[0];
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleException(Exception exc) {
/* 485 */     if (this.exceptionHandler != null) {
/* 486 */       this.exceptionHandler.exception(this, exc);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addIRCEventListener(IRCEventListener l) {
/* 495 */     if (l == null)
/* 496 */       throw new IllegalArgumentException("Listener is null."); 
/* 497 */     int len = this.listeners.length;
/* 498 */     IRCEventListener[] oldListeners = this.listeners;
/* 499 */     this.listeners = new IRCEventListener[len + 1];
/* 500 */     System.arraycopy(oldListeners, 0, this.listeners, 0, len);
/* 501 */     this.listeners[len] = l;
/*     */   }
/*     */   
/*     */   public synchronized void addIRCEventListener(IRCEventListener l, int i) {
/* 505 */     if (l == null)
/* 506 */       throw new IllegalArgumentException("Listener is null."); 
/* 507 */     if (i < 0 || i > this.listeners.length)
/* 508 */       throw new IndexOutOfBoundsException("i is not in range"); 
/* 509 */     int len = this.listeners.length;
/* 510 */     IRCEventListener[] oldListeners = this.listeners;
/* 511 */     this.listeners = new IRCEventListener[len + 1];
/* 512 */     if (i > 0)
/* 513 */       System.arraycopy(oldListeners, 0, this.listeners, 0, i); 
/* 514 */     if (i < this.listeners.length)
/* 515 */       System.arraycopy(oldListeners, i, this.listeners, i + 1, len - i); 
/* 516 */     this.listeners[i] = l;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized boolean removeIRCEventListener(IRCEventListener l) {
/* 524 */     if (l == null)
/* 525 */       return false; 
/* 526 */     int index = -1;
/* 527 */     for (int i = 0; i < this.listeners.length; i++) {
/* 528 */       if (this.listeners[i].equals(l)) {
/* 529 */         index = i; break;
/*     */       } 
/*     */     } 
/* 532 */     if (index == -1)
/* 533 */       return false; 
/* 534 */     this.listeners[index] = null;
/* 535 */     int len = this.listeners.length - 1;
/* 536 */     IRCEventListener[] newListeners = new IRCEventListener[len];
/* 537 */     for (int k = 0, j = 0; k < len; j++) {
/* 538 */       if (this.listeners[j] != null)
/* 539 */         newListeners[k++] = this.listeners[j]; 
/* 540 */     }  this.listeners = newListeners;
/* 541 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isConnected() {
/* 549 */     return (this.level >= 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getNick() {
/* 557 */     return this.nick;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPort() {
/* 565 */     return (this.socket != null) ? this.socket.getPort() : 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTimeout() {
/* 573 */     if (this.socket != null) {
/*     */       try {
/* 575 */         return this.socket.getSoTimeout();
/* 576 */       } catch (IOException exc) {
/* 577 */         handleException(exc);
/* 578 */         return -1;
/*     */       } 
/*     */     }
/* 581 */     return -1;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public InetAddress getLocalAddress() {
/* 589 */     return (this.socket != null) ? this.socket.getLocalAddress() : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 594 */     return "DefaultIRCConnection [nick=" + this.nick + ", config=" + this.serverConfig + "]";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAway() {
/* 602 */     send("AWAY");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doAway(String msg) {
/* 610 */     send("AWAY :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doInvite(String nick, String chan) {
/* 619 */     send("INVITE " + nick + " " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doIson(String nick) {
/* 627 */     send("ISON " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doJoin(String chan) {
/* 635 */     send("JOIN " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doJoin(String chan, String key) {
/* 644 */     send("JOIN " + chan + " " + key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doKick(String chan, String nick) {
/* 653 */     send("KICK " + chan + " " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doKick(String chan, String nick, String msg) {
/* 662 */     send("KICK " + chan + " " + nick + " :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doList() {
/* 670 */     send("LIST");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doList(String chan) {
/* 678 */     send("LIST " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNames() {
/* 686 */     send("NAMES");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNames(String chan) {
/* 694 */     send("NAMES " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPrivmsg(String target, String msg) {
/* 703 */     send("PRIVMSG " + target + " :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doMode(String chan) {
/* 711 */     send("MODE " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doMode(String target, String mode) {
/* 720 */     send("MODE " + target + " " + mode);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNick(String nick) {
/* 728 */     send("NICK " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doNotice(String target, String msg) {
/* 737 */     send("NOTICE " + target + " :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPart(String chan) {
/* 745 */     send("PART " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPart(String chan, String msg) {
/* 754 */     send("PART " + chan + " :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doPong(String ping) {
/* 762 */     send("PONG :" + ping);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doQuit() {
/* 770 */     send("QUIT");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doQuit(String msg) {
/* 778 */     send("QUIT :" + msg);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doTopic(String chan) {
/* 786 */     send("TOPIC " + chan);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doTopic(String chan, String topic) {
/* 795 */     send("TOPIC " + chan + " :" + topic);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doWho(String criteric) {
/* 803 */     send("WHO " + criteric);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doWhois(String nick) {
/* 811 */     send("WHOIS " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doWhowas(String nick) {
/* 819 */     send("WHOWAS " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void doUserhost(String nick) {
/* 827 */     send("USERHOST " + nick);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isSSL() {
/* 835 */     return (this.runtimeConfig.getSSLSupport() != null);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\org\schwering\irc\lib\impl\DefaultIRCConnection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */