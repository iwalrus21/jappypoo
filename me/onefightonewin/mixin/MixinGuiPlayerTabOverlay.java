/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.feature.types.CustomTab;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiPlayerTabOverlay;
/*    */ import net.minecraft.scoreboard.ScoreObjective;
/*    */ import net.minecraft.scoreboard.Scoreboard;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ @Mixin({GuiPlayerTabOverlay.class})
/*    */ public class MixinGuiPlayerTabOverlay
/*    */ {
/*    */   @Inject(at = {@At("HEAD")}, method = {"renderPlayerlist(ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreObjective;)V"}, cancellable = true)
/*    */   public void renderPlayerlist(int width, Scoreboard scoreboardIn, ScoreObjective scoreObjectiveIn, CallbackInfo info) {
/* 21 */     if ((Minecraft.getMinecraft()).world == null || (Minecraft.getMinecraft()).player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     if (!(AntiAFK.getInstance()).legit) {
/* 26 */       if (((CustomTab)AntiAFK.getInstance().getFeature(CustomTab.class)).isEnabled()) {
/* 27 */         info.cancel();
/*    */       }
/*    */       
/* 30 */       for (Feature feature : (AntiAFK.getInstance()).features) {
/* 31 */         if (feature.isEnabled())
/* 32 */           feature.onRenderPlayerlist(scoreboardIn, scoreObjectiveIn); 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinGuiPlayerTabOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */