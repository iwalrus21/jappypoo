/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.AirControl;
/*    */ import me.onefightonewin.feature.types.RapidShift;
/*    */ import me.onefightonewin.feature.types.Safewalk;
/*    */ import me.onefightonewin.feature.types.Velocity;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ import org.spongepowered.asm.mixin.injection.At;
/*    */ import org.spongepowered.asm.mixin.injection.Inject;
/*    */ import org.spongepowered.asm.mixin.injection.Redirect;
/*    */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({Entity.class})
/*    */ public abstract class MixinEntity
/*    */ {
/*    */   @Shadow
/*    */   public double motionX;
/*    */   @Shadow
/*    */   public double motionY;
/*    */   @Shadow
/*    */   public double motionZ;
/*    */   
/*    */   @Shadow
/*    */   public abstract UUID getUniqueID();
/*    */   
/*    */   @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"), method = {"moveEntity(DDD)V"})
/*    */   public boolean isSneaking(Entity entity) {
/* 36 */     if (((RapidShift)AntiAFK.getInstance().getFeature(RapidShift.class)).isEnabled()) {
/* 37 */       return ((RapidShift)AntiAFK.getInstance().getFeature(RapidShift.class)).shouldShift();
/*    */     }
/*    */     
/* 40 */     if (((Safewalk)AntiAFK.getInstance().getFeature(Safewalk.class)).isEnabled() && ((Safewalk)AntiAFK.getInstance().getFeature(Safewalk.class)).safeWalkSmart.getRValue() && 
/* 41 */       (Minecraft.getMinecraft()).gameSettings.keyBindForward.isKeyDown() && !entity.isSneaking()) {
/* 42 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 46 */     return ((((Safewalk)AntiAFK.getInstance().getFeature(Safewalk.class)).isEnabled() && ((Safewalk)AntiAFK.getInstance().getFeature(Safewalk.class)).safeWalkSilent.getRValue()) || entity.isSneaking());
/*    */   }
/*    */   
/*    */   @Inject(method = {"setVelocity"}, at = {@At("HEAD")}, cancellable = true)
/*    */   public void setVelocity(double x, double y, double z, CallbackInfo info) {
/* 51 */     if (getUniqueID() == (Minecraft.getMinecraft()).player.getUniqueID()) {
/* 52 */       if (((Velocity)AntiAFK.getInstance().getFeature(Velocity.class)).isEnabled()) {
/* 53 */         Velocity feature = (Velocity)AntiAFK.getInstance().getFeature(Velocity.class);
/*    */         
/* 55 */         if (!feature.fighting && feature.onlyWhenFighting.getRValue()) {
/*    */           return;
/*    */         }
/*    */         
/* 59 */         info.cancel();
/*    */         
/* 61 */         this.motionX = x * feature.velocityHorizontal.getValue() / 100.0D;
/* 62 */         this.motionY = y * feature.velocityVertical.getValue() / 100.0D;
/* 63 */         this.motionZ = z * feature.velocityHorizontal.getValue() / 100.0D;
/*    */         
/*    */         return;
/*    */       } 
/* 67 */       if (((AirControl)AntiAFK.getInstance().getFeature(AirControl.class)).isEnabled()) {
/* 68 */         AirControl feature = (AirControl)AntiAFK.getInstance().getFeature(AirControl.class);
/*    */         
/* 70 */         info.cancel();
/*    */         
/* 72 */         this.motionX = x * feature.aircontrolHorizontal.getValue() / 100.0D;
/* 73 */         this.motionY = y * feature.aircontrolVertical.getValue() / 100.0D;
/* 74 */         this.motionZ = z * feature.aircontrolHorizontal.getValue() / 100.0D;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */