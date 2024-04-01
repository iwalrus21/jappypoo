/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.DrawImpl;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ 
/*     */ public class Crosshair
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuLabel color;
/*     */   MenuColorPicker colorDef;
/*     */   MenuDropdown colorModes;
/*     */   MenuLabel lengthLabel;
/*     */   MenuSlider length;
/*     */   MenuLabel widthLabel;
/*     */   MenuSlider width;
/*     */   MenuLabel gapLabel;
/*     */   MenuSlider gap;
/*     */   
/*     */   public Crosshair(Minecraft minecraft) {
/*  37 */     super(minecraft, "crosshair", -1, Category.PAGE_6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  42 */     this.lengthLabel = new MenuLabel("Crosshair length", 0, 0);
/*  43 */     this.length = new MenuSlider(3, 1, 20, 0, 0, 100, 6);
/*  44 */     this.widthLabel = new MenuLabel("Crosshair width", 0, 0);
/*  45 */     this.width = new MenuSlider(2, 1, 20, 0, 0, 100, 6);
/*  46 */     this.gapLabel = new MenuLabel("Crosshair gap", 0, 0);
/*  47 */     this.gap = new MenuSlider(3, 1, 20, 0, 0, 100, 6);
/*  48 */     this.color = new MenuLabel("Crosshair color modes", 0, 0);
/*  49 */     this.colorDef = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  50 */     this.colorModes = new MenuDropdown(Modes.class, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  55 */     components.add(this.lengthLabel);
/*  56 */     components.add(this.length);
/*  57 */     components.add(this.widthLabel);
/*  58 */     components.add(this.width);
/*  59 */     components.add(this.gapLabel);
/*  60 */     components.add(this.gap);
/*  61 */     components.add(this.color);
/*  62 */     components.add(this.colorDef);
/*  63 */     components.add(this.colorModes);
/*     */   }
/*     */   
/*     */   enum Modes {
/*  67 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  72 */     if (this.mc.world == null || this.mc.player == null || !shouldRenderCrosshair()) {
/*     */       return;
/*     */     }
/*     */     
/*  76 */     GlStateManager.pushMatrix();
/*     */     
/*  78 */     ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
/*  79 */     int scaleFactor = sr.getScaleFactor();
/*  80 */     float value = 2.0F / scaleFactor;
/*  81 */     GlStateManager.scale(value, value, value);
/*     */     
/*  83 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/*  85 */     if (this.colorModes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/*  86 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/*  87 */     } else if (this.colorModes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/*  88 */       color = this.colorDef.getColor().getRGB();
/*     */     } 
/*     */     
/*  91 */     int centerX = sr.getScaledWidth() / 2;
/*  92 */     int centerY = sr.getScaledHeight() / 2;
/*     */     
/*  94 */     int gap = this.gap.getIntValue() - 1;
/*  95 */     int width = this.width.getIntValue();
/*  96 */     int length = this.length.getIntValue();
/*     */     
/*  98 */     drawRect(centerX - length - gap + 1, centerY - width / 2, length, width, color);
/*  99 */     drawRect(centerX + gap, centerY - width / 2, length, width, color);
/*     */     
/* 101 */     drawRect(centerX - width / 2, centerY - gap - length + 1, width, length, color);
/* 102 */     drawRect(centerX - width / 2, centerY + gap, width, length, color);
/*     */     
/* 104 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldRenderCrosshair() {
/* 109 */     if (this.mc.gameSettings.showDebugInfo && !this.mc.player.hasReducedDebug() && !this.mc.gameSettings.reducedDebugInfo)
/*     */     {
/* 111 */       return false;
/*     */     }
/* 113 */     if (this.mc.playerController.isSpectator()) {
/*     */       
/* 115 */       if (this.mc.pointedEntity != null)
/*     */       {
/* 117 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 121 */       if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*     */         
/* 123 */         BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();
/*     */         
/* 125 */         if (this.mc.world.getTileEntity(blockpos) instanceof net.minecraft.inventory.IInventory)
/*     */         {
/* 127 */           return true;
/*     */         }
/*     */       } 
/*     */       
/* 131 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 136 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Crosshair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */