/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class MenuBindButton
/*     */   extends MenuComponent
/*     */ {
/*  12 */   String text = "Click to bind.";
/*  13 */   int minOffset = 2;
/*     */   
/*     */   boolean mouseDown = false;
/*     */   boolean active = false;
/*     */   int bind;
/*     */   
/*     */   public MenuBindButton(int x, int y, int width, int height) {
/*  20 */     super(x, y, width, height);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  25 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  26 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  27 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  28 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  29 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  31 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  32 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  33 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  34 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  35 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  37 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  38 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  39 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  40 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  41 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyDown(char character, int key) {
/*  46 */     if (this.active) {
/*  47 */       this.bind = key;
/*  48 */       this.active = false;
/*  49 */       onBindAction();
/*  50 */       this.text = "Key bound: " + Keyboard.getKeyName(key) + ".";
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onExitGui(int key) {
/*  56 */     if (this.active && key == 1) {
/*  57 */       this.active = false;
/*  58 */       this.bind = 0;
/*  59 */       this.text = "Click to bind.";
/*  60 */       onBindAction();
/*  61 */       return true;
/*  62 */     }  if (this.active) {
/*  63 */       this.bind = 0;
/*  64 */       this.text = "Click to bind.";
/*     */     } 
/*  66 */     this.active = false;
/*  67 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  72 */     if (button == 0) {
/*  73 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  78 */     if (this.disabled) {
/*  79 */       return true;
/*     */     }
/*  81 */     int x = getRenderX();
/*  82 */     int y = getRenderY();
/*  83 */     int mouseX = this.parent.getMouseX();
/*  84 */     int mouseY = this.parent.getMouseY();
/*     */     
/*  86 */     if (this.mouseDown && 
/*  87 */       mouseX >= x && mouseX <= x + this.width && 
/*  88 */       mouseY >= y && mouseY <= y + this.height + 1) {
/*  89 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  93 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender() {
/*  99 */     int x = getRenderX();
/* 100 */     int y = getRenderY();
/* 101 */     int width = this.width;
/* 102 */     int height = this.height;
/* 103 */     int mouseX = this.parent.getMouseX();
/* 104 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 106 */     ButtonState state = this.active ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*     */     
/* 108 */     if (!this.disabled) {
/* 109 */       if (mouseX >= x && mouseX <= x + width && 
/* 110 */         mouseY >= y && mouseY <= y + height + 1) {
/* 111 */         state = ButtonState.HOVER;
/*     */         
/* 113 */         if (this.mouseDown) {
/* 114 */           this.active = !this.active;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       
/* 119 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/* 122 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 123 */     int lineColor = getColor(DrawType.LINE, state);
/* 124 */     int textColor = getColor(DrawType.TEXT, state);
/*     */     
/* 126 */     drawRect(x + 1, y + 1, width - 1, height - 1, backgroundColor);
/* 127 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 128 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 129 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 130 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/*     */     
/* 132 */     if (this.active) {
/* 133 */       this.text = "Press a key.";
/*     */     }
/* 135 */     drawText(this.text, x + width / 2 - getStringWidth(this.text) / 2, y + height / 2 - getStringHeight(this.text) / 2, textColor);
/* 136 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public int getKey() {
/* 140 */     return this.bind;
/*     */   }
/*     */   
/*     */   public void setKey(int key) {
/* 144 */     this.bind = key;
/* 145 */     if (key == 0) {
/* 146 */       this.text = "Click to bind.";
/*     */     } else {
/* 148 */       this.text = "Key bound: " + Keyboard.getKeyName(key) + ".";
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getText() {
/* 153 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 157 */     this.text = text;
/*     */   }
/*     */   
/*     */   public boolean isActive() {
/* 161 */     return this.active;
/*     */   }
/*     */   
/*     */   public void setActive(boolean active) {
/* 165 */     this.active = active;
/*     */   }
/*     */   
/*     */   public void onBindAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuBindButton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */