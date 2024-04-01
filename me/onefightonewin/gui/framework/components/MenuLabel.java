/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MenuPriority;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ 
/*     */ public class MenuLabel
/*     */   extends MenuComponent
/*     */ {
/*     */   String text;
/*     */   String tooltip;
/*     */   ButtonState lastState;
/*  17 */   int minOffset = 2;
/*     */   boolean mouseDown = false;
/*     */   
/*     */   public MenuLabel(String text, String tooltip, int x, int y) {
/*  21 */     super(x, y, 0, 0);
/*  22 */     this.text = text;
/*  23 */     this.tooltip = tooltip;
/*     */   }
/*     */   
/*     */   public MenuLabel(String text, int x, int y) {
/*  27 */     super(x, y, 0, 0);
/*  28 */     this.text = text;
/*  29 */     this.tooltip = "";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  35 */     setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(10, 10, 10, 255));
/*     */     
/*  37 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(100, 120, 255, 255));
/*     */     
/*  39 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  40 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  41 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  42 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  43 */     setColor(DrawType.TEXT, ButtonState.POPUP, new Color(100, 100, 100, 255));
/*  44 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean passesThrough() {
/*  49 */     if (this.disabled) {
/*  50 */       return true;
/*     */     }
/*  52 */     int x = getRenderX();
/*  53 */     int y = getRenderY();
/*  54 */     int mouseX = this.parent.getMouseX();
/*  55 */     int mouseY = this.parent.getMouseY();
/*  56 */     int width = getStringWidth(this.text);
/*  57 */     int height = getStringHeight(this.text);
/*     */     
/*  59 */     if (this.mouseDown && 
/*  60 */       mouseX >= x && mouseX <= x + width && 
/*  61 */       mouseY >= y && mouseY <= y + height + 1) {
/*  62 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  72 */     if (button == 0) {
/*  73 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onRender() {
/*  78 */     int x = getRenderX();
/*  79 */     int y = getRenderY();
/*  80 */     int width = getStringWidth(this.text);
/*  81 */     int height = getStringHeight(this.text);
/*  82 */     int mouseX = this.parent.getMouseX();
/*  83 */     int mouseY = this.parent.getMouseY();
/*  84 */     ButtonState state = ButtonState.NORMAL;
/*     */     
/*  86 */     if (!this.disabled) {
/*  87 */       if (mouseX >= x && mouseX <= x + width && 
/*  88 */         mouseY >= y && mouseY <= y + height) {
/*  89 */         state = ButtonState.HOVER;
/*     */         
/*  91 */         if (this.mouseDown) {
/*  92 */           onAction();
/*     */         }
/*     */       } 
/*     */     } else {
/*  96 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/*  99 */     setPriority((state == ButtonState.HOVER) ? MenuPriority.HIGHEST : MenuPriority.MEDIUM);
/*     */     
/* 101 */     drawText(this.text, x, y, getColor(DrawType.TEXT, state));
/*     */     
/* 103 */     this.lastState = state;
/* 104 */     drawTooltip();
/*     */     
/* 106 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public void drawTooltip() {
/* 110 */     if (this.tooltip.length() > 0 && (this.lastState == ButtonState.HOVER || this.lastState == ButtonState.HOVERACTIVE)) {
/* 111 */       int tipWidth = getStringWidth(this.tooltip) + this.minOffset * 2;
/* 112 */       int tipHeight = getStringHeight(this.tooltip) + this.minOffset * 2;
/* 113 */       int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
/* 114 */       int mouseX = this.parent.getMouseX();
/* 115 */       int mouseY = this.parent.getMouseY() - tipHeight;
/*     */       
/* 117 */       ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
/* 118 */       if (mouseX + tipWidth >= res.getScaledWidth()) {
/* 119 */         mouseX -= tipWidth;
/*     */       }
/* 121 */       drawRect(mouseX, mouseY, tipWidth, tipHeight, getColor(DrawType.BACKGROUND, ButtonState.POPUP));
/* 122 */       drawHorizontalLine(mouseX, mouseY, tipWidth + 1, 1, lineColor);
/* 123 */       drawVerticalLine(mouseX, mouseY + 1, tipHeight - 1, 1, lineColor);
/* 124 */       drawHorizontalLine(mouseX, mouseY + tipHeight, tipWidth + 1, 1, lineColor);
/* 125 */       drawVerticalLine(mouseX + tipWidth, mouseY + 1, tipHeight - 1, 1, lineColor);
/* 126 */       drawText(this.tooltip, mouseX + this.minOffset, mouseY + this.minOffset, getColor(DrawType.TEXT, ButtonState.POPUP));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 132 */     return getStringHeight(this.text);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 137 */     return getStringWidth(this.text);
/*     */   }
/*     */   
/*     */   public String getText() {
/* 141 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 145 */     this.text = text;
/*     */   }
/*     */   
/*     */   public String getTooltip() {
/* 149 */     return this.tooltip;
/*     */   }
/*     */   
/*     */   public void setTooltip(String tooltip) {
/* 153 */     this.tooltip = tooltip;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuLabel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */