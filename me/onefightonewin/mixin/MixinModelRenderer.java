/*     */ package me.onefightonewin.mixin;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.model.ModelBase;
/*     */ import net.minecraft.client.model.ModelRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mixin({ModelRenderer.class})
/*     */ public abstract class MixinModelRenderer
/*     */ {
/*     */   @Shadow
/*     */   public boolean isHidden;
/*     */   @Shadow
/*     */   public boolean showModel;
/*     */   @Shadow
/*     */   private boolean compiled;
/*     */   @Shadow
/*     */   public float offsetX;
/*     */   @Shadow
/*     */   public float offsetY;
/*     */   @Shadow
/*     */   public float offsetZ;
/*     */   @Shadow
/*     */   public float rotateAngleX;
/*     */   @Shadow
/*     */   public float rotateAngleY;
/*     */   @Shadow
/*     */   public float rotateAngleZ;
/*     */   @Shadow
/*     */   public float rotationPointX;
/*     */   @Shadow
/*     */   public float rotationPointY;
/*     */   @Shadow
/*     */   public float rotationPointZ;
/*     */   @Shadow
/*     */   public List<ModelRenderer> childModels;
/*     */   @Shadow
/*     */   private int displayList;
/*     */   @Shadow
/*     */   private ModelBase baseModel;
/*     */   
/*     */   @Shadow
/*     */   abstract void compileDisplayList(float paramFloat);
/*     */   
/*     */   @Overwrite
/*     */   public void render(float p_78785_1_) {
/*  65 */     if (!this.isHidden)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  75 */       if (this.showModel) {
/*  76 */         if (!this.compiled) {
/*  77 */           compileDisplayList(p_78785_1_);
/*     */         }
/*     */         
/*  80 */         GlStateManager.translate(this.offsetX, this.offsetY, this.offsetZ);
/*     */         
/*  82 */         if (this.rotateAngleX == 0.0F && this.rotateAngleY == 0.0F && this.rotateAngleZ == 0.0F) {
/*  83 */           if (this.rotationPointX == 0.0F && this.rotationPointY == 0.0F && this.rotationPointZ == 0.0F) {
/*  84 */             GlStateManager.callList(this.displayList);
/*     */             
/*  86 */             if (this.childModels != null) {
/*  87 */               for (int k = 0; k < this.childModels.size(); k++) {
/*  88 */                 ((ModelRenderer)this.childModels.get(k)).render(p_78785_1_);
/*     */               }
/*     */             }
/*     */           } else {
/*  92 */             GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/*  93 */             GlStateManager.callList(this.displayList);
/*     */             
/*  95 */             if (this.childModels != null) {
/*  96 */               for (int j = 0; j < this.childModels.size(); j++) {
/*  97 */                 ((ModelRenderer)this.childModels.get(j)).render(p_78785_1_);
/*     */               }
/*     */             }
/*     */             
/* 101 */             GlStateManager.translate(-this.rotationPointX * p_78785_1_, -this.rotationPointY * p_78785_1_, -this.rotationPointZ * p_78785_1_);
/*     */           } 
/*     */         } else {
/* 104 */           GlStateManager.pushMatrix();
/* 105 */           GlStateManager.translate(this.rotationPointX * p_78785_1_, this.rotationPointY * p_78785_1_, this.rotationPointZ * p_78785_1_);
/*     */           
/* 107 */           if (this.rotateAngleZ != 0.0F) {
/* 108 */             GlStateManager.rotate(this.rotateAngleZ * 57.295776F, 0.0F, 0.0F, 1.0F);
/*     */           }
/*     */           
/* 111 */           if (this.rotateAngleY != 0.0F) {
/* 112 */             GlStateManager.rotate(this.rotateAngleY * 57.295776F, 0.0F, 1.0F, 0.0F);
/*     */           }
/*     */           
/* 115 */           if (this.rotateAngleX != 0.0F) {
/* 116 */             GlStateManager.rotate(this.rotateAngleX * 57.295776F, 1.0F, 0.0F, 0.0F);
/*     */           }
/*     */           
/* 119 */           GlStateManager.callList(this.displayList);
/*     */           
/* 121 */           if (this.childModels != null) {
/* 122 */             for (int i = 0; i < this.childModels.size(); i++) {
/* 123 */               ((ModelRenderer)this.childModels.get(i)).render(p_78785_1_);
/*     */             }
/*     */           }
/*     */           
/* 127 */           GlStateManager.popMatrix();
/*     */         } 
/*     */         
/* 130 */         GlStateManager.translate(-this.offsetX, -this.offsetY, -this.offsetZ);
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinModelRenderer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */