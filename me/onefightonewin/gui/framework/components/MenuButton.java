/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ 
/*     */ public class MenuButton
/*     */   extends MenuComponent {
/*     */   protected String text;
/*  11 */   protected int minOffset = 2;
/*     */   protected boolean mouseDown = false;
/*     */   protected boolean active = false;
/*     */   
/*     */   public MenuButton(String text, int x, int y) {
/*  16 */     super(x, y, -1, -1);
/*  17 */     this.text = text;
/*     */   }
/*     */   
/*     */   public MenuButton(String text, int x, int y, int width, int height) {
/*  21 */     super(x, y, width, height);
/*  22 */     this.text = text;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  27 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  28 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  29 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  30 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  31 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  33 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  34 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  35 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  36 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  37 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  39 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  40 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  41 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  42 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  43 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  48 */     if (button == 0) {
/*  49 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  54 */     if (this.disabled) {
/*  55 */       return true;
/*     */     }
/*  57 */     int x = getRenderX();
/*  58 */     int y = getRenderY();
/*  59 */     int mouseX = this.parent.getMouseX();
/*  60 */     int mouseY = this.parent.getMouseY();
/*     */     
/*  62 */     if (this.mouseDown && 
/*  63 */       mouseX >= x && mouseX <= x + this.width && 
/*  64 */       mouseY >= y && mouseY <= y + this.height + 1) {
/*  65 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  69 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender() {
/*  75 */     int x = getRenderX();
/*  76 */     int y = getRenderY();
/*  77 */     int width = (this.width == -1 && this.height == -1) ? (getStringWidth(this.text) + this.minOffset * 2) : this.width;
/*  78 */     int height = (this.width == -1 && this.height == -1) ? (getStringHeight(this.text) + this.minOffset * 2) : this.height;
/*  79 */     int mouseX = this.parent.getMouseX();
/*  80 */     int mouseY = this.parent.getMouseY();
/*     */     
/*  82 */     ButtonState state = this.active ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*     */     
/*  84 */     if (!this.disabled) {
/*  85 */       if (mouseX >= x && mouseX <= x + width && 
/*  86 */         mouseY >= y && mouseY <= y + height + 1) {
/*  87 */         state = ButtonState.HOVER;
/*     */         
/*  89 */         if (this.mouseDown) {
/*  90 */           this.active = !this.active;
/*  91 */           onAction();
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/*  96 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/*  99 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 100 */     int lineColor = getColor(DrawType.LINE, state);
/* 101 */     int textColor = getColor(DrawType.TEXT, state);
/*     */     
/* 103 */     drawRect(x + 1, y + 1, width - 1, height - 1, backgroundColor);
/* 104 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 105 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 106 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 107 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 108 */     drawText(this.text, x + width / 2 - getStringWidth(this.text) / 2, y + height / 2 - getStringHeight(this.text) / 2, textColor);
/* 109 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public String getText() {
/* 113 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 117 */     this.text = text;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 121 */     return this.active;
/*     */   }
/*     */   
/*     */   public void setActive(boolean active) {
/* 125 */     this.active = active;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */