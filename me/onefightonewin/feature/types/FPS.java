/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.DrawImpl;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuDataObject;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FPS
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuLabel modesLabel;
/*     */   MenuDropdown modes;
/*     */   MenuColorPicker setColor;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*  37 */   final int bg = (new Color(0, 0, 0, 155)).getRGB();
/*     */   
/*     */   public FPS(Minecraft minecraft) {
/*  40 */     super(minecraft, "fps", -1, Category.PAGE_6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  45 */     this.modesLabel = new MenuLabel("FPS color modes", 0, 0);
/*  46 */     this.setColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  47 */     this.modes = new MenuDropdown(Modes.class, 0, 0);
/*     */ 
/*     */     
/*  50 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  51 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  56 */     components.add(this.modesLabel);
/*  57 */     components.add(this.setColor);
/*  58 */     components.add(this.modes);
/*     */ 
/*     */     
/*  61 */     components.add(this.x);
/*  62 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  67 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  71 */     Mouse.poll();
/*     */     
/*  73 */     GlStateManager.pushMatrix();
/*  74 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  75 */     float value = 2.0F / sr.getScaleFactor();
/*  76 */     GlStateManager.scale(value, value, value);
/*  77 */     GlStateManager.enableBlend();
/*  78 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/*  80 */     List<String> list = new ArrayList<>();
/*     */     
/*  82 */     list.add("[" + Minecraft.getDebugFPS() + " FPS]");
/*     */     
/*  84 */     List<String> renderStrings = new ArrayList<>();
/*  85 */     int maxWidth = 0;
/*  86 */     int height = 0;
/*     */     
/*  88 */     for (String item : list) {
/*  89 */       int width = getStringWidth(item) + 4;
/*     */       
/*  91 */       if (width > maxWidth) {
/*  92 */         maxWidth = width;
/*     */       }
/*     */       
/*  95 */       renderStrings.add(item);
/*     */       
/*  97 */       height += getStringHeight(item) + 2;
/*     */     } 
/*     */     
/* 100 */     if (this.y.getIntValue() == -1 && maxWidth > 0) {
/* 101 */       this.y.setValue(sr.getScaledHeight() / 4);
/*     */     }
/*     */     
/* 104 */     Collections.sort(renderStrings, new Comparator<String>()
/*     */         {
/*     */           public int compare(String a, String b) {
/* 107 */             return Integer.valueOf(FPS.this.getStringWidth(b)).compareTo(Integer.valueOf(FPS.this.getStringWidth(a)));
/*     */           }
/*     */         });
/*     */     
/* 111 */     height += 2;
/*     */ 
/*     */ 
/*     */     
/* 115 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/* 117 */     if (this.modes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/* 118 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/* 119 */     } else if (this.modes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/* 120 */       color = this.setColor.getColor().getRGB();
/*     */     } 
/*     */     
/* 123 */     int y = this.y.getIntValue() + 2;
/* 124 */     for (String feature : renderStrings) {
/* 125 */       drawText(feature, this.x.getIntValue() - 3, y, color);
/*     */       
/* 127 */       y += getStringHeight(feature) + 2;
/*     */     } 
/*     */     
/* 130 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 131 */       this.mc.currentScreen != null) {
/* 132 */       if (Mouse.isButtonDown(0)) {
/* 133 */         int mouseX = Mouse.getX();
/* 134 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 136 */         mouseX = Math.round(mouseX / 2.0F);
/* 137 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 139 */         if (!this.dragging) {
/* 140 */           if (mouseX >= this.x.getIntValue() && mouseX <= this.x.getIntValue() + maxWidth && 
/* 141 */             mouseY >= this.y.getIntValue() && mouseY <= this.y.getIntValue() + height) {
/* 142 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 146 */           this.x.setValue(mouseX);
/* 147 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 150 */       } else if (this.dragging) {
/* 151 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 157 */     GlStateManager.disableBlend();
/* 158 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   enum Modes {
/* 162 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FPS.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */