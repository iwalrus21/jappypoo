/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatColor;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class Friends extends Command {
/*    */   private static final String DEFAULT_ERROR = "Incorrect syntax. .friends <list|clear>";
/*    */   
/*    */   public Friends() {
/* 12 */     super(".friends", "Lets you clear friends.");
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRun(String[] args) {
/* 17 */     if (args.length != 1) {
/* 18 */       ChatUtils.sendLocalMessage("Incorrect syntax. .friends <list|clear>", ChatColor.RED);
/*    */     }
/* 20 */     else if (args[0].equalsIgnoreCase("clear")) {
/* 21 */       (AntiAFK.getInstance()).friends.clear();
/* 22 */       ChatUtils.sendLocalMessage("Cleared all friends.", ChatColor.RED);
/* 23 */     } else if (args[0].equalsIgnoreCase("list")) {
/* 24 */       if ((AntiAFK.getInstance()).friends.isEmpty()) {
/* 25 */         ChatUtils.sendLocalMessage("You haven't added any friends yet.", ChatColor.RED);
/*    */         
/*    */         return;
/*    */       } 
/* 29 */       StringBuilder builder = new StringBuilder("Currently added friends: ");
/*    */       
/* 31 */       boolean first = true;
/*    */       
/* 33 */       for (String friend : (AntiAFK.getInstance()).friends) {
/* 34 */         if (!first) {
/* 35 */           builder.append(", ");
/*    */         }
/*    */         
/* 38 */         builder.append(friend);
/* 39 */         first = false;
/*    */       } 
/*    */       
/* 42 */       builder.append(".");
/*    */       
/* 44 */       ChatUtils.sendLocalMessage(builder.toString(), ChatColor.YELLOW);
/*    */     } else {
/* 46 */       ChatUtils.sendLocalMessage("Incorrect syntax. .friends <list|clear>", ChatColor.RED);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\Friends.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */