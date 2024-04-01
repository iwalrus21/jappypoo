/*    */ package me.onefightonewin.chat.commands.types;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.chat.ChatColor;
/*    */ import me.onefightonewin.chat.ChatUtils;
/*    */ import me.onefightonewin.chat.commands.Command;
/*    */ 
/*    */ public class XRay
/*    */   extends Command {
/*    */   public XRay() {
/* 12 */     super(".xray", "Lets you manage the xray module.");
/*    */   }
/*    */   private static final String DEFAULT_ERROR = "Incorrect syntax. .xray <add|remove|list>";
/*    */   
/*    */   public void onRun(String[] args) {
/* 17 */     if (args.length == 0 || args.length > 3) {
/* 18 */       ChatUtils.sendLocalMessage("Incorrect syntax. .xray <add|remove|list>", ChatColor.RED);
/* 19 */     } else if (args.length == 1) {
/* 20 */       if (args[0].equalsIgnoreCase("add")) {
/* 21 */         ChatUtils.sendLocalMessage("Incorrect syntax. .xray add <block id>", ChatColor.RED);
/* 22 */       } else if (args[0].equalsIgnoreCase("remove")) {
/* 23 */         ChatUtils.sendLocalMessage("Incorrect syntax. .xray remove <block id>", ChatColor.RED);
/* 24 */       } else if (args[0].equalsIgnoreCase("list")) {
/* 25 */         if ((AntiAFK.getInstance()).xray.isEmpty()) {
/* 26 */           ChatUtils.sendLocalMessage("You haven't added any xray blocks yet.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 30 */         StringBuilder builder = new StringBuilder("Currently added XRay blocks: ");
/*    */         
/* 32 */         boolean first = true;
/*    */         
/* 34 */         for (Iterator<Integer> iterator = (AntiAFK.getInstance()).xray.iterator(); iterator.hasNext(); ) { int block = ((Integer)iterator.next()).intValue();
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
/* 48 */         ChatUtils.sendLocalMessage("Incorrect syntax. .xray <add|remove|list>", ChatColor.RED);
/*    */       } 
/* 50 */     } else if (args.length == 2) {
/* 51 */       if (args[0].equalsIgnoreCase("add")) {
/* 52 */         if (!isInteger(args[1])) {
/* 53 */           ChatUtils.sendLocalMessage("The xray block entered must be a integer, try again.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 57 */         Integer block = Integer.valueOf(Integer.parseInt(args[1]));
/*    */         
/* 59 */         if ((AntiAFK.getInstance()).xray.contains(block)) {
/* 60 */           ChatUtils.sendLocalMessage("The block entered is already on the xray list.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 64 */         (AntiAFK.getInstance()).xray.add(block);
/* 65 */         ChatUtils.sendLocalMessage("Successfully added " + block + " to the xray list.", ChatColor.GREEN);
/* 66 */       } else if (args[0].equalsIgnoreCase("remove")) {
/* 67 */         if (!isInteger(args[1])) {
/* 68 */           ChatUtils.sendLocalMessage("The xray block entered must be a integer, try again.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 72 */         Integer block = Integer.valueOf(Integer.parseInt(args[1]));
/*    */         
/* 74 */         if (!(AntiAFK.getInstance()).xray.contains(block)) {
/* 75 */           ChatUtils.sendLocalMessage("The block entered is not on the xray list.", ChatColor.RED);
/*    */           
/*    */           return;
/*    */         } 
/* 79 */         (AntiAFK.getInstance()).xray.remove(block);
/* 80 */         ChatUtils.sendLocalMessage("Successfully removed " + block + " from the xray list.", ChatColor.GREEN);
/*    */       } else {
/* 82 */         ChatUtils.sendLocalMessage("Incorrect syntax. .xray <add|remove|list>", ChatColor.RED);
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


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\commands\types\XRay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */