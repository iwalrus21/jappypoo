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
/*     */ 
/*     */ 
/*     */ public class MenuCheckbox
/*     */   extends MenuComponent
/*     */ {
/*     */   String text;
/*     */   String tooltip;
/*     */   boolean checked;
/*     */   int bind;
/*     */   ButtonState lastState;
/*     */   boolean checkable = true;
/*  22 */   int textOffset = 4;
/*  23 */   int minOffset = 2;
/*  24 */   int optionWindowWidth = 75;
/*  25 */   int optionWindowHeight = 36;
/*     */   boolean mouseDown = false;
/*     */   
/*     */   public MenuCheckbox(String text, String tooltip, int x, int y, int width, int height) {
/*  29 */     super(x, y, width, height);
/*  30 */     this.text = text;
/*  31 */     this.tooltip = tooltip;
/*  32 */     this.bind = 0;
/*     */   }
/*     */   
/*     */   public MenuCheckbox(String text, int x, int y, int width, int height) {
/*  36 */     super(x, y, width, height);
/*  37 */     this.text = text;
/*  38 */     this.tooltip = "";
/*  39 */     this.bind = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  44 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  45 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(81, 108, 255, 255));
/*  46 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  47 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(100, 120, 255, 255));
/*  48 */     setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(10, 10, 10, 255));
/*  49 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  51 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  52 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  53 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
/*  54 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(30, 30, 30, 255));
/*  55 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(100, 120, 255, 255));
/*  56 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  58 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  59 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  60 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  61 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  62 */     setColor(DrawType.TEXT, ButtonState.POPUP, new Color(100, 100, 100, 255));
/*  63 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  68 */     if (button == 0) {
/*  69 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  74 */     if (this.disabled) {
/*  75 */       return true;
/*     */     }
/*  77 */     int mouseX = this.parent.getMouseX();
/*  78 */     int mouseY = this.parent.getMouseY();
/*     */     
/*  80 */     if (this.mouseDown) {
/*  81 */       int x = getRenderX();
/*  82 */       int y = getRenderY();
/*  83 */       int width = getStringWidth(this.text) + this.width + this.textOffset;
/*  84 */       int height = getStringHeight(this.text);
/*  85 */       if (mouseX >= x && mouseX <= x + width && 
/*  86 */         mouseY >= y && mouseY <= y + height + 1) {
/*  87 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  91 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/*  96 */     int x = getRenderX();
/*  97 */     int y = getRenderY();
/*  98 */     int width = getStringWidth(this.text) + this.width + this.textOffset;
/*  99 */     int height = getStringHeight(this.text);
/* 100 */     int mouseX = this.parent.getMouseX();
/* 101 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 103 */     ButtonState state = this.checked ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*     */     
/* 105 */     if (!this.disabled) {
/* 106 */       if (mouseX >= x && mouseX <= x + width && 
/* 107 */         mouseY >= y && mouseY <= y + height) {
/* 108 */         state = this.checked ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
/*     */         
/* 110 */         if (this.tooltip.length() > 0) {
/* 111 */           setPriority(MenuPriority.HIGHEST);
/*     */         }
/* 113 */         if (this.mouseDown && this.checkable) {
/* 114 */           setChecked(!isChecked());
/*     */         }
/*     */       } 
/*     */     } else {
/* 118 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/* 121 */     ButtonState savedState = state;
/*     */     
/* 123 */     if (!this.checkable) {
/* 124 */       state = ButtonState.DISABLED;
/*     */     }
/* 126 */     width = this.width;
/* 127 */     height = this.height;
/*     */     
/* 129 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 130 */     int lineColor = getColor(DrawType.LINE, state);
/* 131 */     int textColor = getColor(DrawType.TEXT, state);
/*     */     
/* 133 */     drawRect(x + 2, y + 2, width - 3, height - 3, backgroundColor);
/* 134 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 135 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 136 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 137 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 138 */     drawText(this.text, x + width + this.textOffset, y + 1, textColor);
/*     */     
/* 140 */     state = savedState;
/* 141 */     this.lastState = state;
/* 142 */     drawTooltip();
/*     */     
/* 144 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public void drawTooltip() {
/* 148 */     if (this.tooltip.length() > 0 && (this.lastState == ButtonState.HOVER || this.lastState == ButtonState.HOVERACTIVE)) {
/* 149 */       int tipWidth = getStringWidth(this.tooltip) + this.minOffset * 2;
/* 150 */       int tipHeight = getStringHeight(this.tooltip) + this.minOffset * 2;
/* 151 */       int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
/* 152 */       int mouseX = this.parent.getMouseX();
/* 153 */       int mouseY = this.parent.getMouseY() - tipHeight;
/*     */       
/* 155 */       ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
/*     */       
/* 157 */       if (mouseX + tipWidth >= res.getScaledWidth()) {
/* 158 */         mouseX -= tipWidth;
/*     */       }
/* 160 */       drawRect(mouseX, mouseY, tipWidth, tipHeight, getColor(DrawType.BACKGROUND, ButtonState.POPUP));
/* 161 */       drawHorizontalLine(mouseX, mouseY, tipWidth + 1, 1, lineColor);
/* 162 */       drawVerticalLine(mouseX, mouseY + 1, tipHeight - 1, 1, lineColor);
/* 163 */       drawHorizontalLine(mouseX, mouseY + tipHeight, tipWidth + 1, 1, lineColor);
/* 164 */       drawVerticalLine(mouseX + tipWidth, mouseY + 1, tipHeight - 1, 1, lineColor);
/* 165 */       drawText(this.tooltip, mouseX + this.minOffset, mouseY + this.minOffset, getColor(DrawType.TEXT, ButtonState.POPUP));
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isChecked() {
/* 170 */     return this.checked;
/*     */   }
/*     */   
/*     */   public void setChecked(boolean checked) {
/* 174 */     this.checked = checked;
/* 175 */     onAction();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getRValue() {
/* 181 */     return isChecked();
/*     */   }
/*     */   
/*     */   public String getText() {
/* 185 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 189 */     this.text = text;
/*     */   }
/*     */   
/*     */   public String getTooltip() {
/* 193 */     return this.tooltip;
/*     */   }
/*     */   
/*     */   public void setTooltip(String tooltip) {
/* 197 */     this.tooltip = tooltip;
/*     */   }
/*     */   
/*     */   public int getBind() {
/* 201 */     return this.bind;
/*     */   }
/*     */   
/*     */   public void setBind(int key) {
/* 205 */     this.bind = key;
/*     */   }
/*     */   
/*     */   public boolean isCheckable() {
/* 209 */     return this.checkable;
/*     */   }
/*     */   
/*     */   public void setCheckable(boolean checkable) {
/* 213 */     this.checkable = checkable;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuCheckbox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */