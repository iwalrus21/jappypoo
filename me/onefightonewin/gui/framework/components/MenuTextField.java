/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MenuPriority;
/*     */ import me.onefightonewin.gui.framework.TextPattern;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ 
/*     */ public class MenuTextField
/*     */   extends MenuComponent
/*     */ {
/*     */   String text;
/*  15 */   int minOffset = 2;
/*  16 */   int index = 0;
/*  17 */   int lineRefreshTime = 1000;
/*  18 */   long lineTime = 0L;
/*     */   
/*     */   boolean passwordField = false;
/*     */   boolean mouseDown = false;
/*     */   boolean mouseDragging = false;
/*     */   boolean focused = false;
/*     */   boolean tab = true;
/*     */   TextPattern pattern;
/*     */   
/*     */   public MenuTextField(TextPattern pattern, int x, int y, int width, int height) {
/*  28 */     super(x, y, width, height);
/*  29 */     this.pattern = pattern;
/*  30 */     this.text = "";
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  35 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  36 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  37 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  38 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  39 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  41 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  42 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  43 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
/*  44 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
/*  45 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  47 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  48 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  49 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  50 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  51 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onExitGui(int button) {
/*  56 */     this.focused = false;
/*  57 */     onFocusLose();
/*  58 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  63 */     if (button == 0) {
/*  64 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  69 */     if (this.disabled) {
/*  70 */       return true;
/*     */     }
/*  72 */     if (this.focused) {
/*  73 */       return false;
/*     */     }
/*  75 */     if (this.mouseDown) {
/*  76 */       int x = getRenderX();
/*  77 */       int y = getRenderY();
/*  78 */       int mouseX = this.parent.getMouseX();
/*  79 */       int mouseY = this.parent.getMouseY();
/*  80 */       if (mouseX >= x && mouseX <= x + this.width + this.minOffset * 2 && 
/*  81 */         mouseY >= y && mouseY <= y + this.height) {
/*  82 */         return false;
/*     */       }
/*     */     } 
/*     */     
/*  86 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKeyDown(char character, int key) {
/*  91 */     if (this.focused) {
/*  92 */       int oldIndex = this.index;
/*  93 */       boolean found = true;
/*  94 */       boolean wantRender = false;
/*  95 */       switch (key) {
/*     */         case 28:
/*  97 */           onEnter();
/*  98 */           this.focused = false;
/*  99 */           onFocusLose();
/*     */           break;
/*     */         
/*     */         case 15:
/* 103 */           if (this.tab)
/*     */             break; 
/* 105 */           onTab();
/* 106 */           this.focused = false;
/* 107 */           onFocusLose();
/*     */           break;
/*     */         
/*     */         case 203:
/* 111 */           if (this.index - 1 >= 0)
/* 112 */             this.index--; 
/* 113 */           wantRender = true;
/*     */           break;
/*     */         
/*     */         case 205:
/* 117 */           if (this.index + 1 <= this.text.length())
/* 118 */             this.index++; 
/* 119 */           wantRender = true;
/*     */           break;
/*     */         
/*     */         case 14:
/*     */         case 211:
/* 124 */           if (!this.text.isEmpty() && this.index - 1 >= 0) {
/* 125 */             this.text = (new StringBuilder(this.text)).deleteCharAt(this.index - 1).toString();
/* 126 */             onAction();
/*     */           } 
/*     */           
/* 129 */           if (this.index - 1 >= 0)
/* 130 */             this.index--; 
/* 131 */           wantRender = true;
/*     */           break;
/*     */         
/*     */         default:
/* 135 */           found = false;
/*     */           break;
/*     */       } 
/*     */       
/* 139 */       if (wantRender && 
/* 140 */         oldIndex != this.index) {
/* 141 */         this.lineTime = getLinePrediction();
/*     */       }
/* 143 */       if (found) {
/*     */         return;
/*     */       }
/* 146 */       if (this.pattern != TextPattern.NONE) {
/* 147 */         if (this.pattern == TextPattern.NUMBERS_ONLY && !Character.isDigit(character) && (this.text.length() != 0 || character != '-')) {
/*     */           return;
/*     */         }
/* 150 */         if (this.pattern == TextPattern.TEXT_ONLY && !Character.isAlphabetic(character)) {
/*     */           return;
/*     */         }
/* 153 */         if (this.pattern == TextPattern.TEXT_AND_NUMBERS && !Character.isAlphabetic(character) && !Character.isDigit(character))
/*     */           return; 
/*     */       } 
/* 156 */       if ((character + "").matches("[A-Za-z0-9\\s_\\+\\-\\.,!@ï¿½#\\$%\\^&\\*\\(\\);\\\\/\\|<>\"'\\[\\]\\?=]")) {
/*     */         try {
/* 158 */           if (this.pattern == TextPattern.NUMBERS_ONLY && character != '-') {
/* 159 */             Integer.valueOf((new StringBuilder(this.text)).insert(this.index, character).toString());
/*     */           }
/* 161 */           this.lineTime = getLinePrediction();
/* 162 */           this.text = (new StringBuilder(this.text)).insert(this.index, character).toString();
/* 163 */           this.index++;
/* 164 */           onAction();
/* 165 */         } catch (NumberFormatException numberFormatException) {}
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRender() {
/* 173 */     setPriority(this.focused ? MenuPriority.HIGH : MenuPriority.MEDIUM);
/* 174 */     int x = getRenderX();
/* 175 */     int y = getRenderY();
/* 176 */     int width = this.width + this.minOffset * 2;
/* 177 */     int height = this.height;
/* 178 */     int mouseX = this.parent.getMouseX();
/* 179 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 181 */     ButtonState state = ButtonState.NORMAL;
/* 182 */     if (this.mouseDown) {
/* 183 */       this.focused = false;
/*     */     }
/* 185 */     if (!this.disabled) {
/* 186 */       if (mouseX >= x && mouseX <= x + width && 
/* 187 */         mouseY >= y && mouseY <= y + height) {
/* 188 */         state = ButtonState.HOVER;
/*     */         
/* 190 */         if (this.mouseDown) {
/* 191 */           this.focused = true;
/* 192 */           this.lineTime = System.currentTimeMillis();
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 197 */       state = ButtonState.DISABLED;
/*     */     } 
/*     */     
/* 200 */     if (this.tab && 
/* 201 */       !Keyboard.isKeyDown(15)) {
/* 202 */       this.tab = false;
/*     */     }
/*     */     
/* 205 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 206 */     int lineColor = getColor(DrawType.LINE, state);
/* 207 */     int textColor = getColor(DrawType.TEXT, state);
/*     */     
/* 209 */     drawRect(x + 1, y + 1, width - 1, height - 1, backgroundColor);
/* 210 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 211 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 212 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 213 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/*     */     
/* 215 */     String textToDraw = this.text;
/* 216 */     if (isPasswordField()) {
/* 217 */       String newText = "";
/* 218 */       for (int i = 0; i < textToDraw.length(); i++) {
/* 219 */         newText = newText + "*";
/*     */       }
/* 221 */       textToDraw = newText;
/*     */     } 
/* 223 */     String add = "";
/* 224 */     if (this.focused) {
/*     */ 
/*     */ 
/*     */       
/* 228 */       add = "|";
/*     */       
/* 230 */       if ((System.currentTimeMillis() - this.lineTime) % this.lineRefreshTime * 2L >= this.lineRefreshTime) {
/* 231 */         textToDraw = (new StringBuilder(textToDraw)).insert(this.index, add).toString();
/* 232 */         add = "";
/*     */       } 
/*     */     } 
/*     */     
/* 236 */     int labelWidth = getStringWidth(textToDraw + add);
/* 237 */     int comp = 0;
/* 238 */     while (labelWidth >= width) {
/* 239 */       if (comp < this.index) {
/* 240 */         textToDraw = textToDraw.substring(1);
/* 241 */         labelWidth = getStringWidth(textToDraw + add);
/* 242 */       } else if (comp > this.index) {
/* 243 */         textToDraw = textToDraw.substring(0, textToDraw.length() - 1);
/* 244 */         labelWidth = getStringWidth(textToDraw + add);
/*     */       } 
/* 246 */       comp++;
/*     */     } 
/* 248 */     int renderIndex = comp;
/* 249 */     int renderStopIndex = comp + textToDraw.length();
/* 250 */     if (add == "") {
/* 251 */       renderStopIndex--;
/*     */     }
/* 253 */     while (this.index > this.text.length()) {
/* 254 */       this.index--;
/*     */     }
/* 256 */     drawText(textToDraw, x + this.minOffset, y + height / 2 - getStringHeight(textToDraw) / 2, textColor);
/*     */     
/* 258 */     if (state == ButtonState.HOVER && this.mouseDown) {
/* 259 */       this.focused = true;
/* 260 */       onFocusGain();
/* 261 */       this.lineTime = getLinePrediction();
/*     */       
/* 263 */       int position = x;
/* 264 */       if (mouseX < position) {
/* 265 */         this.index = 0;
/*     */         
/*     */         return;
/*     */       } 
/* 269 */       float bestDiff = 1000.0F;
/* 270 */       int bestIndex = -1;
/* 271 */       for (int i = renderIndex; i < renderStopIndex; i++) {
/* 272 */         if (this.text.length() > i) {
/*     */ 
/*     */           
/* 275 */           int diff = Math.abs(mouseX - position);
/* 276 */           if (bestDiff > diff) {
/* 277 */             bestDiff = diff;
/* 278 */             bestIndex = i;
/*     */           } 
/* 280 */           position += getStringWidth(this.text.charAt(i) + "");
/*     */         } 
/*     */       } 
/* 283 */       if (mouseX > position) {
/* 284 */         this.index = this.text.length();
/* 285 */       } else if (bestIndex != -1) {
/* 286 */         this.index = bestIndex;
/*     */       } else {
/* 288 */         this.index = 0;
/*     */       } 
/* 290 */     }  this.mouseDown = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onFocusGain() {}
/*     */ 
/*     */   
/*     */   public void onFocusLose() {}
/*     */ 
/*     */   
/*     */   private long getLinePrediction() {
/* 302 */     return System.currentTimeMillis() - (this.lineRefreshTime / 2);
/*     */   }
/*     */   
/*     */   public int getIntValue() {
/* 306 */     if (this.pattern == TextPattern.NUMBERS_ONLY && this.text.equals("-")) {
/* 307 */       return 0;
/*     */     }
/*     */     try {
/* 310 */       return Integer.valueOf(this.text).intValue();
/* 311 */     } catch (NumberFormatException e) {
/* 312 */       return 0;
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getText() {
/* 317 */     if (this.pattern == TextPattern.NUMBERS_ONLY && this.text.equals("-"))
/* 318 */       return "-0"; 
/* 319 */     return this.text;
/*     */   }
/*     */   
/*     */   public void setText(String text) {
/* 323 */     this.text = text;
/*     */   }
/*     */   
/*     */   public void setCursor(int i) {
/* 327 */     if (i >= 0)
/* 328 */       this.index = i; 
/*     */   }
/*     */   
/*     */   public boolean isPasswordField() {
/* 332 */     return this.passwordField;
/*     */   }
/*     */   
/*     */   public void setPasswordField(boolean passwordField) {
/* 336 */     this.passwordField = passwordField;
/*     */   }
/*     */   
/*     */   public boolean isFocused() {
/* 340 */     return this.focused;
/*     */   }
/*     */   
/*     */   public void setFocused(boolean focused, boolean tab) {
/* 344 */     if (focused) {
/* 345 */       this.lineTime = System.currentTimeMillis();
/* 346 */       onFocusGain();
/*     */     } else {
/* 348 */       onFocusLose();
/*     */     } 
/*     */     
/* 351 */     if (tab) {
/* 352 */       this.tab = true;
/*     */     }
/* 354 */     this.focused = focused;
/*     */   }
/*     */   
/*     */   public void onTab() {}
/*     */   
/*     */   public void onEnter() {}
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuTextField.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */