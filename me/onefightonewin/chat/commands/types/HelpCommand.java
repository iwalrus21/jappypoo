/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ import me.onefightonewin.chat.commands.CommandManager;
/*    */ 
/*    */ public class HelpCommand extends Command {
/*    */   public HelpCommand() {
/* 10 */     super(".help", "Lists all existing commands.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRun(String[] args) {
/* 15 */     CommandManager commandManager = (AntiAFK.getInstance()).commandManager;
/*    */     
/* 17 */     for (Command command : commandManager.getCommands())
/* 18 */       ChatUtils.sendLocalMessage(command.getCommand() + " - " + command.getDescription()); 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\HelpCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */