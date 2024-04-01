/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatColor;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class InventoryCleaner
/*    */   extends Command {
/*    */   public InventoryCleaner() {
/* 12 */     super(".inventorycleaner", "Lets you manage the inventory cleaner module.");
/*    */   }
/*    */   private static final String DEFAULT_ERROR = "Incorrect syntax. .inventorycleaner <add|remove|list>";
/*    */   
/*    */   public void onRun(String[] args) {
/* 17 */     if (args.length == 0 || args.length > 3) {
/* 18 */       ChatUtils.sendLocalMessage("Incorrect syntax. .inventorycleaner <add|remove|list>", ChatColor.RED);
/* 19 */     } else if (args.length == 1) {
/* 20 */       if (args[0].equalsIgnoreCase("add")) {
/* 21 */         ChatUtils.sendLocalMessage("Incorrect syntax. .inventorycleaner add <item id>", ChatColor.RED);
/* 22 */       } else if (args[0].equalsIgnoreCase("remove")) {
/* 23 */         ChatUtils.sendLocalMessage("Incorrect syntax. .inventorycleaner remove <item id>", ChatColor.RED);
/* 24 */       } else if (args[0].equalsIgnoreCase("list")) {
/* 25 */         if (((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.isEmpty()) {
/* 26 */           ChatUtils.sendLocalMessage("You haven't added any trash items yet.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 30 */         StringBuilder builder = new StringBuilder("Currently added trash items: ");
/*    */         
/* 32 */         boolean first = true;
/*    */         
/* 34 */         for (Iterator<Integer> iterator = ((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.iterator(); iterator.hasNext(); ) { int block = ((Integer)iterator.next()).intValue();
/* 35 */           if (!first) {
/* 36 */             builder.append(", ");
/*    */           }
/*    */           
/* 39 */           builder.append(block);
/* 40 */           first = false; }
/*    */ 
/*    */         
/* 43 */         builder.append(".");
/*    */         
/* 45 */         ChatUtils.sendLocalMessage(builder.toString(), ChatColor.YELLOW);
/*    */       } else {
/*    */         
/* 48 */         ChatUtils.sendLocalMessage("Incorrect syntax. .inventorycleaner <add|remove|list>", ChatColor.RED);
/*    */       } 
/* 50 */     } else if (args.length == 2) {
/* 51 */       if (args[0].equalsIgnoreCase("add")) {
/* 52 */         if (!isInteger(args[1])) {
/* 53 */           ChatUtils.sendLocalMessage("The trash item entered must be a integer, try again.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 57 */         Integer block = Integer.valueOf(Integer.parseInt(args[1]));
/*    */         
/* 59 */         if (((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.contains(block)) {
/* 60 */           ChatUtils.sendLocalMessage("The trash item entered is already on the trash list.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 64 */         ((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.add(block);
/* 65 */         ChatUtils.sendLocalMessage("Successfully added " + block + " to the trash list.", ChatColor.GREEN);
/* 66 */       } else if (args[0].equalsIgnoreCase("remove")) {
/* 67 */         if (!isInteger(args[1])) {
/* 68 */           ChatUtils.sendLocalMessage("The trash item entered must be a integer, try again.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 72 */         Integer block = Integer.valueOf(Integer.parseInt(args[1]));
/*    */         
/* 74 */         if (!((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.contains(block)) {
/* 75 */           ChatUtils.sendLocalMessage("The trash item entered is not on the trash list.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 79 */         ((me.onefightonewin.feature.types.InventoryCleaner)AntiAFK.getInstance().getFeature(me.onefightonewin.feature.types.InventoryCleaner.class)).trashItems.remove(block);
/* 80 */         ChatUtils.sendLocalMessage("Successfully removed " + block + " from the trash list.", ChatColor.GREEN);
/*    */       } else {
/* 82 */         ChatUtils.sendLocalMessage("Incorrect syntax. .inventorycleaner <add|remove|list>", ChatColor.RED);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private boolean isInteger(String string) {
/*    */     try {
/* 89 */       Integer.valueOf(string);
/* 90 */       return true;
/* 91 */     } catch (NumberFormatException e) {
/* 92 */       return false;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\InventoryCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */