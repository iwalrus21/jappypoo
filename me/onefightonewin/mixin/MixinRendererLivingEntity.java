/*     */ package me.onefightonewin.mixin;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.nio.FloatBuffer;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.types.HitColor;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.OpenGlHelper;
/*     */ import net.minecraft.client.renderer.entity.Render;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.entity.RendererLivingEntity;
/*     */ import net.minecraft.client.renderer.texture.DynamicTexture;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ 
/*     */ 
/*     */ @Mixin({RendererLivingEntity.class})
/*     */ public abstract class MixinRendererLivingEntity<T extends EntityLivingBase>
/*     */   extends Render<T>
/*     */ {
/*     */   @Shadow
/*     */   protected FloatBuffer brightnessBuffer;
/*     */   @Shadow
/*     */   private static DynamicTexture TEXTURE_BRIGHTNESS;
/*     */   
/*     */   @Shadow
/*     */   abstract int getColorMultiplier(T paramT, float paramFloat1, float paramFloat2);
/*     */   
/*     */   protected MixinRendererLivingEntity(RenderManager renderManager) {
/*  33 */     super(renderManager);
/*     */   }
/*     */ 
/*     */   
/*     */   @Overwrite
/*     */   protected boolean setBrightness(T entitylivingbaseIn, float partialTicks, boolean combineTextures) {
/*  39 */     float f = entitylivingbaseIn.getBrightness(partialTicks);
/*  40 */     int i = getColorMultiplier(entitylivingbaseIn, f, partialTicks);
/*  41 */     boolean flag = ((i >> 24 & 0xFF) > 0);
/*  42 */     boolean flag1 = (((EntityLivingBase)entitylivingbaseIn).hurtTime > 0 || ((EntityLivingBase)entitylivingbaseIn).deathTime > 0);
/*     */     
/*  44 */     if (entitylivingbaseIn != null) {
/*  45 */       HitColor hit = (HitColor)AntiAFK.getInstance().getFeature(HitColor.class);
/*     */       
/*  47 */       if (hit.shouldRenderHit(entitylivingbaseIn.getUniqueID())) {
/*  48 */         flag = true;
/*  49 */         flag1 = false;
/*     */         
/*  51 */         Color color = new Color(hit.hitColor.getColor().getRGB(), true);
/*  52 */         i = (new Color(color.getRed(), color.getGreen(), color.getBlue(), 255 - color.getAlpha())).getRGB();
/*     */       } 
/*     */     } 
/*     */     
/*  56 */     if (!flag && !flag1)
/*     */     {
/*  58 */       return false;
/*     */     }
/*  60 */     if (!flag && !combineTextures)
/*     */     {
/*  62 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  66 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/*  67 */     GlStateManager.enableTexture2D();
/*  68 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/*  69 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/*  70 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.defaultTexUnit);
/*  71 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PRIMARY_COLOR);
/*  72 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/*  73 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/*  74 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/*  75 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.defaultTexUnit);
/*  76 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/*  77 */     GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
/*  78 */     GlStateManager.enableTexture2D();
/*  79 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/*  80 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, OpenGlHelper.GL_INTERPOLATE);
/*  81 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_CONSTANT);
/*  82 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.GL_PREVIOUS);
/*  83 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE2_RGB, OpenGlHelper.GL_CONSTANT);
/*  84 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/*  85 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/*  86 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND2_RGB, 770);
/*  87 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/*  88 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/*  89 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/*  90 */     this.brightnessBuffer.position(0);
/*     */     
/*  92 */     if (flag1) {
/*     */       
/*  94 */       this.brightnessBuffer.put(1.0F);
/*  95 */       this.brightnessBuffer.put(0.0F);
/*  96 */       this.brightnessBuffer.put(0.0F);
/*  97 */       this.brightnessBuffer.put(0.3F);
/*     */     }
/*     */     else {
/*     */       
/* 101 */       float f1 = (i >> 24 & 0xFF) / 255.0F;
/* 102 */       float f2 = (i >> 16 & 0xFF) / 255.0F;
/* 103 */       float f3 = (i >> 8 & 0xFF) / 255.0F;
/* 104 */       float f4 = (i & 0xFF) / 255.0F;
/* 105 */       this.brightnessBuffer.put(f2);
/* 106 */       this.brightnessBuffer.put(f3);
/* 107 */       this.brightnessBuffer.put(f4);
/* 108 */       this.brightnessBuffer.put(1.0F - f1);
/*     */     } 
/*     */     
/* 111 */     this.brightnessBuffer.flip();
/* 112 */     GL11.glTexEnv(8960, 8705, this.brightnessBuffer);
/* 113 */     GlStateManager.setActiveTexture(OpenGlHelper.GL_TEXTURE2);
/* 114 */     GlStateManager.enableTexture2D();
/* 115 */     GlStateManager.bindTexture(TEXTURE_BRIGHTNESS.getGlTextureId());
/* 116 */     GL11.glTexEnvi(8960, 8704, OpenGlHelper.GL_COMBINE);
/* 117 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_RGB, 8448);
/* 118 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_RGB, OpenGlHelper.GL_PREVIOUS);
/* 119 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE1_RGB, OpenGlHelper.lightmapTexUnit);
/* 120 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_RGB, 768);
/* 121 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND1_RGB, 768);
/* 122 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_COMBINE_ALPHA, 7681);
/* 123 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_SOURCE0_ALPHA, OpenGlHelper.GL_PREVIOUS);
/* 124 */     GL11.glTexEnvi(8960, OpenGlHelper.GL_OPERAND0_ALPHA, 770);
/* 125 */     GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
/* 126 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinRendererLivingEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */