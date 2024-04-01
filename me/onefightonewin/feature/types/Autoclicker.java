/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.AWTException;
/*     */ import java.awt.Robot;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
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
/*     */ public class Autoclicker
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel optionsLabel;
/*     */   MenuComboBox options;
/*     */   MenuLabel cpsMinLabel;
/*     */   MenuSlider cpsMin;
/*     */   MenuLabel cpsMaxLabel;
/*     */   MenuSlider cpsMax;
/*     */   final Method method;
/*     */   final Method methodBlock;
/*     */   final Field field;
/*     */   Random random;
/*     */   int interval;
/*     */   int ticksRemaining;
/*     */   int cps;
/*     */   long lastTime;
/*     */   Robot robot;
/*     */   
/*     */   public Autoclicker(Minecraft minecraft) {
/*  57 */     super(minecraft, "autoclicker", 0, Category.PAGE_1);
/*  58 */     this.random = new Random();
/*  59 */     this.method = ReflectionHelper.findMethod(Minecraft.class, minecraft, new String[] { "clickMouse", "func_147116_af" }, new Class[0]);
/*  60 */     this.methodBlock = ReflectionHelper.findMethod(Minecraft.class, minecraft, new String[] { "rightClickMouse", "func_147121_ag" }, new Class[0]);
/*  61 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "leftClickCounter", "field_71429_W" });
/*     */     try {
/*  63 */       this.robot = new Robot();
/*  64 */     } catch (AWTException e) {
/*  65 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  71 */     this.optionsLabel = new MenuLabel("Autoclicker options", 0, 0);
/*  72 */     this.options = new MenuComboBox(Options.class, 0, 0);
/*  73 */     this.cpsMinLabel = new MenuLabel("CPS Min", 0, 0);
/*  74 */     this.cpsMin = new MenuSlider(5, 1, 25, 0, 0, 100, 6);
/*  75 */     this.cpsMaxLabel = new MenuLabel("CPS Max", 0, 0);
/*  76 */     this.cpsMax = new MenuSlider(5, 1, 25, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  81 */     components.add(this.optionsLabel);
/*  82 */     components.add(this.options);
/*  83 */     components.add(this.cpsMinLabel);
/*  84 */     components.add(this.cpsMin);
/*  85 */     components.add(this.cpsMaxLabel);
/*  86 */     components.add(this.cpsMax);
/*     */   }
/*     */   
/*     */   enum Options {
/*  90 */     BREAK_BLOCKS, INVENTORY, ON_LEFT_CLICK_ONLY, WEAPONS_ONLY, BLOCK_HIT;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Options cond) {
/*  94 */     for (String condition : this.options.getValues()) {
/*  95 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  96 */         return true; 
/*     */     } 
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 103 */     this.lastTime = 0L;
/* 104 */     this.cps = 0;
/* 105 */     this.ticksRemaining = 0;
/* 106 */     this.interval = 0;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/* 111 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 115 */     if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */     
/* 119 */     boolean mouseDown = this.mc.gameSettings.keyBindAttack.isKeyDown();
/*     */     
/* 121 */     if (hasCondition(Options.INVENTORY) && (this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainerCreative || this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory) && 
/* 122 */       Mouse.isButtonDown(0) && Keyboard.isKeyDown(42)) {
/* 123 */       this.robot.mouseRelease(16);
/* 124 */       this.robot.mousePress(16);
/* 125 */       mouseDown = false;
/*     */     } 
/*     */ 
/*     */     
/* 129 */     if (hasCondition(Options.ON_LEFT_CLICK_ONLY) && 
/* 130 */       !mouseDown) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 135 */     if (hasCondition(Options.WEAPONS_ONLY)) {
/* 136 */       if (this.mc.player.func_70694_bm() != null) {
/* 137 */         ItemStack item = this.mc.player.func_70694_bm();
/* 138 */         if (!(item.getItem() instanceof net.minecraft.item.ItemAxe) && !(item.getItem() instanceof net.minecraft.item.ItemSword)) {
/*     */           return;
/*     */         }
/*     */       } else {
/*     */         return;
/*     */       } 
/*     */     }
/*     */     
/* 146 */     if (!hasCondition(Options.BREAK_BLOCKS)) {
/* 147 */       MovingObjectPosition movingObjectPosition = this.mc.objectMouseOver;
/*     */       
/* 149 */       if (movingObjectPosition == null) {
/*     */         return;
/*     */       }
/* 152 */       if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/* 157 */     MovingObjectPosition obj = this.mc.objectMouseOver;
/*     */     
/* 159 */     if (obj != null && 
/* 160 */       obj.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
/* 161 */       Entity entity = obj.entityHit;
/*     */       
/* 163 */       if (((AntiEvade)AntiAFK.getInstance().getFeature(AntiEvade.class)).isEntityEvading(entity)) {
/*     */         return;
/*     */       }
/*     */       
/* 167 */       if (((AntiDisengage)AntiAFK.getInstance().getFeature(AntiDisengage.class)).isEntityEvading(entity)) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 173 */     Keystrokes strokes = (Keystrokes)AntiAFK.getInstance().getFeature(Keystrokes.class);
/*     */     
/* 175 */     if (this.ticksRemaining > 0) {
/* 176 */       this.ticksRemaining--;
/*     */     } else {
/* 178 */       int min = this.cpsMin.getIntValue();
/* 179 */       int max = this.cpsMax.getIntValue();
/*     */       
/* 181 */       int cps = min;
/*     */       
/* 183 */       if (max > min) {
/* 184 */         cps += this.random.nextInt(max - min);
/*     */       }
/*     */       
/* 187 */       this.cps = cps;
/* 188 */       this.interval = Math.round(1000.0F / cps);
/* 189 */       this.ticksRemaining = 20;
/*     */     } 
/*     */     
/* 192 */     long time = System.currentTimeMillis();
/* 193 */     boolean reached = false;
/* 194 */     boolean enabled = false;
/*     */     
/* 196 */     if (strokes.isEnabled()) {
/* 197 */       enabled = true;
/*     */       
/* 199 */       if (strokes.leftCPS > this.cps) {
/* 200 */         reached = true;
/*     */       }
/*     */     } 
/*     */     
/* 204 */     enabled = false;
/*     */     
/* 206 */     if ((time - this.lastTime > this.interval && !enabled) || (enabled && !reached)) {
/* 207 */       this.lastTime = time;
/*     */       try {
/* 209 */         this.field.setInt(this.mc, 0);
/* 210 */         this.method.invoke(this.mc, new Object[0]);
/*     */         
/* 212 */         if (strokes.isEnabled()) {
/* 213 */           strokes.leftClick();
/*     */         }
/*     */         
/* 216 */         if (hasCondition(Options.BLOCK_HIT)) {
/* 217 */           this.methodBlock.invoke(this.mc, new Object[0]);
/*     */           
/* 219 */           if (strokes.isEnabled()) {
/* 220 */             strokes.rightClick();
/*     */           }
/*     */         } 
/* 223 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 224 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Autoclicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */