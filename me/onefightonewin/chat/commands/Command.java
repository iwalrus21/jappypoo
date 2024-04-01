/*    */ package me.onefightonewin.chat.commands;
/*    */ 
/*    */ public abstract class Command implements CommandImpl {
/*    */   private String command;
/*    */   private String description;
/*    */   
/*    */   public Command(String command, String description) {
/*  8 */     this.command = command;
/*  9 */     this.description = description;
/*    */   }
/*    */   
/*    */   public String getCommand() {
/* 13 */     return this.command;
/*    */   }
/*    */   
/*    */   public String getDescription() {
/* 17 */     return this.description;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\Command.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */