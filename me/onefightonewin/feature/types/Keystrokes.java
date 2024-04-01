/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.DrawImpl;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuDataObject;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Keystrokes
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuLabel modesLabel;
/*     */   MenuLabel modesLineLabel;
/*     */   MenuDropdown modes;
/*     */   MenuColorPicker setColor;
/*     */   MenuColorPicker setLineColor;
/*     */   MenuCheckbox rightCps;
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean lastLeft = false;
/*     */   boolean lastRight = false;
/*  45 */   final int background = (new Color(200, 200, 200, 125)).getRGB();
/*  46 */   final int text = (new Color(10, 10, 10, 125)).getRGB();
/*     */   
/*  48 */   final int bar = (new Color(200, 200, 200, 255)).getRGB();
/*  49 */   final int barInverted = (new Color(10, 10, 10, 255)).getRGB();
/*     */   
/*     */   int leftCPS;
/*     */   
/*     */   int rightCPS;
/*     */   
/*     */   boolean dragging;
/*     */   List<Integer> leftClickTicks;
/*     */   List<Integer> rightClickTicks;
/*     */   List<Integer> leftClicks;
/*     */   List<Integer> rightClicks;
/*     */   int tickcount;
/*     */   
/*     */   public Keystrokes(Minecraft minecraft) {
/*  63 */     super(minecraft, "keystrokes", -1, Category.PAGE_6);
/*  64 */     this.leftClicks = new ArrayList<>();
/*  65 */     this.rightClicks = new ArrayList<>();
/*  66 */     this.leftClickTicks = new ArrayList<>();
/*  67 */     this.rightClickTicks = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  72 */     this.modesLabel = new MenuLabel("CPS color", 0, 0);
/*  73 */     this.setColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*  74 */     this.modesLineLabel = new MenuLabel("CPS line color", 0, 0);
/*  75 */     this.setLineColor = new MenuColorPicker(0, 0, 20, 10, Color.WHITE.getRGB());
/*     */     
/*  77 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  78 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*  79 */     this.rightCps = new MenuCheckbox("Right CPS", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  84 */     components.add(this.modesLabel);
/*  85 */     components.add(this.setColor);
/*  86 */     components.add(this.modesLineLabel);
/*  87 */     components.add(this.setLineColor);
/*     */     
/*  89 */     components.add(this.x);
/*  90 */     components.add(this.y);
/*  91 */     components.add(this.rightCps);
/*     */   }
/*     */   
/*     */   enum Modes {
/*  95 */     COLOR, RAINBOW, NO_COLOR;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 100 */     this.lastLeft = false;
/* 101 */     this.lastRight = false;
/* 102 */     this.leftCPS = 0;
/* 103 */     this.rightCPS = 0;
/* 104 */     this.tickcount = 0;
/* 105 */     this.leftClicks.clear();
/* 106 */     this.rightClicks.clear();
/* 107 */     this.leftClickTicks.clear();
/* 108 */     this.rightClickTicks.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public void leftClick() {
/* 113 */     this.leftClickTicks.add(Integer.valueOf(this.tickcount));
/* 114 */     this.leftClicks.add(Integer.valueOf(this.tickcount));
/*     */   }
/*     */ 
/*     */   
/*     */   public void rightClick() {
/* 119 */     this.rightClickTicks.add(Integer.valueOf(this.tickcount));
/* 120 */     this.rightClicks.add(Integer.valueOf(this.tickcount));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 125 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 129 */     if (event.phase == TickEvent.Phase.START) {
/*     */       return;
/*     */     }
/*     */     
/* 133 */     Mouse.poll();
/*     */     
/* 135 */     if (Mouse.isButtonDown(0)) {
/* 136 */       if (!this.lastLeft) {
/* 137 */         leftClick();
/*     */       }
/* 139 */       this.lastLeft = true;
/*     */     } else {
/* 141 */       this.lastLeft = false;
/*     */     } 
/*     */     
/* 144 */     if (Mouse.isButtonDown(1)) {
/* 145 */       if (!this.lastRight) {
/* 146 */         rightClick();
/*     */       }
/* 148 */       this.lastRight = true;
/*     */     } else {
/* 150 */       this.lastRight = false;
/*     */     } 
/*     */     
/* 153 */     int leftCPS = 0;
/* 154 */     int rightCPS = 0;
/*     */     
/* 156 */     Iterator<Integer> iterator = this.leftClicks.iterator();
/*     */     
/* 158 */     while (iterator.hasNext()) {
/* 159 */       int tick = ((Integer)iterator.next()).intValue();
/*     */       
/* 161 */       int tickDiff = this.tickcount - tick;
/*     */       
/* 163 */       if (tickDiff > 20 || (this.tickcount < tick && this.tickcount + 60 - tick > 20)) {
/* 164 */         iterator.remove(); continue;
/*     */       } 
/* 166 */       leftCPS++;
/*     */     } 
/*     */ 
/*     */     
/* 170 */     iterator = this.rightClicks.iterator();
/*     */     
/* 172 */     while (iterator.hasNext()) {
/* 173 */       int tick = ((Integer)iterator.next()).intValue();
/*     */       
/* 175 */       int tickDiff = this.tickcount - tick;
/*     */       
/* 177 */       if (tickDiff > 20 || (this.tickcount < tick && this.tickcount + 60 - tick > 20)) {
/* 178 */         iterator.remove(); continue;
/*     */       } 
/* 180 */       rightCPS++;
/*     */     } 
/*     */ 
/*     */     
/* 184 */     this.leftCPS = leftCPS;
/* 185 */     this.rightCPS = rightCPS;
/*     */     
/* 187 */     this.tickcount++;
/*     */     
/* 189 */     if (this.tickcount > 60) {
/* 190 */       this.tickcount = 0;
/*     */     }
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent event) {
/* 196 */     if (event.type != RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
/*     */       return;
/*     */     }
/*     */     
/* 200 */     if (this.x.getIntValue() == -1) {
/* 201 */       this.x.setValue(MathHelper.ceil(this.mc.displayWidth / 2.0D) - 125);
/*     */     }
/*     */     
/* 204 */     if (this.y.getIntValue() == -1) {
/* 205 */       this.y.setValue(MathHelper.ceil(this.mc.displayHeight / 2.0D) - 100);
/*     */     }
/*     */     
/* 208 */     Mouse.poll();
/*     */     
/* 210 */     String text = "[" + this.leftCPS + (this.rightCps.getRValue() ? (" | " + this.rightCPS) : "") + " CPS]";
/*     */     
/* 212 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 213 */       this.mc.currentScreen != null) {
/* 214 */       if (Mouse.isButtonDown(0)) {
/* 215 */         int maxWidth = this.mc.fontRenderer.getStringWidth(text);
/* 216 */         int maxHeight = this.mc.fontRenderer.FONT_HEIGHT;
/* 217 */         int mouseX = Mouse.getX();
/* 218 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 220 */         mouseX = Math.round(mouseX / 2.0F);
/* 221 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 223 */         if (!this.dragging) {
/* 224 */           if (mouseX >= this.x.getIntValue() && mouseX <= this.x.getIntValue() + maxWidth && 
/* 225 */             mouseY >= this.y.getIntValue() && mouseY <= this.y.getIntValue() + maxHeight) {
/* 226 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 230 */           this.x.setValue(mouseX - 10);
/* 231 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 234 */       } else if (this.dragging) {
/* 235 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 241 */     boolean mnanualAuto = false;
/* 242 */     Autoclicker clicker = (Autoclicker)AntiAFK.getInstance().getFeature(Autoclicker.class);
/*     */     
/* 244 */     if (clicker.isEnabled() && clicker.hasCondition(Autoclicker.Options.ON_LEFT_CLICK_ONLY) && 
/* 245 */       this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/* 246 */       mnanualAuto = true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 251 */     GlStateManager.pushMatrix();
/* 252 */     float value = 2.0F / (new ScaledResolution(Minecraft.getMinecraft())).getScaleFactor();
/* 253 */     GlStateManager.scale(value, value, value);
/* 254 */     GlStateManager.enableBlend();
/* 255 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*     */     
/* 257 */     int baseHeight = this.y.getIntValue();
/* 258 */     int baseWidth = this.x.getIntValue();
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
/* 318 */     if (this.rightCps.getRValue()) {
/* 319 */       text = "[" + this.leftCPS;
/* 320 */       int width = getStringWidth(text);
/*     */       
/* 322 */       drawText(text, baseWidth, baseHeight, this.setColor.getColor().getRGB());
/*     */       
/* 324 */       text = " | ";
/*     */       
/* 326 */       drawText(text, baseWidth + width, baseHeight, this.setLineColor.getColor().getRGB());
/*     */       
/* 328 */       width += getStringWidth(text);
/* 329 */       text = this.rightCPS + " CPS]";
/*     */       
/* 331 */       drawText(text, baseWidth + width, baseHeight, this.setColor.getColor().getRGB());
/*     */     } else {
/*     */       
/* 334 */       drawText(text, baseWidth, baseHeight, this.setColor.getColor().getRGB());
/*     */     } 
/*     */     
/* 337 */     GlStateManager.color(1.0F, 1.0F, 1.0F);
/* 338 */     GlStateManager.disableBlend();
/* 339 */     GlStateManager.popMatrix();
/*     */   }
/*     */   
/*     */   private boolean isClickTick(String string) {
/* 343 */     int ticklifespan = 2;
/* 344 */     if (string.contentEquals("left")) {
/* 345 */       Iterator<Integer> iterator = this.leftClickTicks.iterator();
/*     */       
/* 347 */       if (iterator.hasNext()) {
/* 348 */         int tick = ((Integer)iterator.next()).intValue();
/*     */         
/* 350 */         int tickDiff = this.tickcount - tick;
/*     */         
/* 352 */         if (tickDiff >= ticklifespan || (this.tickcount < tick && this.tickcount + 60 - tick >= ticklifespan)) {
/* 353 */           iterator.remove();
/*     */         }
/* 355 */         return true;
/*     */       } 
/* 357 */     } else if (string.equalsIgnoreCase("right")) {
/* 358 */       Iterator<Integer> iterator = this.rightClickTicks.iterator();
/*     */       
/* 360 */       if (iterator.hasNext()) {
/* 361 */         int tick = ((Integer)iterator.next()).intValue();
/* 362 */         int tickDiff = this.tickcount - tick;
/*     */         
/* 364 */         if (tickDiff >= ticklifespan || (this.tickcount < tick && this.tickcount + 60 - tick >= ticklifespan)) {
/* 365 */           iterator.remove();
/*     */         }
/*     */         
/* 368 */         return true;
/*     */       } 
/*     */     } 
/*     */     
/* 372 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Keystrokes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */