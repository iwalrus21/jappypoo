/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatColor;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class Unfriend extends Command {
/*    */   private static final String DEFAULT_ERROR = "Incorrect syntax. .unfriend <name>";
/*    */   
/*    */   public Unfriend() {
/* 12 */     super(".unfriend", "Lets you remove friends.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRun(String[] args) {
/* 17 */     if (args.length != 1) {
/* 18 */       ChatUtils.sendLocalMessage("Incorrect syntax. .unfriend <name>", ChatColor.RED);
/*    */     } else {
/* 20 */       String name = args[0];
/*    */       
/* 22 */       if (!(AntiAFK.getInstance()).friends.stream().anyMatch(name::equalsIgnoreCase)) {
/* 23 */         ChatUtils.sendLocalMessage("The user entered is not on the friend list.", ChatColor.RED);
/*    */         
/*    */         return;
/*    */       } 
/* 27 */       (AntiAFK.getInstance()).friends.removeIf(name::equalsIgnoreCase);
/* 28 */       ChatUtils.sendLocalMessage("Successfully removed " + name + " from the friend list.", ChatColor.GREEN);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\Unfriend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */