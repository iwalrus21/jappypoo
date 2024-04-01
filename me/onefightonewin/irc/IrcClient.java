/*     */ package me.onefightonewin.irc;
/*     */ 
/*     */ import java.security.KeyManagementException;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.chat.ChatUtils;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import org.schwering.irc.lib.IRCConfig;
/*     */ import org.schwering.irc.lib.IRCConfigBuilder;
/*     */ import org.schwering.irc.lib.IRCConnection;
/*     */ import org.schwering.irc.lib.IRCConnectionFactory;
/*     */ import org.schwering.irc.lib.IRCTrafficLogger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IrcClient
/*     */ {
/*     */   private boolean ready = false;
/*     */   private IRCConnection connection;
/*     */   IRCTrafficLogger logger;
/*     */   
/*     */   public void init() {
/*  25 */     String name = Minecraft.getMinecraft().getSession().getProfile().getName();
/*     */     
/*  27 */     this.logger = new IRCTrafficLogger()
/*     */       {
/*     */         public void out(String line) {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         public void in(String line) {
/*  45 */           if (!IrcClient.this.ready && 
/*  46 */             line.trim().endsWith(":End of MOTD command.")) {
/*  47 */             IrcClient.this.ready = true;
/*  48 */             ChatUtils.sendLocalMessage("You are successfully connected to the chat servers! Type .say <message> to write a message to other client users.", ChatColor.GREEN);
/*     */             
/*     */             return;
/*     */           } 
/*     */           
/*  53 */           String[] parts = line.split("@");
/*     */           
/*  55 */           if (parts.length <= 1) {
/*     */             return;
/*     */           }
/*     */           
/*  59 */           int beginIndex = parts[0].indexOf(":");
/*  60 */           int endIndex = parts[0].indexOf("!");
/*     */           
/*  62 */           if (beginIndex == -1 || endIndex == -1) {
/*     */             return;
/*     */           }
/*     */           
/*  66 */           String user = parts[0].substring(beginIndex + 1, endIndex);
/*  67 */           int privmsgIndex = parts[1].indexOf("PRIVMSG");
/*     */           
/*  69 */           if (privmsgIndex == -1) {
/*     */             return;
/*     */           }
/*     */           
/*  73 */           String message = parts[1].substring(parts[1].indexOf(":", privmsgIndex) + 1);
/*     */           
/*  75 */           ChatUtils.sendLocalMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + (AntiAFK.getInstance()).name + ChatColor.AQUA + "] " + ChatColor.RED + user + ChatColor.WHITE + ": " + ChatColor.YELLOW + message);
/*     */         }
/*     */       };
/*     */     
/*  79 */     IRCConfig config = IRCConfigBuilder.newBuilder().host("irc.ircnet.com").port(6667).nick(name).username(name).realname(name).trafficLogger(this.logger).build();
/*  80 */     this.connection = IRCConnectionFactory.newConnection(config);
/*     */     
/*     */     try {
/*  83 */       this.connection.connect();
/*  84 */       this.connection.doJoin("#" + (AntiAFK.getInstance()).name);
/*  85 */     } catch (KeyManagementException|java.security.NoSuchAlgorithmException|java.io.IOException e) {
/*  86 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void sendMessage(String message) {
/*  91 */     if (!this.ready || this.connection == null || !this.connection.isConnected()) {
/*  92 */       ChatUtils.sendLocalMessage("Not ready yet! Try again in a few minutes since we're still connecting to the chat server.", ChatColor.RED); return;
/*     */     } 
/*  94 */     if (this.ready && !this.connection.isConnected()) {
/*  95 */       ChatUtils.sendLocalMessage("IRC connection was closed, we're trying to revive it, we will let you know once its back up!.", ChatColor.RED);
/*  96 */       this.ready = false;
/*  97 */       init();
/*     */     } 
/*     */     
/* 100 */     ChatUtils.sendLocalMessage(ChatColor.AQUA + "[" + ChatColor.GOLD + (AntiAFK.getInstance()).name + ChatColor.AQUA + "] " + ChatColor.RED + Minecraft.getMinecraft().getSession().getProfile().getName() + ChatColor.WHITE + ": " + ChatColor.YELLOW + message);
/* 101 */     this.connection.doPrivmsg("#" + (AntiAFK.getInstance()).name, message);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\irc\IrcClient.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */