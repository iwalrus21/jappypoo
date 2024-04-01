/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiNewChat;
/*    */ import net.minecraft.util.IChatComponent;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({GuiNewChat.class})
/*    */ public class MixinGuiNewChat
/*    */ {
/*    */   @Inject(at = {@At("HEAD")}, method = {"printChatMessage(Lnet/minecraft/util/IChatComponent;)V"}, cancellable = true)
/*    */   public void printChatMessage(IChatComponent mesasge, CallbackInfo info) {
/* 18 */     if ((Minecraft.getMinecraft()).world == null || (Minecraft.getMinecraft()).player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 22 */     if (!(AntiAFK.getInstance()).legit) {
/* 23 */       boolean cancel = false;
/*    */       
/* 25 */       for (Feature feature : (AntiAFK.getInstance()).features) {
/* 26 */         if (feature.isEnabled() && 
/* 27 */           !feature.onAppendMessage(mesasge)) {
/* 28 */           cancel = true;
/*    */         }
/*    */       } 
/*    */ 
/*    */       
/* 33 */       if (cancel)
/* 34 */         info.cancel(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinGuiNewChat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */