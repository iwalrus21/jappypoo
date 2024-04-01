/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Hitbox
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel multiplierLabel;
/*     */   MenuSlider multiplier;
/*     */   MenuLabel optionsLabel;
/*     */   MenuComboBox options;
/*  25 */   float orginalWidth = 0.6F;
/*  26 */   float orginalHeight = 1.8F;
/*     */   
/*     */   public Hitbox(Minecraft minecraft) {
/*  29 */     super(minecraft, "hitbox", 0, Category.PAGE_2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  34 */     this.multiplierLabel = new MenuLabel("Hitbox multiplier", 0, 0);
/*  35 */     this.multiplier = new MenuSlider(0.0F, 0.0F, 1.0F, 1, 0, 0, 100, 6);
/*  36 */     this.optionsLabel = new MenuLabel("Hitbox options", 0, 0);
/*  37 */     this.options = new MenuComboBox(Settings.class, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  42 */     components.add(this.optionsLabel);
/*  43 */     components.add(this.options);
/*  44 */     components.add(this.multiplierLabel);
/*  45 */     components.add(this.multiplier);
/*     */   }
/*     */   
/*     */   enum Settings {
/*  49 */     ONLY_ON_GROUND, DISABLE_IN_WATER, ONLY_WHEN_SPRINTING;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Settings cond) {
/*  53 */     for (String condition : this.options.getValues()) {
/*  54 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  55 */         return true; 
/*     */     } 
/*  57 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  64 */     for (EntityPlayer entity : this.mc.world.playerEntities) {
/*  65 */       if (entity == this.mc.player) {
/*     */         continue;
/*     */       }
/*     */       
/*  69 */       entity.setEntityBoundingBox(new AxisAlignedBB((entity.getEntityBoundingBox()).minX, (entity.getEntityBoundingBox()).minY, (entity.getEntityBoundingBox()).minZ, (entity.getEntityBoundingBox()).minX + this.orginalWidth, (entity.getEntityBoundingBox()).minY + this.orginalHeight, (entity.getEntityBoundingBox()).minZ + this.orginalWidth));
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  75 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     if (hasCondition(Settings.ONLY_ON_GROUND) && 
/*  80 */       !this.mc.player.onGround) {
/*  81 */       onDisable();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  86 */     if (hasCondition(Settings.DISABLE_IN_WATER) && 
/*  87 */       this.mc.player.isInWater()) {
/*  88 */       onDisable();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/*  93 */     if (hasCondition(Settings.ONLY_WHEN_SPRINTING) && 
/*  94 */       !this.mc.player.isSprinting()) {
/*  95 */       onDisable();
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 100 */     float multiplier = this.multiplier.getValue() + 1.0F;
/* 101 */     float width = this.orginalWidth * multiplier / 2.0F;
/*     */     
/* 103 */     for (EntityPlayer entity : this.mc.world.playerEntities) {
/* 104 */       if (entity == this.mc.player) {
/*     */         continue;
/*     */       }
/*     */       
/* 108 */       AxisAlignedBB box = entity.getEntityBoundingBox();
/* 109 */       double w = box.maxX - box.minX;
/*     */       
/* 111 */       if (Math.max(w, width) - Math.min(w, width) < 0.1D)
/* 112 */         entity.setEntityBoundingBox(new AxisAlignedBB((entity.getEntityBoundingBox()).minX - width, (entity.getEntityBoundingBox()).minY, (entity.getEntityBoundingBox()).minZ - width, (entity.getEntityBoundingBox()).minX + width * 2.0D, (entity.getEntityBoundingBox()).minY + this.orginalHeight, (entity.getEntityBoundingBox()).minZ + width * 2.0D)); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Hitbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */