/*    */ package me.onefightonewin.chat;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.util.ChatComponentText;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ 
/*    */ public class ChatUtils
/*    */ {
/*    */   public static void sendMessage(String message) {
/* 10 */     if ((Minecraft.getMinecraft()).player != null)
/* 11 */       (Minecraft.getMinecraft()).player.sendChatMessage(message); 
/*    */   }
/*    */   
/*    */   public static void sendLocalMessage(String message, ChatColor color) {
/* 15 */     if ((Minecraft.getMinecraft()).player != null)
/* 16 */       (Minecraft.getMinecraft()).ingameGUI.getChatGUI().printChatMessage((IChatComponent)new ChatComponentText(color + message.replaceAll(" ", " " + color))); 
/*    */   }
/*    */   
/*    */   public static void sendLocalMessage(String message) {
/* 20 */     sendLocalMessage(message, ChatColor.YELLOW);
/*    */   }
/*    */   
/*    */   public static void addMessageToHistory(String message) {
/* 24 */     if ((Minecraft.getMinecraft()).player != null)
/* 25 */       (Minecraft.getMinecraft()).ingameGUI.getChatGUI().addToSentMessages(message); 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\chat\ChatUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */