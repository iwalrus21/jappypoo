/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Antiafk
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel antiAfkIntervalLabel;
/*     */   public MenuSlider antiAfkInterval;
/*     */   long lastMove;
/*     */   Random random;
/*  25 */   int lastType = -1;
/*     */   
/*     */   public Antiafk(Minecraft minecraft) {
/*  28 */     super(minecraft, "antiafk", 0, Category.PAGE_3);
/*  29 */     this.random = new Random();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  34 */     this.antiAfkIntervalLabel = new MenuLabel("Anti-AFK interval", 0, 0);
/*  35 */     this.antiAfkInterval = new MenuSlider(0, 1, 1000, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  40 */     components.add(this.antiAfkIntervalLabel);
/*  41 */     components.add(this.antiAfkInterval);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  46 */     this.lastMove = -1L;
/*  47 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), false);
/*  48 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), false);
/*  49 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindForward.getKeyCode(), false);
/*  50 */     KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindBack.getKeyCode(), false);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  55 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     long time = System.currentTimeMillis();
/*     */     
/*  61 */     if (time - this.lastMove < this.antiAfkInterval.getIntValue()) {
/*  62 */       if (time - this.lastMove > 250L) {
/*  63 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), false);
/*  64 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), false);
/*  65 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindForward.getKeyCode(), false);
/*  66 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindBack.getKeyCode(), false);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     boolean reset = false;
/*  72 */     if (this.lastType == -1) {
/*  73 */       this.lastType = this.random.nextInt(3);
/*     */     } else {
/*  75 */       reset = true;
/*     */     } 
/*     */     
/*  78 */     if (reset) {
/*  79 */       if (this.lastType == 1) {
/*  80 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), true);
/*  81 */       } else if (this.lastType == 0) {
/*  82 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), true);
/*  83 */       } else if (this.lastType == 3) {
/*  84 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindForward.getKeyCode(), true);
/*  85 */       } else if (this.lastType == 2) {
/*  86 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindBack.getKeyCode(), true);
/*     */       } 
/*     */       
/*  89 */       this.lastType = -1;
/*     */     }
/*  91 */     else if (this.lastType == 0) {
/*  92 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindLeft.getKeyCode(), true);
/*  93 */     } else if (this.lastType == 1) {
/*  94 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindRight.getKeyCode(), true);
/*  95 */     } else if (this.lastType == 2) {
/*  96 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindForward.getKeyCode(), true);
/*  97 */     } else if (this.lastType == 3) {
/*  98 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindBack.getKeyCode(), true);
/*     */     } 
/*     */ 
/*     */     
/* 102 */     this.lastMove = time;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Antiafk.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */