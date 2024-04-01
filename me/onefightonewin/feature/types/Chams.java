/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.AimTarget;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraftforge.client.event.RenderLivingEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.opengl.GL11;
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
/*     */ public class Chams
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel modesLabel;
/*     */   MenuDropdown modes;
/*     */   MenuColorPicker setColor;
/*     */   MenuLabel entityLabel;
/*     */   MenuComboBox entity;
/*     */   
/*     */   public Chams(Minecraft minecraft) {
/*  42 */     super(minecraft, "chams", 0, Category.PAGE_4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  47 */     this.modesLabel = new MenuLabel("Chams color modes", 0, 0);
/*  48 */     this.setColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  49 */     this.modes = new MenuDropdown(Modes.class, 0, 0);
/*  50 */     this.entityLabel = new MenuLabel("Chams entities", 0, 0);
/*  51 */     this.entity = new MenuComboBox(AimTarget.class, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  56 */     components.add(this.modesLabel);
/*  57 */     components.add(this.setColor);
/*  58 */     components.add(this.modes);
/*  59 */     components.add(this.entityLabel);
/*  60 */     components.add(this.entity);
/*     */   }
/*     */   
/*     */   enum Modes {
/*  64 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/*  68 */     for (String condition : this.entity.getValues()) {
/*  69 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  70 */         return true; 
/*     */     } 
/*  72 */     return false;
/*     */   }
/*     */   
/*     */   public Color getChamsColor(Entity entity) {
/*  76 */     Color color = null;
/*     */     
/*  78 */     if (entity instanceof net.minecraft.entity.player.EntityPlayer) {
/*  79 */       if (hasCondition(AimTarget.ENEMIES)) {
/*  80 */         if (entity == this.mc.player) {
/*  81 */           return null;
/*     */         }
/*  83 */         color = this.setColor.getColor();
/*     */       } else {
/*  85 */         return null;
/*     */       } 
/*  87 */     } else if (entity instanceof net.minecraft.entity.passive.EntityBat || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.boss.EntityDragon || entity instanceof net.minecraft.entity.monster.EntityGhast || entity instanceof net.minecraft.entity.monster.EntityIronGolem) {
/*     */ 
/*     */       
/*  90 */       if (hasCondition(AimTarget.MOBS)) {
/*  91 */         color = this.setColor.getColor();
/*     */       } else {
/*  93 */         return null;
/*     */       } 
/*  95 */     } else if (entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof net.minecraft.entity.passive.EntitySquid || entity instanceof net.minecraft.entity.passive.EntityAnimal) {
/*     */       
/*  97 */       if (hasCondition(AimTarget.ANIMALS)) {
/*  98 */         color = this.setColor.getColor();
/*     */       } else {
/* 100 */         return null;
/*     */       } 
/*     */     } 
/*     */     
/* 104 */     if (color == null) {
/* 105 */       return null;
/*     */     }
/* 107 */     if (this.modes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString()))
/* 108 */       return (AntiAFK.getInstance()).rainbowColor; 
/* 109 */     if (this.modes.getValue().equalsIgnoreCase(Modes.NO_COLOR.toString())) {
/* 110 */       return new Color(255, 255, 255, 255);
/*     */     }
/*     */     
/* 113 */     return color;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPreRenderLiving(RenderLivingEvent.Pre<EntityLivingBase> event) {
/* 118 */     if (getChamsColor((Entity)event.entity) != null) {
/* 119 */       GL11.glEnable(32823);
/* 120 */       GL11.glPolygonOffset(1.0F, -1000000.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onPostRenderLiving(RenderLivingEvent.Post<EntityLivingBase> event) {
/* 126 */     if (getChamsColor((Entity)event.entity) != null) {
/* 127 */       GL11.glPolygonOffset(1.0F, 1000000.0F);
/* 128 */       GL11.glDisable(32823);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Chams.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */