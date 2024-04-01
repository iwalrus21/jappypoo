/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.multiplayer.PlayerControllerMP;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
/*    */ 
/*    */ @Mixin({PlayerControllerMP.class})
/*    */ public class MixinPlayerController
/*    */ {
/*    */   @Inject(at = {@At("RETURN")}, method = {"getBlockReachDistance()F"})
/*    */   public float getBlockReachDistance(CallbackInfoReturnable<Float> info) {
/* 20 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/* 21 */       return ((Float)info.getReturnValue()).floatValue();
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     return ((Float)info.getReturnValue()).floatValue();
/*    */   }
/*    */   
/*    */   @Inject(at = {@At("HEAD")}, method = {"attackEntity(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/entity/Entity;)V"})
/*    */   public void attackEntity(EntityPlayer playerIn, Entity targetEntity, CallbackInfo info) {
/* 39 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/*    */       return;
/*    */     }
/*    */     
/* 43 */     if (playerIn.getUniqueID().equals((Minecraft.getMinecraft()).player.getUniqueID()) && 
/* 44 */       !(AntiAFK.getInstance()).legit)
/* 45 */       for (Feature feature : (AntiAFK.getInstance()).features) {
/* 46 */         if (feature.isEnabled())
/* 47 */           feature.onAttacKEntity(targetEntity); 
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinPlayerController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */