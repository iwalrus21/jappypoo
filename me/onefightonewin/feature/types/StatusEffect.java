/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
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
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class StatusEffect
/*     */   extends Feature
/*     */   implements DrawImpl {
/*  32 */   protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
/*     */   
/*     */   MenuLabel modesLabel;
/*     */   
/*     */   MenuDropdown modes;
/*     */   
/*     */   MenuColorPicker setColor;
/*     */   
/*     */   MenuLabel backgroundLabel;
/*     */   MenuColorPicker backgroundColor;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*  45 */   final int bg = (new Color(0, 0, 0, 155)).getRGB();
/*     */   
/*     */   public StatusEffect(Minecraft minecraft) {
/*  48 */     super(minecraft, "statuseffect", -1, Category.PAGE_6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  53 */     this.modesLabel = new MenuLabel("StatusEffect color modes", 0, 0);
/*  54 */     this.setColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  55 */     this.modes = new MenuDropdown(Modes.class, 0, 0);
/*  56 */     this.backgroundLabel = new MenuLabel("StatusEffect background color", 0, 0);
/*  57 */     this.backgroundColor = new MenuColorPicker(0, 0, 20, 10, this.bg);
/*  58 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  59 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  64 */     components.add(this.modesLabel);
/*  65 */     components.add(this.setColor);
/*  66 */     components.add(this.modes);
/*  67 */     components.add(this.backgroundLabel);
/*  68 */     components.add(this.backgroundColor);
/*  69 */     components.add(this.x);
/*  70 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  75 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     Mouse.poll();
/*     */     
/*  81 */     GlStateManager.pushMatrix();
/*  82 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  83 */     float value = 2.0F / sr.getScaleFactor();
/*  84 */     GlStateManager.scale(value, value, value);
/*  85 */     GlStateManager.enableBlend();
/*  86 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/*  88 */     List<String> list = new ArrayList<>();
/*  89 */     int dimension = 18;
/*     */     
/*  91 */     for (PotionEffect effect : this.mc.player.getActivePotionEffects()) {
/*  92 */       String first = I18n.format(effect.getEffectName(), new Object[0]);
/*  93 */       String second = Potion.func_76389_a(effect);
/*     */       
/*  95 */       if (effect.getAmplifier() == 1) {
/*  96 */         first = first + " " + I18n.format("enchantment.level.2", new Object[0]);
/*  97 */       } else if (effect.getAmplifier() == 2) {
/*  98 */         first = first + " " + I18n.format("enchantment.level.3", new Object[0]);
/*  99 */       } else if (effect.getAmplifier() == 3) {
/* 100 */         first = first + " " + I18n.format("enchantment.level.4", new Object[0]);
/*     */       } 
/*     */       
/* 103 */       list.add(first);
/* 104 */       list.add(second);
/*     */     } 
/* 106 */     List<String> renderStrings = new ArrayList<>();
/* 107 */     int maxWidth = 0;
/* 108 */     int height = 0;
/*     */     
/* 110 */     for (String item : list) {
/* 111 */       int width = getStringWidth(item) + 4;
/*     */       
/* 113 */       if (width > maxWidth) {
/* 114 */         maxWidth = width;
/*     */       }
/*     */       
/* 117 */       renderStrings.add(item);
/*     */       
/* 119 */       height += getStringHeight(item) + 2;
/*     */     } 
/*     */     
/* 122 */     if (maxWidth > 0) {
/* 123 */       maxWidth += dimension;
/*     */     }
/*     */     
/* 126 */     if (this.y.getIntValue() == -1 && maxWidth > 0) {
/* 127 */       this.y.setValue(sr.getScaledHeight() / 2);
/*     */     }
/*     */     
/* 130 */     height += 2;
/*     */     
/* 132 */     drawRect(this.x.getIntValue() - 5, this.y.getIntValue(), maxWidth, height, this.backgroundColor.getColor().getRGB());
/*     */     
/* 134 */     int tY = this.y.getIntValue() + 2;
/* 135 */     for (PotionEffect effect : this.mc.player.getActivePotionEffects()) {
/* 136 */       Potion potion = Potion.field_76425_a[effect.func_76456_a()];
/* 137 */       int i1 = potion.getStatusIconIndex();
/*     */       
/* 139 */       if (potion.hasStatusIcon()) {
/* 140 */         GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 141 */         this.mc.getTextureManager().bindTexture(inventoryBackground);
/* 142 */         drawTexturedModalRect(this.x.getIntValue() - 4, tY + 2, 0 + i1 % 8 * dimension, 198 + i1 / 8 * dimension, dimension, dimension);
/*     */       } 
/*     */       
/* 145 */       String first = I18n.format(effect.getEffectName(), new Object[0]);
/* 146 */       String second = Potion.func_76389_a(effect);
/*     */       
/* 148 */       if (effect.getAmplifier() == 1) {
/* 149 */         first = first + " " + I18n.format("enchantment.level.2", new Object[0]);
/* 150 */       } else if (effect.getAmplifier() == 2) {
/* 151 */         first = first + " " + I18n.format("enchantment.level.3", new Object[0]);
/* 152 */       } else if (effect.getAmplifier() == 3) {
/* 153 */         first = first + " " + I18n.format("enchantment.level.4", new Object[0]);
/*     */       } 
/*     */       
/* 156 */       tY += getStringHeight(first) + getStringHeight(second) + 4;
/*     */     } 
/*     */ 
/*     */     
/* 160 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/* 162 */     if (this.modes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/* 163 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/* 164 */     } else if (this.modes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/* 165 */       color = this.setColor.getColor().getRGB();
/*     */     } 
/*     */     
/* 168 */     int y = this.y.getIntValue() + 2;
/* 169 */     for (String feature : renderStrings) {
/* 170 */       drawText(feature, this.x.getIntValue() - 3 + dimension, y, color);
/*     */       
/* 172 */       y += getStringHeight(feature) + 2;
/*     */     } 
/*     */     
/* 175 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 176 */       this.mc.currentScreen != null) {
/* 177 */       if (Mouse.isButtonDown(0)) {
/* 178 */         int mouseX = Mouse.getX();
/* 179 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 181 */         mouseX = Math.round(mouseX / 2.0F);
/* 182 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 184 */         if (!this.dragging) {
/* 185 */           if (mouseX >= this.x.getIntValue() && mouseX <= this.x.getIntValue() + maxWidth && 
/* 186 */             mouseY >= this.y.getIntValue() && mouseY <= this.y.getIntValue() + height) {
/* 187 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 191 */           this.x.setValue(mouseX);
/* 192 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 195 */       } else if (this.dragging) {
/* 196 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 202 */     GlStateManager.disableBlend();
/* 203 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   public void drawTexturedModalRect(int x, int y, int textureX, int textureY, int width, int height) {
/* 207 */     float f = 0.00390625F;
/* 208 */     float f1 = 0.00390625F;
/* 209 */     float zLevel = 0.0F;
/* 210 */     Tessellator tessellator = Tessellator.getInstance();
/* 211 */     WorldRenderer worldrenderer = tessellator.getBuffer();
/* 212 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
/* 213 */     worldrenderer.pos((x + 0), (y + height), zLevel).func_181673_a(((textureX + 0) * f), ((textureY + height) * f1)).endVertex();
/* 214 */     worldrenderer.pos((x + width), (y + height), zLevel).func_181673_a(((textureX + width) * f), ((textureY + height) * f1)).endVertex();
/* 215 */     worldrenderer.pos((x + width), (y + 0), zLevel).func_181673_a(((textureX + width) * f), ((textureY + 0) * f1)).endVertex();
/* 216 */     worldrenderer.pos((x + 0), (y + 0), zLevel).func_181673_a(((textureX + 0) * f), ((textureY + 0) * f1)).endVertex();
/* 217 */     tessellator.draw();
/*     */   }
/*     */   
/*     */   enum Modes
/*     */   {
/* 222 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\StatusEffect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */