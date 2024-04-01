/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ public class Velocity
/*     */   extends Feature
/*     */ {
/*  19 */   final int hitCacheTime = 5000;
/*     */   
/*     */   MenuCheckbox velocityMode2;
/*     */   
/*     */   MenuLabel velocityVerticalLabel;
/*     */   
/*     */   public MenuSlider velocityVertical;
/*     */   MenuLabel velocityHorizontalLabel;
/*     */   public MenuSlider velocityHorizontal;
/*     */   public MenuCheckbox onlyWhenFighting;
/*     */   public boolean fighting;
/*     */   long lastFight;
/*     */   
/*     */   public Velocity(Minecraft minecraft) {
/*  33 */     super(minecraft, "velocity", 0, Category.PAGE_1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  38 */     this.velocityMode2 = new MenuCheckbox("Velocity - Mode 2", 0, 0, 12, 12);
/*     */     
/*  40 */     this.velocityVerticalLabel = new MenuLabel("Velocity vertical", 0, 0);
/*  41 */     this.velocityVertical = new MenuSlider(100, 0, 100, 0, 0, 100, 6);
/*  42 */     this.velocityHorizontalLabel = new MenuLabel("Velocity horizontal", 0, 0);
/*  43 */     this.velocityHorizontal = new MenuSlider(100, 0, 100, 0, 0, 100, 6);
/*     */     
/*  45 */     this.onlyWhenFighting = new MenuCheckbox("Only when fighting", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onAttackEntity(AttackEntityEvent e) {
/*  51 */     if (e.entityPlayer != null && e.target != null) {
/*  52 */       if (!e.entity.world.isRemote) {
/*     */         return;
/*     */       }
/*     */       
/*  56 */       if (e.entityPlayer.getDistance(e.target) > 6.0F) {
/*     */         return;
/*     */       }
/*     */       
/*  60 */       if (e.entityPlayer.getUniqueID().equals(this.mc.player.getUniqueID())) {
/*  61 */         this.fighting = true;
/*  62 */         this.lastFight = System.currentTimeMillis() + 5000L;
/*  63 */       } else if (e.target.getUniqueID().equals(this.mc.player.getUniqueID())) {
/*  64 */         this.fighting = true;
/*  65 */         this.lastFight = System.currentTimeMillis() + 5000L;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  72 */     components.add(this.velocityMode2);
/*  73 */     components.add(this.velocityVerticalLabel);
/*  74 */     components.add(this.velocityVertical);
/*  75 */     components.add(this.velocityHorizontalLabel);
/*  76 */     components.add(this.velocityHorizontal);
/*  77 */     components.add(this.onlyWhenFighting);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  82 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  86 */     if (this.fighting && 
/*  87 */       System.currentTimeMillis() > this.lastFight) {
/*  88 */       this.fighting = false;
/*     */     }
/*     */ 
/*     */     
/*  92 */     if (!this.fighting && this.onlyWhenFighting.getRValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     if (this.velocityMode2.getRValue() && 
/*  97 */       this.mc.player.hurtTime > this.mc.player.maxHurtTime / 2) {
/*  98 */       this.mc.player.motionX = 0.0D;
/*  99 */       this.mc.player.motionY = 0.0D;
/* 100 */       this.mc.player.motionZ = 0.0D;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Velocity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */