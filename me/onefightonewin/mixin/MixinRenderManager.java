/*    */ package me.onefightonewin.mixin;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.types.Chams;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.OpenGlHelper;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import org.spongepowered.asm.mixin.Mixin;
/*    */ import org.spongepowered.asm.mixin.Overwrite;
/*    */ import org.spongepowered.asm.mixin.Shadow;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Mixin({RenderManager.class})
/*    */ public abstract class MixinRenderManager
/*    */ {
/*    */   @Shadow
/*    */   private double renderPosX;
/*    */   @Shadow
/*    */   private double renderPosY;
/*    */   @Shadow
/*    */   private double renderPosZ;
/*    */   
/*    */   @Shadow
/*    */   abstract boolean func_147939_a(Entity paramEntity, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2, boolean paramBoolean);
/*    */   
/*    */   @Overwrite
/*    */   public boolean func_147936_a(Entity entity, float partialTicks, boolean p_147936_3_) {
/* 33 */     if (entity.ticksExisted == 0) {
/*    */       
/* 35 */       entity.lastTickPosX = entity.posX;
/* 36 */       entity.lastTickPosY = entity.posY;
/* 37 */       entity.lastTickPosZ = entity.posZ;
/*    */     } 
/*    */     
/* 40 */     double d0 = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * partialTicks;
/* 41 */     double d1 = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * partialTicks;
/* 42 */     double d2 = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * partialTicks;
/* 43 */     float f = entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks;
/* 44 */     int i = entity.getBrightnessForRender(partialTicks);
/*    */     
/* 46 */     if (entity.isBurning())
/*    */     {
/* 48 */       i = 15728880;
/*    */     }
/*    */     
/* 51 */     int j = i % 65536;
/* 52 */     int k = i / 65536;
/* 53 */     OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
/*    */     
/* 55 */     Chams chams = (Chams)AntiAFK.getInstance().getFeature(Chams.class);
/* 56 */     Color color = null;
/*    */     
/* 58 */     if (chams.isEnabled() && (
/* 59 */       color = chams.getChamsColor(entity)) != null) {
/* 60 */       GlStateManager.color(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*    */     }
/*    */ 
/*    */     
/* 64 */     if (color == null) {
/* 65 */       GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/*    */     }
/* 67 */     return func_147939_a(entity, d0 - this.renderPosX, d1 - this.renderPosY, d2 - this.renderPosZ, f, partialTicks, p_147936_3_);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinRenderManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */