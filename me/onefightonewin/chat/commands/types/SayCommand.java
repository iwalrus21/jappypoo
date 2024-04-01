/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class SayCommand extends Command {
/*    */   public SayCommand() {
/*  9 */     super("$", "Sends a message that only client users can see.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRun(String[] args) {
/* 14 */     StringBuilder builder = new StringBuilder();
/*    */     
/* 16 */     for (String arg : args) {
/* 17 */       if (!builder.toString().isEmpty()) {
/* 18 */         builder.append(" ");
/*    */       }
/*    */       
/* 21 */       builder.append(arg);
/*    */     } 
/*    */     
/* 24 */     if (builder.toString().length() == 0) {
/* 25 */       ChatUtils.sendLocalMessage("Incorrect usage of the command, try .say <message>.");
/*    */       
/*    */       return;
/*    */     } 
/* 29 */     String message = builder.toString();
/* 30 */     (AntiAFK.getInstance()).ircBot.sendMessage(message);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\SayCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */