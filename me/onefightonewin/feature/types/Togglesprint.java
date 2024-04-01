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
/*     */ import me.onefightonewin.gui.framework.TextPattern;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuDataObject;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuTextField;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class Togglesprint
/*     */   extends Feature
/*     */   implements DrawImpl {
/*  33 */   final int bg = (new Color(0, 0, 0, 155)).getRGB();
/*     */   
/*     */   MenuCheckbox toggleSneak;
/*     */   
/*     */   MenuLabel color;
/*     */   
/*     */   MenuColorPicker colorDef;
/*     */   
/*     */   MenuDropdown colorModes;
/*     */   MenuLabel sprintNameLabel;
/*     */   MenuTextField sprintName;
/*     */   MenuLabel sneakLabel;
/*     */   MenuTextField sneak;
/*     */   MenuLabel backgroundLabel;
/*     */   MenuColorPicker backgroundColor;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*     */   boolean[] states;
/*     */   boolean[] presses;
/*     */   
/*     */   public Togglesprint(Minecraft minecraft) {
/*  55 */     super(minecraft, "togglesprint", -1, Category.PAGE_MISC);
/*  56 */     this.presses = new boolean[2];
/*  57 */     this.states = new boolean[2];
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  63 */     this.color = new MenuLabel("Togglesprint color modes", 0, 0);
/*  64 */     this.colorDef = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  65 */     this.colorModes = new MenuDropdown(Modes.class, 0, 0);
/*  66 */     this.toggleSneak = new MenuCheckbox("Sneak", 0, 0, 12, 12);
/*  67 */     this.sprintNameLabel = new MenuLabel("Sprint name", 0, 0);
/*  68 */     this.sprintName = new MenuTextField(TextPattern.NONE, 0, 0, 100, 12);
/*     */     
/*  70 */     if (this.sprintName.getText().trim().isEmpty()) {
/*  71 */       this.sprintName.setText("[Sprinting (Toggled)]");
/*     */     }
/*     */     
/*  74 */     this.sneakLabel = new MenuLabel("Sneak name", 0, 0);
/*  75 */     this.sneak = new MenuTextField(TextPattern.NONE, 0, 0, 100, 12);
/*     */     
/*  77 */     if (this.sneak.getText().trim().isEmpty()) {
/*  78 */       this.sneak.setText("[Sneaking (Toggled)]");
/*     */     }
/*     */     
/*  81 */     this.backgroundLabel = new MenuLabel("Togglesprint background color", 0, 0);
/*  82 */     this.backgroundColor = new MenuColorPicker(0, 0, 20, 10, this.bg);
/*     */     
/*  84 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  85 */     this.y = new MenuDataObject(getName() + "-y", "5");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  90 */     components.add(this.color);
/*  91 */     components.add(this.colorDef);
/*  92 */     components.add(this.colorModes);
/*  93 */     components.add(this.toggleSneak);
/*  94 */     components.add(this.sprintNameLabel);
/*  95 */     components.add(this.sprintName);
/*  96 */     components.add(this.sneakLabel);
/*  97 */     components.add(this.sneak);
/*  98 */     components.add(this.backgroundLabel);
/*  99 */     components.add(this.backgroundColor);
/* 100 */     components.add(this.x);
/* 101 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   enum Modes {
/* 105 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 110 */     for (int i = 0; i < 2; i++) {
/* 111 */       this.states[i] = false;
/* 112 */       this.presses[i] = false;
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 118 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     if (this.mc.currentScreen == null) {
/* 123 */       boolean prevSneak = this.presses[0];
/* 124 */       boolean prevSprint = this.presses[1];
/*     */       
/* 126 */       if (Keyboard.isKeyDown(this.mc.gameSettings.keyBindSneak.getKeyCode()) && this.toggleSneak.getRValue()) {
/* 127 */         this.presses[0] = true;
/*     */       } else {
/* 129 */         this.presses[0] = false;
/*     */       } 
/*     */       
/* 132 */       if (Keyboard.isKeyDown(this.mc.gameSettings.keyBindSprint.getKeyCode())) {
/* 133 */         this.presses[1] = true;
/*     */       } else {
/* 135 */         this.presses[1] = false;
/*     */       } 
/*     */       
/* 138 */       if (!prevSneak && this.presses[0]) {
/* 139 */         this.states[0] = !this.states[0];
/* 140 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), this.states[0]);
/* 141 */       } else if (!prevSprint && this.presses[1]) {
/* 142 */         this.states[1] = !this.states[1];
/* 143 */         KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(), this.states[1]);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/* 150 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 154 */     Mouse.poll();
/*     */     
/* 156 */     GlStateManager.pushMatrix();
/* 157 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 158 */     float value = 2.0F / sr.getScaleFactor();
/* 159 */     GlStateManager.scale(value, value, value);
/* 160 */     GlStateManager.enableBlend();
/* 161 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/* 163 */     List<String> list = new ArrayList<>();
/*     */     
/* 165 */     if (this.states[0]) {
/* 166 */       list.add(this.sneak.getText());
/*     */       
/* 168 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
/*     */       
/* 170 */       if (this.mc.currentScreen != null) {
/* 171 */         this.mc.player.setSneaking(true);
/*     */       }
/* 173 */     } else if (this.states[1]) {
/* 174 */       list.add(this.sprintName.getText());
/*     */       
/* 176 */       KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSprint.getKeyCode(), true);
/*     */     } 
/*     */     
/* 179 */     List<String> renderStrings = new ArrayList<>();
/* 180 */     int maxWidth = 0;
/* 181 */     int height = 0;
/*     */     
/* 183 */     for (String item : list) {
/* 184 */       int width = getStringWidth(item) + 4;
/*     */       
/* 186 */       if (width > maxWidth) {
/* 187 */         maxWidth = width;
/*     */       }
/*     */       
/* 190 */       renderStrings.add(item);
/*     */       
/* 192 */       height += getStringHeight(item) + 2;
/*     */     } 
/*     */     
/* 195 */     if (this.x.getIntValue() == -1 && maxWidth > 0) {
/* 196 */       this.x.setValue(sr.getScaledWidth() - maxWidth);
/*     */     }
/*     */     
/* 199 */     Collections.sort(renderStrings, new Comparator<String>()
/*     */         {
/*     */           public int compare(String a, String b) {
/* 202 */             return Integer.valueOf(Togglesprint.this.getStringWidth(b)).compareTo(Integer.valueOf(Togglesprint.this.getStringWidth(a)));
/*     */           }
/*     */         });
/*     */     
/* 206 */     height += 2;
/*     */     
/* 208 */     drawRect(this.x.getIntValue() - 5, this.y.getIntValue(), maxWidth, height, this.backgroundColor.getColor().getRGB());
/*     */     
/* 210 */     int color = (new Color(255, 255, 255, 255)).getRGB();
/*     */     
/* 212 */     if (this.colorModes.getValue().equalsIgnoreCase(Modes.RAINBOW.toString())) {
/* 213 */       color = (AntiAFK.getInstance()).rainbowColor.getRGB();
/* 214 */     } else if (this.colorModes.getValue().equalsIgnoreCase(Modes.COLOR.toString())) {
/* 215 */       color = this.colorDef.getColor().getRGB();
/*     */     } 
/*     */     
/* 218 */     int y = this.y.getIntValue() + 2;
/* 219 */     for (String feature : renderStrings) {
/* 220 */       drawText(feature, this.x.getIntValue() - 3, y, color);
/*     */       
/* 222 */       y += getStringHeight(feature) + 2;
/*     */     } 
/*     */     
/* 225 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 226 */       this.mc.currentScreen != null) {
/* 227 */       if (Mouse.isButtonDown(0)) {
/* 228 */         int mouseX = Mouse.getX();
/* 229 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 231 */         mouseX = Math.round(mouseX / 2.0F);
/* 232 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 234 */         if (!this.dragging) {
/* 235 */           if (mouseX >= this.x.getIntValue() && mouseX <= this.x.getIntValue() + maxWidth && 
/* 236 */             mouseY >= this.y.getIntValue() && mouseY <= this.y.getIntValue() + height) {
/* 237 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 241 */           this.x.setValue(mouseX);
/* 242 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 245 */       } else if (this.dragging) {
/* 246 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 252 */     GlStateManager.disableBlend();
/* 253 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Togglesprint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */