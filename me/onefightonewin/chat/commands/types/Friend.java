/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatColor;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class Friend extends Command {
/*    */   private static final String DEFAULT_ERROR = "Incorrect syntax. .friend <name>";
/*    */   
/*    */   public Friend() {
/* 12 */     super(".friend", "Lets you add friends.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRun(String[] args) {
/* 17 */     if (args.length != 1) {
/* 18 */       ChatUtils.sendLocalMessage("Incorrect syntax. .friend <name>", ChatColor.RED);
/*    */     } else {
/* 20 */       String name = args[0];
/*    */       
/* 22 */       if ((AntiAFK.getInstance()).friends.stream().anyMatch(name::equalsIgnoreCase)) {
/* 23 */         ChatUtils.sendLocalMessage("The user entered is already on the friend list.", ChatColor.RED);
/*    */         
/*    */         return;
/*    */       } 
/* 27 */       (AntiAFK.getInstance()).friends.add(name);
/* 28 */       ChatUtils.sendLocalMessage("Successfully added " + name + " to the friend list.", ChatColor.GREEN);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\Friend.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */