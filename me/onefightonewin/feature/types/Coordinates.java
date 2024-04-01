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
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ public class Coordinates
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuLabel modesLabel;
/*     */   MenuDropdown modes;
/*     */   MenuColorPicker setColor;
/*     */   MenuLabel backgroundLabel;
/*     */   MenuColorPicker backgroundColor;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*  36 */   final int bg = (new Color(0, 0, 0, 155)).getRGB();
/*     */   
/*     */   public Coordinates(Minecraft minecraft) {
/*  39 */     super(minecraft, "coordinates", -1, Category.PAGE_6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  44 */     this.modesLabel = new MenuLabel("Coordinates color modes", 0, 0);
/*  45 */     this.setColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  46 */     this.modes = new MenuDropdown(Modes.class, 0, 0);
/*  47 */     this.backgroundLabel = new MenuLabel("Coordinates background color", 0, 0);
/*  48 */     this.backgroundColor = new MenuColorPicker(0, 0, 20, 10, this.bg);
/*  49 */     this.x = new MenuDataObject(getName() + "-x", "10");
/*  50 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  55 */     components.add(this.modesLabel);
/*  56 */     components.add(this.setColor);
/*  57 */     components.add(this.modes);
/*  58 */     components.add(this.backgroundLabel);
/*  59 */     components.add(this.backgroundColor);
/*  60 */     components.add(this.x);
/*  61 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  66 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  70 */     Mouse.poll();
/*     */     
/*  72 */     GlStateManager.pushMatrix();
/*  73 */     ScaledResolution sr = new ScaledResolution(this.mc);
/*  74 */     float value = 2.0F / sr.getScaleFactor();
/*  75 */     GlStateManager.scale(value, value, value);
/*  76 */     GlStateManager.enableBlend();
/*  77 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/*  79 */     List<String> list = new ArrayList<>();
/*     */     
/*  81 */     String toDraw = "";
/*     */     
/*  83 */     switch (this.mc.player.getHorizontalFacing()) {
/*     */       case DOWN:
/*  85 */         toDraw = "D";
/*     */         break;
/*     */       
/*     */       case EAST:
/*  89 */         toDraw = "E";
/*     */         break;
/*     */       
/*     */       case NORTH:
/*  93 */         toDraw = "N";
/*     */         break;
/*     */       
/*     */       case SOUTH:
/*  97 */         toDraw = "S";
/*     */         break;
/*     */       
/*     */       case UP:
/* 101 */         toDraw = "U";
/*     */         break;
/*     */       
/*     */       case WEST:
/* 105 */         toDraw = "W";
/*     */         break;
/*     */       
/*     */       default:
/* 109 */         toDraw = "?";
/*     */         break;
/*     */     } 
/*     */     
/* 113 */     list.add("(" + this.mc.player.getPosition().getX() + ", " + this.mc.player.getPosition().getY() + ", " + this.mc.player.getPosition().getZ() + ") " + toDraw);
/*     */     
/* 115 */     List<String> renderStrings = new ArrayList<>();
/* 116 */     int maxWidth = 0;
/* 117 */     int height = 0;
/*     */     
/* 119 */     for (String item : list) {
/* 120 */       int width = getStringWidth(item) + 4;
/*     */       
/* 122 */       if (width > maxWidth) {
/* 123 */         maxWidth = width;
/*     */       }
/*     */       
/* 126 */       renderStrings.add(item);
/*     */       
/* 128 */       height += getStringHeight(item) + 2;
/*     */     } 
/*     */     
/* 131 */     if (this.y.getIntValue() == -1 && maxWidth > 0) {
/* 132 */       this.y.setValue(sr.getScaledHeight() / 3);
/*     */     }
/*     */     
/* 135 */     height += 2;
/*     */     
/* 137 */     drawRect(this.x.getIntValue() - 5, this.y.getIntValue(), maxWidth, height, this.backgroundColor.getColor().getRGB());
/*     */     
/* 139 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/* 141 */     if (this.modes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/* 142 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/* 143 */     } else if (this.modes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/* 144 */       color = this.setColor.getColor().getRGB();
/*     */     } 
/*     */     
/* 147 */     int y = this.y.getIntValue() + 2;
/*     */     
/* 149 */     for (int i = 0; i < renderStrings.size(); i++) {
/* 150 */       String feature = renderStrings.get(i);
/* 151 */       drawText(feature, this.x.getIntValue() - 3, y, color);
/* 152 */       y += getStringHeight(feature) + 2;
/*     */     } 
/*     */     
/* 155 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 156 */       this.mc.currentScreen != null) {
/* 157 */       if (Mouse.isButtonDown(0)) {
/* 158 */         int mouseX = Mouse.getX();
/* 159 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 161 */         mouseX = Math.round(mouseX / 2.0F);
/* 162 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 164 */         if (!this.dragging) {
/* 165 */           if (mouseX >= this.x.getIntValue() && mouseX <= this.x.getIntValue() + maxWidth && 
/* 166 */             mouseY >= this.y.getIntValue() && mouseY <= this.y.getIntValue() + height) {
/* 167 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 171 */           this.x.setValue(mouseX);
/* 172 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 175 */       } else if (this.dragging) {
/* 176 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 182 */     GlStateManager.disableBlend();
/* 183 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   enum Modes {
/* 187 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Coordinates.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */