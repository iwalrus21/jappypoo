/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.math.RoundingMode;
/*     */ import java.text.DecimalFormat;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MenuPriority;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class MenuSlider
/*     */   extends MenuComponent
/*     */ {
/*     */   boolean isFloat = false;
/*     */   float value;
/*     */   float minValue;
/*     */   float maxValue;
/*  19 */   int minOffset = 2;
/*     */   DecimalFormat format;
/*     */   boolean wantToDrag = false;
/*     */   boolean mouseDragging = false;
/*     */   boolean mouseDown = false;
/*     */   
/*     */   public MenuSlider(int startValue, int minValue, int maxValue, int x, int y, int width, int height) {
/*  26 */     super(x, y, width, height);
/*     */     
/*  28 */     init(startValue, minValue, maxValue, 1);
/*     */   }
/*     */   
/*     */   public MenuSlider(float startValue, float minValue, float maxValue, int precision, int x, int y, int width, int height) {
/*  32 */     super(x, y, width, height);
/*  33 */     this.isFloat = true;
/*     */     
/*  35 */     init(startValue, minValue, maxValue, precision);
/*     */   }
/*     */   
/*     */   private void initPrecision(int precision) {
/*  39 */     this.format = new DecimalFormat();
/*  40 */     this.format.setMaximumFractionDigits(precision);
/*  41 */     this.format.setRoundingMode(RoundingMode.HALF_DOWN);
/*     */   }
/*     */   
/*     */   private void init(float startValue, float minValue, float maxValue, int precision) {
/*  45 */     this.value = startValue;
/*  46 */     this.minValue = minValue;
/*  47 */     this.maxValue = maxValue;
/*     */     
/*  49 */     if (minValue > this.value) {
/*  50 */       this.value = minValue;
/*     */     }
/*  52 */     if (this.value > maxValue) {
/*  53 */       this.value = maxValue;
/*     */     }
/*  55 */     if (this.minValue > this.maxValue) {
/*  56 */       this.maxValue = this.minValue;
/*     */     }
/*  58 */     initPrecision((precision < 1) ? 1 : precision);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  63 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  64 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  65 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  66 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  67 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  69 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  70 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  71 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
/*  72 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
/*  73 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(81, 108, 255, 255));
/*  74 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  76 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  77 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  78 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  79 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  80 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  85 */     if (button == 0) {
/*  86 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouseClickMove(int button) {
/*  91 */     if (button == 0) {
/*  92 */       this.mouseDragging = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  97 */     if (this.disabled) {
/*  98 */       return true;
/*     */     }
/* 100 */     if (this.mouseDown) {
/* 101 */       int x = getRenderX();
/* 102 */       int y = getRenderY();
/* 103 */       int mouseX = this.parent.getMouseX();
/* 104 */       int mouseY = this.parent.getMouseY();
/* 105 */       if (mouseX >= x && mouseX <= x + this.width && 
/* 106 */         mouseY >= y && mouseY <= y + this.height) {
/* 107 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 111 */     return !this.wantToDrag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/* 116 */     setPriority(this.wantToDrag ? MenuPriority.HIGHEST : MenuPriority.HIGH);
/* 117 */     int x = getRenderX();
/* 118 */     int y = getRenderY();
/* 119 */     int width = this.width;
/* 120 */     int height = this.height;
/* 121 */     int mouseX = this.parent.getMouseX();
/* 122 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 124 */     ButtonState state = ButtonState.NORMAL;
/*     */     
/* 126 */     if (!this.disabled) {
/* 127 */       if (mouseX >= x && mouseX <= x + width && 
/* 128 */         mouseY >= y && mouseY <= y + height) {
/* 129 */         state = ButtonState.HOVER;
/*     */       }
/*     */     } else {
/*     */       
/* 133 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/* 136 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 137 */     int lineColor = getColor(DrawType.LINE, state);
/* 138 */     int textColor = getColor(DrawType.TEXT, state);
/*     */     
/* 140 */     drawRect(x + 1, y + 1, width - 1, height - 1, backgroundColor);
/* 141 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 142 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 143 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 144 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 145 */     String data = "";
/* 146 */     if (this.isFloat) {
/* 147 */       data = getValue() + "";
/*     */     } else {
/* 149 */       data = getIntValue() + "";
/*     */     } 
/* 151 */     float diff = this.maxValue - this.minValue;
/* 152 */     int linePos = x + Math.round(width * (this.value - this.minValue) / diff);
/*     */     
/* 154 */     drawRect(x, y + 1, linePos - x, height - 1, getColor(DrawType.LINE, ButtonState.POPUP));
/* 155 */     drawText(data, linePos - getStringWidth(data) / 2, y + height - 5, textColor);
/*     */     
/* 157 */     if (this.wantToDrag || (this.mouseDown && state == ButtonState.HOVER)) {
/* 158 */       if (this.mouseDown) {
/* 159 */         this.wantToDrag = true;
/*     */       }
/* 161 */       float wantedValue = this.minValue + (mouseX - this.minOffset - x) * diff / (width - this.minOffset * 2);
/*     */       
/* 163 */       if (wantedValue > this.maxValue) {
/* 164 */         wantedValue = this.maxValue;
/* 165 */       } else if (this.minValue > wantedValue) {
/* 166 */         wantedValue = this.minValue;
/*     */       } 
/* 168 */       float oldValue = this.value;
/*     */       
/* 170 */       this.value = wantedValue;
/* 171 */       if (oldValue != this.value)
/* 172 */         onAction(); 
/*     */     } 
/* 174 */     if (this.wantToDrag) {
/* 175 */       this.mouseDragging = Mouse.isButtonDown(0);
/* 176 */       this.wantToDrag = this.mouseDragging;
/*     */     } 
/* 178 */     this.mouseDragging = false;
/* 179 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public int getIntValue() {
/* 183 */     return Math.round(this.value);
/*     */   }
/*     */   
/*     */   public float getValue() {
/*     */     try {
/* 188 */       return Float.valueOf(this.format.format(this.value).replace(",", ".")).floatValue();
/* 189 */     } catch (NumberFormatException e) {
/* 190 */       e.printStackTrace();
/* 191 */       return Math.round(this.value * 10.0F) / 10.0F;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setValue(float value) {
/* 196 */     this.value = value;
/* 197 */     onAction();
/*     */   }
/*     */   
/*     */   public float getMinValue() {
/* 201 */     return this.minValue;
/*     */   }
/*     */   
/*     */   public void setMinValue(float minValue) {
/* 205 */     this.minValue = minValue;
/*     */   }
/*     */   
/*     */   public float getMaxValue() {
/* 209 */     return this.maxValue;
/*     */   }
/*     */   
/*     */   public void setMaxValue(float maxValue) {
/* 213 */     this.maxValue = maxValue;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuSlider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */