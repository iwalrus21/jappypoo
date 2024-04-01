/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ @Mixin({Minecraft.class})
/*    */ public class MixinMinecraft
/*    */ {
/*    */   @Inject(at = {@At("HEAD")}, method = {"clickMouse()V"})
/*    */   public void onLeftClick(CallbackInfo info) {
/* 16 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null || (AntiAFK.getInstance()).legit) {
/*    */       return;
/*    */     }
/*    */     
/* 20 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 21 */       if (feature.isEnabled()) {
/* 22 */         feature.onLeftClick();
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   @Inject(at = {@At("HEAD")}, method = {"rightClickMouse()V"}, cancellable = true)
/*    */   public void onRightClick(CallbackInfo info) {
/* 29 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null || (AntiAFK.getInstance()).legit) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     boolean cancel = false;
/*    */     
/* 35 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 36 */       if (feature.isEnabled() && 
/* 37 */         !feature.onRightClick()) {
/* 38 */         cancel = true;
/*    */       }
/*    */     } 
/*    */ 
/*    */     
/* 43 */     if (cancel)
/* 44 */       info.cancel(); 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinMinecraft.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */