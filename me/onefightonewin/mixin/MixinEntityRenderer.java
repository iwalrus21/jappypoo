/*     */ package me.onefightonewin.mixin;
/*     */ 
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.feature.types.FovStatic;
/*     */ import me.onefightonewin.feature.types.NoHurtCam;
/*     */ import me.onefightonewin.feature.types.PerspectiveMod;
/*     */ import me.onefightonewin.feature.types.Reach;
/*     */ import me.onefightonewin.feature.types.Zoom;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.ActiveRenderInfo;
/*     */ import net.minecraft.client.renderer.EntityRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.resources.IResourceManagerReloadListener;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.client.ForgeHooksClient;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.Redirect;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mixin({EntityRenderer.class})
/*     */ public abstract class MixinEntityRenderer
/*     */   implements IResourceManagerReloadListener
/*     */ {
/*     */   @Shadow
/*     */   private boolean cloudFog;
/*     */   @Shadow
/*     */   private Minecraft mc;
/*     */   @Shadow
/*     */   private float thirdPersonDistancePrev;
/*     */   @Shadow
/*     */   private float thirdPersonDistance;
/*     */   @Shadow
/*     */   private boolean debugView;
/*     */   @Shadow
/*     */   private float fovModifierHand;
/*     */   @Shadow
/*     */   private float fovModifierHandPrev;
/*     */   
/*     */   @Inject(at = {@At("TAIL")}, method = {"getMouseOver(F)V"})
/*     */   public void getMouseOverPre(float partialTicks, CallbackInfo info) {
/*  59 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     if (((Reach)AntiAFK.getInstance().getFeature(Reach.class)).isEnabled()) {
/*  64 */       Reach reach = (Reach)AntiAFK.getInstance().getFeature(Reach.class);
/*  65 */       Minecraft mc = Minecraft.getMinecraft();
/*  66 */       MovingObjectPosition obj = (Minecraft.getMinecraft()).objectMouseOver;
/*     */       
/*  68 */       if (obj != null && (
/*  69 */         obj.typeOfHit == MovingObjectPosition.MovingObjectType.MISS || obj.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)) {
/*  70 */         MovingObjectPosition reachObj = reach.getRayTracedContent(reach.getReach());
/*     */         
/*  72 */         if (reachObj != null) {
/*  73 */           mc.objectMouseOver = reachObj;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @Inject(method = {"renderWorldPass"}, at = {@At(value = "FIELD", target = "Lnet/minecraft/client/renderer/EntityRenderer;renderHand:Z", shift = At.Shift.AFTER)})
/*     */   public void renderWorldPass(int pass, float partialTicks, long finishTimeNano, CallbackInfo callbackInfo) {
/*  82 */     if ((Minecraft.getMinecraft()).player == null || (Minecraft.getMinecraft()).world == null) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     if (!(AntiAFK.getInstance()).legit) {
/*  87 */       for (Feature feature : (AntiAFK.getInstance()).features) {
/*  88 */         if (feature.isEnabled()) {
/*  89 */           feature.onRenderWorldPass();
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   @Overwrite
/*     */   public void orientCamera(float partialTicks) {
/*  97 */     Entity entity = this.mc.getRenderViewEntity();
/*     */     
/*  99 */     float yaw = entity.rotationYaw;
/* 100 */     float prevYaw = entity.prevRotationYaw;
/*     */     
/* 102 */     float pitch = entity.rotationPitch;
/* 103 */     float prevPitch = entity.prevRotationPitch;
/*     */     
/* 105 */     PerspectiveMod mod = (PerspectiveMod)AntiAFK.getInstance().getFeature(PerspectiveMod.class);
/*     */     
/* 107 */     if (mod.isEnabled()) {
/* 108 */       yaw = mod.getCameraYaw();
/* 109 */       prevYaw = yaw;
/* 110 */       pitch = mod.getCameraPitch();
/* 111 */       prevPitch = pitch;
/*     */     } 
/*     */     
/* 114 */     float f = entity.getEyeHeight();
/* 115 */     double d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/* 116 */     double d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/* 117 */     double d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/*     */     
/* 119 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).isPlayerSleeping()) {
/*     */       
/* 121 */       f = (float)(f + 1.0D);
/* 122 */       GlStateManager.translate(0.0F, 0.3F, 0.0F);
/*     */       
/* 124 */       if (!this.mc.gameSettings.debugCamEnable)
/*     */       {
/* 126 */         BlockPos blockpos = new BlockPos(entity);
/* 127 */         IBlockState iblockstate = this.mc.world.getBlockState(blockpos);
/* 128 */         ForgeHooksClient.orientBedCamera((IBlockAccess)this.mc.world, blockpos, iblockstate, entity);
/*     */         
/* 130 */         GlStateManager.rotate(prevYaw + (yaw - prevYaw) * partialTicks + 180.0F, 0.0F, -1.0F, 0.0F);
/* 131 */         GlStateManager.rotate(prevPitch + (pitch - prevPitch) * partialTicks, -1.0F, 0.0F, 0.0F);
/*     */       }
/*     */     
/* 134 */     } else if (this.mc.gameSettings.thirdPersonView > 0) {
/*     */       
/* 136 */       double d3 = (this.thirdPersonDistancePrev + (this.thirdPersonDistance - this.thirdPersonDistancePrev) * partialTicks);
/*     */       
/* 138 */       if (this.mc.gameSettings.debugCamEnable)
/*     */       {
/* 140 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/*     */       }
/*     */       else
/*     */       {
/* 144 */         float f1 = yaw;
/* 145 */         float f2 = pitch;
/*     */         
/* 147 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*     */         {
/* 149 */           f2 += 180.0F;
/*     */         }
/*     */         
/* 152 */         double d4 = (-MathHelper.sin(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
/* 153 */         double d5 = (MathHelper.cos(f1 / 180.0F * 3.1415927F) * MathHelper.cos(f2 / 180.0F * 3.1415927F)) * d3;
/* 154 */         double d6 = -MathHelper.sin(f2 / 180.0F * 3.1415927F) * d3;
/*     */         
/* 156 */         for (int i = 0; i < 8; i++) {
/*     */           
/* 158 */           float f3 = ((i & 0x1) * 2 - 1);
/* 159 */           float f4 = ((i >> 1 & 0x1) * 2 - 1);
/* 160 */           float f5 = ((i >> 2 & 0x1) * 2 - 1);
/* 161 */           f3 *= 0.1F;
/* 162 */           f4 *= 0.1F;
/* 163 */           f5 *= 0.1F;
/* 164 */           MovingObjectPosition movingobjectposition = this.mc.world.rayTraceBlocks(new Vec3(d0 + f3, d1 + f4, d2 + f5), new Vec3(d0 - d4 + f3 + f5, d1 - d6 + f4, d2 - d5 + f5));
/*     */           
/* 166 */           if (movingobjectposition != null) {
/*     */             
/* 168 */             double d7 = movingobjectposition.hitVec.distanceTo(new Vec3(d0, d1, d2));
/*     */             
/* 170 */             if (d7 < d3)
/*     */             {
/* 172 */               d3 = d7;
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 177 */         if (this.mc.gameSettings.thirdPersonView == 2)
/*     */         {
/* 179 */           GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
/*     */         }
/*     */         
/* 182 */         GlStateManager.rotate(pitch - f2, 1.0F, 0.0F, 0.0F);
/* 183 */         GlStateManager.rotate(yaw - f1, 0.0F, 1.0F, 0.0F);
/* 184 */         GlStateManager.translate(0.0F, 0.0F, (float)-d3);
/* 185 */         GlStateManager.rotate(f1 - yaw, 0.0F, 1.0F, 0.0F);
/* 186 */         GlStateManager.rotate(f2 - pitch, 1.0F, 0.0F, 0.0F);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 191 */       GlStateManager.translate(0.0F, 0.0F, -0.1F);
/*     */     } 
/*     */     
/* 194 */     if (!this.mc.gameSettings.debugCamEnable) {
/*     */       
/* 196 */       yaw = prevYaw + (yaw - prevYaw) * partialTicks + 180.0F;
/* 197 */       pitch = prevPitch + (pitch - prevPitch) * partialTicks;
/* 198 */       float roll = 0.0F;
/*     */       
/* 200 */       if (entity instanceof EntityAnimal) {
/*     */         
/* 202 */         EntityAnimal entityanimal = (EntityAnimal)entity;
/* 203 */         yaw = entityanimal.prevRotationYawHead + (entityanimal.rotationYawHead - entityanimal.prevRotationYawHead) * partialTicks + 180.0F;
/*     */       } 
/* 205 */       GlStateManager.rotate(roll, 0.0F, 0.0F, 1.0F);
/* 206 */       GlStateManager.rotate(pitch, 1.0F, 0.0F, 0.0F);
/* 207 */       GlStateManager.rotate(yaw, 0.0F, 1.0F, 0.0F);
/*     */     } 
/*     */     
/* 210 */     GlStateManager.translate(0.0F, -f, 0.0F);
/* 211 */     d0 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
/* 212 */     d1 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + f;
/* 213 */     d2 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
/* 214 */     this.cloudFog = this.mc.renderGlobal.hasCloudFog(d0, d1, d2, partialTicks);
/*     */   }
/*     */   
/*     */   @Redirect(method = {"updateCameraAndRender"}, at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;inGameHasFocus:Z"))
/*     */   public boolean inGameHasFcous(Minecraft mc) {
/* 219 */     PerspectiveMod mod = (PerspectiveMod)AntiAFK.getInstance().getFeature(PerspectiveMod.class);
/*     */     
/* 221 */     if (mod.isEnabled()) {
/* 222 */       mod.overrideMouse();
/*     */     }
/*     */     
/* 225 */     return mc.inGameHasFocus;
/*     */   }
/*     */   
/*     */   @Overwrite
/*     */   private float getFOVModifier(float partialTicks, boolean p_78481_2_) {
/* 230 */     FovStatic mod = (FovStatic)AntiAFK.getInstance().getFeature(FovStatic.class);
/* 231 */     Zoom zoom = (Zoom)AntiAFK.getInstance().getFeature(Zoom.class);
/*     */     
/* 233 */     if (zoom.isEnabled())
/* 234 */       return zoom.amount.getValue(); 
/* 235 */     if (mod.isEnabled()) {
/* 236 */       return mod.amount.getValue();
/*     */     }
/*     */     
/* 239 */     if (this.debugView)
/*     */     {
/* 241 */       return 90.0F;
/*     */     }
/*     */ 
/*     */     
/* 245 */     Entity entity = this.mc.getRenderViewEntity();
/* 246 */     float f = 70.0F;
/*     */     
/* 248 */     if (p_78481_2_) {
/*     */       
/* 250 */       f = this.mc.gameSettings.fovSetting;
/* 251 */       f *= this.fovModifierHandPrev + (this.fovModifierHand - this.fovModifierHandPrev) * partialTicks;
/*     */     } 
/*     */     
/* 254 */     if (entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getHealth() <= 0.0F) {
/*     */       
/* 256 */       float f1 = ((EntityLivingBase)entity).deathTime + partialTicks;
/* 257 */       f /= (1.0F - 500.0F / (f1 + 500.0F)) * 2.0F + 1.0F;
/*     */     } 
/*     */     
/* 260 */     Block block = ActiveRenderInfo.func_180786_a((World)this.mc.world, entity, partialTicks);
/*     */     
/* 262 */     if (block.getMaterial() == Material.WATER)
/*     */     {
/* 264 */       f = f * 60.0F / 70.0F;
/*     */     }
/*     */     
/* 267 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   @Overwrite
/*     */   private void hurtCameraEffect(float partialTicks) {
/* 273 */     if (this.mc.getRenderViewEntity() instanceof EntityLivingBase) {
/*     */       
/* 275 */       if (this.mc.getRenderViewEntity().getUniqueID().equals(this.mc.player.getUniqueID())) {
/* 276 */         NoHurtCam nhc = (NoHurtCam)AntiAFK.getInstance().getFeature(NoHurtCam.class);
/*     */         
/* 278 */         if (nhc.isEnabled()) {
/*     */           return;
/*     */         }
/*     */       } 
/* 282 */       EntityLivingBase entitylivingbase = (EntityLivingBase)this.mc.getRenderViewEntity();
/* 283 */       float f = entitylivingbase.hurtTime - partialTicks;
/*     */       
/* 285 */       if (entitylivingbase.getHealth() <= 0.0F) {
/*     */         
/* 287 */         float f1 = entitylivingbase.deathTime + partialTicks;
/* 288 */         GlStateManager.rotate(40.0F - 8000.0F / (f1 + 200.0F), 0.0F, 0.0F, 1.0F);
/*     */       } 
/*     */       
/* 291 */       if (f < 0.0F) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 296 */       f /= entitylivingbase.maxHurtTime;
/* 297 */       f = MathHelper.sin(f * f * f * f * 3.1415927F);
/* 298 */       float f2 = entitylivingbase.attackedAtYaw;
/* 299 */       GlStateManager.rotate(-f2, 0.0F, 1.0F, 0.0F);
/* 300 */       GlStateManager.rotate(-f * 14.0F, 0.0F, 0.0F, 1.0F);
/* 301 */       GlStateManager.rotate(f2, 0.0F, 1.0F, 0.0F);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinEntityRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */