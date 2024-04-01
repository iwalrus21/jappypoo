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
/*     */ public class ArrayListFeature
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuLabel color;
/*     */   MenuColorPicker colorDef;
/*     */   MenuDropdown colorModes;
/*     */   MenuLabel backgroundLabel;
/*     */   MenuColorPicker backgroundColor;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*  38 */   final int bg = (new Color(0, 0, 0, 155)).getRGB();
/*     */   
/*     */   List<String> skip;
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  44 */     this.color = new MenuLabel("ArrayList color modes", 0, 0);
/*  45 */     this.colorDef = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  46 */     this.colorModes = new MenuDropdown(Modes.class, 0, 0);
/*  47 */     this.backgroundLabel = new MenuLabel("ArrayList background color", 0, 0);
/*  48 */     this.backgroundColor = new MenuColorPicker(0, 0, 20, 10, this.bg);
/*  49 */     this.x = new MenuDataObject(getName() + "-x", "5");
/*  50 */     this.y = new MenuDataObject(getName() + "-y", "5");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  55 */     components.add(this.color);
/*  56 */     components.add(this.colorDef);
/*  57 */     components.add(this.colorModes);
/*  58 */     components.add(this.backgroundLabel);
/*  59 */     components.add(this.backgroundColor);
/*  60 */     components.add(this.x);
/*  61 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   enum Modes {
/*  65 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */   
/*     */   public ArrayListFeature(Minecraft minecraft) {
/*  69 */     super(minecraft, "arraylist", -1, Category.PAGE_MISC);
/*     */     
/*  71 */     this.skip = new ArrayList<>();
/*  72 */     this.skip.add("lockhud");
/*  73 */     this.skip.add("arraylist");
/*  74 */     this.skip.add("customtab");
/*  75 */     this.skip.add("middleclickfriends");
/*  76 */     this.skip.add("textmode");
/*  77 */     this.skip.add("keystrokes");
/*  78 */     this.skip.add("togglesprint");
/*  79 */     this.skip.add("fps");
/*  80 */     this.skip.add("armorstatus");
/*  81 */     this.skip.add("statuseffect");
/*  82 */     this.skip.add("perspective");
/*  83 */     this.skip.add("coordinates");
/*  84 */     this.skip.add("hitcolor");
/*  85 */     this.skip.add("crosshair");
/*  86 */     this.skip.add("mousedelayfix");
/*  87 */     this.skip.add("blockoverlay");
/*  88 */     this.skip.add("fovstatic");
/*  89 */     this.skip.add("restocktimer");
/*  90 */     this.skip.add("flashcounter");
/*  91 */     this.skip.add("zoom");
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  96 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     GlStateManager.pushMatrix();
/* 101 */     float value = 2.0F / (new ScaledResolution(this.mc)).getScaleFactor();
/* 102 */     GlStateManager.scale(value, value, value);
/* 103 */     GlStateManager.enableBlend();
/* 104 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/* 106 */     List<Feature> features = (AntiAFK.getInstance()).features;
/*     */     
/* 108 */     List<String> renderStrings = new ArrayList<>();
/* 109 */     TextMode textMode = (TextMode)AntiAFK.getInstance().getFeature(TextMode.class);
/* 110 */     int maxWidth = 0;
/* 111 */     int height = 0;
/*     */     
/* 113 */     for (Feature feature : features) {
/* 114 */       if (feature.isEnabled()) {
/* 115 */         String string = textMode.getString(feature.getName());
/*     */         
/* 117 */         if (string.length() == 0) {
/*     */           continue;
/*     */         }
/* 120 */         if (this.skip.contains(feature.getName())) {
/*     */           continue;
/*     */         }
/* 123 */         int width = getStringWidth(string) + 4;
/*     */         
/* 125 */         if (width > maxWidth) {
/* 126 */           maxWidth = width;
/*     */         }
/*     */         
/* 129 */         renderStrings.add(string);
/*     */         
/* 131 */         height += getStringHeight(string) + 2;
/*     */       } 
/*     */     } 
/*     */     
/* 135 */     Collections.sort(renderStrings, new Comparator<String>()
/*     */         {
/*     */           public int compare(String a, String b) {
/* 138 */             return Integer.valueOf(ArrayListFeature.this.getStringWidth(b)).compareTo(Integer.valueOf(ArrayListFeature.this.getStringWidth(a)));
/*     */           }
/*     */         });
/*     */     
/* 142 */     height += 2;
/*     */     
/* 144 */     int x = this.x.getIntValue();
/*     */     
/* 146 */     drawRect(x, this.y.getIntValue(), maxWidth, height, this.backgroundColor.getColor().getRGB());
/*     */     
/* 148 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/* 150 */     if (this.colorModes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/* 151 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/* 152 */     } else if (this.colorModes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/* 153 */       color = this.colorDef.getColor().getRGB();
/*     */     } 
/*     */     
/* 156 */     int y = this.y.getIntValue() + 2;
/* 157 */     for (String feature : renderStrings) {
/* 158 */       drawText(feature, x + 2, y, color);
/*     */       
/* 160 */       y += getStringHeight(feature) + 2;
/*     */     } 
/*     */     
/* 163 */     Mouse.poll();
/*     */     
/* 165 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 166 */       this.mc.currentScreen != null) {
/* 167 */       if (Mouse.isButtonDown(0)) {
/* 168 */         int mouseX = Mouse.getX();
/* 169 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 171 */         mouseX = Math.round(mouseX / 2.0F);
/* 172 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 174 */         if (!this.dragging) {
/* 175 */           if (mouseX >= this.x.getIntValue() && mouseX <= x + maxWidth && 
/* 176 */             mouseY >= this.y.getIntValue() && mouseY <= y) {
/* 177 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 181 */           this.x.setValue(mouseX);
/* 182 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 185 */       } else if (this.dragging) {
/* 186 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 192 */     GlStateManager.disableBlend();
/* 193 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\ArrayListFeature.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */