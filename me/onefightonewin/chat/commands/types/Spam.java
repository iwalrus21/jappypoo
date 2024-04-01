/*     */ package me.onefightonewin.chat.commands.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.chat.ChatUtils;
/*     */ import me.onefightonewin.chat.commands.Command;
/*     */ import net.minecraft.client.Minecraft;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Spam
/*     */   extends Command
/*     */ {
/*     */   private static final String DEFAULT_ERROR = "Incorrect syntax. .spam <add|interval|toggle|clear>";
/*     */   List<String> spamList;
/*     */   Thread spamThread;
/*     */   boolean state;
/*  20 */   int interval = 3;
/*     */   
/*     */   public Spam() {
/*  23 */     super(".spam", "Spams a message / command in the server.");
/*  24 */     this.spamList = new CopyOnWriteArrayList<>();
/*  25 */     this.spamThread = new Thread(new SpamThread());
/*  26 */     this.spamThread.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRun(String[] args) {
/*  31 */     if (args.length == 0) {
/*  32 */       ChatUtils.sendLocalMessage("Incorrect syntax. .spam <add|interval|toggle|clear>", ChatColor.RED);
/*  33 */     } else if (args.length == 1) {
/*  34 */       if (args[0].equalsIgnoreCase("add")) {
/*  35 */         ChatUtils.sendLocalMessage("Incorrect syntax. ." + getCommand() + " add <message / command>", ChatColor.RED);
/*  36 */       } else if (args[0].equalsIgnoreCase("toggle")) {
/*  37 */         this.state = !this.state;
/*  38 */         ChatUtils.sendLocalMessage("The spam has successfully been turned " + (this.state ? "ON" : "OFF") + ".", ChatColor.GREEN);
/*  39 */       } else if (args[0].equalsIgnoreCase("clear")) {
/*  40 */         this.spamList.clear();
/*  41 */         ChatUtils.sendLocalMessage("The spam list has been successfully cleared.", ChatColor.GREEN);
/*  42 */       } else if (args[0].equalsIgnoreCase("interval")) {
/*  43 */         ChatUtils.sendLocalMessage("Incorrect syntax. ." + getCommand() + " interval <seconds between messages>", ChatColor.RED);
/*  44 */       } else if (args[0].equalsIgnoreCase("list")) {
/*  45 */         if (this.spamList.isEmpty()) {
/*  46 */           ChatUtils.sendLocalMessage("You haven't added any spam orders yet.", ChatColor.RED);
/*     */           
/*     */           return;
/*     */         } 
/*  50 */         StringBuilder builder = new StringBuilder("Currently added spam orders: ");
/*     */         
/*  52 */         boolean first = true;
/*     */         
/*  54 */         for (String message : this.spamList) {
/*  55 */           if (!first) {
/*  56 */             builder.append(", ");
/*     */           }
/*     */           
/*  59 */           builder.append(message);
/*  60 */           first = false;
/*     */         } 
/*     */         
/*  63 */         builder.append(".");
/*     */         
/*  65 */         ChatUtils.sendLocalMessage(builder.toString(), ChatColor.YELLOW);
/*     */       } else {
/*     */         
/*  68 */         ChatUtils.sendLocalMessage("Incorrect syntax. .spam <add|interval|toggle|clear>", ChatColor.RED);
/*     */       } 
/*  70 */     } else if (args.length >= 2) {
/*  71 */       if (args[0].equalsIgnoreCase("interval")) {
/*  72 */         if (!isInteger(args[1])) {
/*  73 */           ChatUtils.sendLocalMessage("The interval is not a number.", ChatColor.RED);
/*     */           
/*     */           return;
/*     */         } 
/*  77 */         Integer seconds = Integer.valueOf(Integer.parseInt(args[1]));
/*     */         
/*  79 */         if (seconds.intValue() >= 1) {
/*  80 */           this.interval = seconds.intValue();
/*  81 */           ChatUtils.sendLocalMessage("Successfully changed the interval to " + this.interval + " seconds.", ChatColor.GREEN);
/*     */         } else {
/*  83 */           ChatUtils.sendLocalMessage("The interval must be 1 second or above.", ChatColor.RED);
/*     */         } 
/*  85 */       } else if (args[0].equalsIgnoreCase("add")) {
/*  86 */         StringBuilder builder = new StringBuilder();
/*     */         
/*  88 */         for (int i = 1; i < args.length; i++) {
/*  89 */           if (!builder.toString().isEmpty()) {
/*  90 */             builder.append(" ");
/*     */           }
/*     */           
/*  93 */           builder.append(args[i]);
/*     */         } 
/*     */         
/*  96 */         if (builder.toString().length() == 0) {
/*  97 */           ChatUtils.sendLocalMessage("Incorrect usage of the command, try .say <message>.");
/*     */           
/*     */           return;
/*     */         } 
/* 101 */         if (this.spamList.contains(builder.toString())) {
/* 102 */           ChatUtils.sendLocalMessage("The message entered is already on the spam list.", ChatColor.RED);
/*     */           
/*     */           return;
/*     */         } 
/* 106 */         this.spamList.add(builder.toString());
/* 107 */         ChatUtils.sendLocalMessage("Successfully added \"" + builder.toString() + "\" to the spam list.", ChatColor.GREEN);
/*     */       } else {
/* 109 */         ChatUtils.sendLocalMessage("Incorrect syntax. .spam <add|interval|toggle|clear>", ChatColor.RED);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isInteger(String string) {
/*     */     try {
/* 116 */       Integer.valueOf(string);
/* 117 */       return true;
/* 118 */     } catch (NumberFormatException e) {
/* 119 */       return false;
/*     */     } 
/*     */   }
/*     */   
/*     */   class SpamThread
/*     */     implements Runnable {
/*     */     public void run() {
/*     */       while (true) {
/* 127 */         while (Spam.this.state) {
/* 128 */           int id = 0;
/*     */           
/* 130 */           for (String message : Spam.this.spamList) {
/* 131 */             if (!Spam.this.state || (Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/*     */               break;
/*     */             }
/*     */             
/* 135 */             String toSend = message;
/*     */             
/* 137 */             if (!toSend.startsWith("/")) {
/* 138 */               toSend = toSend + " " + id;
/*     */             }
/*     */             
/* 141 */             ChatUtils.sendMessage(toSend);
/* 142 */             id++;
/*     */             
/*     */             try {
/* 145 */               Thread.sleep((Spam.this.interval * 1000));
/* 146 */             } catch (InterruptedException e) {
/* 147 */               e.printStackTrace();
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         try {
/* 152 */           Thread.sleep(1000L);
/* 153 */         } catch (InterruptedException e) {
/* 154 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\Spam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */