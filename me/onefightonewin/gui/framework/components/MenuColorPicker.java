/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Point;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MenuPriority;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ 
/*     */ 
/*     */ public class MenuColorPicker
/*     */   extends MenuComponent
/*     */ {
/*     */   Color color;
/*     */   Color temp;
/*     */   Point startPos;
/*     */   ButtonState lastState;
/*     */   boolean mouseDown = false;
/*     */   boolean mouseDragging = false;
/*     */   boolean pickingColor = false;
/*     */   boolean canPick = true;
/*  29 */   int size = 80;
/*  30 */   int pickerOffset = 2;
/*  31 */   int colorOffset = 10;
/*  32 */   int alphaOffset = 10;
/*  33 */   int pickerWindowWidth = this.size + this.colorOffset;
/*  34 */   int pickerWindowHeight = this.size + this.alphaOffset;
/*     */   MenuSlider alphaSlider;
/*     */   
/*     */   public MenuColorPicker(int x, int y, int width, int height, int defaultColor) {
/*  38 */     super(x, y, width, height);
/*  39 */     Color theColor = new Color(defaultColor, true);
/*  40 */     this.color = theColor;
/*  41 */     this.temp = theColor;
/*  42 */     this.alphaSlider = new MenuSlider(1.0F, 0.0F, 1.0F, 1, 0, 0, this.pickerWindowWidth, 10)
/*     */       {
/*     */         public void onAction() {
/*  45 */           MenuColorPicker.this.color = new Color(MenuColorPicker.this.color.getRed(), MenuColorPicker.this.color.getGreen(), MenuColorPicker.this.color.getBlue(), Math.round(MenuColorPicker.this.alphaSlider.getValue() * 255.0F));
/*  46 */           MenuColorPicker.this.onAction();
/*     */         }
/*     */       };
/*  49 */     this.alphaSlider.setValue(theColor.getAlpha() / 255.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInitColors() {
/*  54 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  55 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(81, 108, 255, 255));
/*  56 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  57 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(100, 120, 255, 255));
/*  58 */     setColor(DrawType.BACKGROUND, ButtonState.POPUP, new Color(10, 10, 10, 255));
/*  59 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  61 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  62 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  63 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(10, 10, 10, 255));
/*  64 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(10, 10, 10, 255));
/*  65 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(100, 120, 255, 255));
/*  66 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  68 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  69 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/*  70 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  71 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/*  72 */     setColor(DrawType.TEXT, ButtonState.POPUP, new Color(100, 100, 100, 255));
/*  73 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  78 */     if (button == 0 && this.alphaSlider.passesThrough()) {
/*  79 */       this.mouseDown = true;
/*     */     }
/*  81 */     int x = getRenderX();
/*  82 */     int y = getRenderY();
/*     */     
/*  84 */     if (this.parent.getMouseX() >= x && this.parent.getMouseX() <= x + this.width && 
/*  85 */       this.parent.getMouseY() >= y && this.parent.getMouseY() <= y + this.height + 1 && 
/*  86 */       !this.pickingColor) {
/*  87 */       if (button == 1) {
/*     */         try {
/*  89 */           int data = Integer.valueOf((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)).intValue();
/*  90 */           Color color = new Color(data, true);
/*     */           
/*  92 */           if (color != null) {
/*  93 */             this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(this.alphaSlider.getValue() * 255.0F));
/*  94 */             onAction();
/*     */           } 
/*  96 */         } catch (HeadlessException|java.awt.datatransfer.UnsupportedFlavorException|java.io.IOException|NumberFormatException e) {
/*  97 */           e.printStackTrace();
/*     */         }
/*     */       
/* 100 */       } else if (button == 2) {
/* 101 */         StringSelection stringSelection = new StringSelection(getColor().getRGB() + "");
/* 102 */         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 103 */         clipboard.setContents(stringSelection, null);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 109 */     this.alphaSlider.onMouseClick(button);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClickMove(int button) {
/* 114 */     if (button == 0 && this.alphaSlider.passesThrough()) {
/* 115 */       this.mouseDragging = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean onExitGui(int key) {
/* 120 */     if (this.pickingColor) {
/* 121 */       this.pickingColor = false;
/*     */     }
/* 123 */     this.alphaSlider.onExitGui(key);
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean passesThrough() {
/* 129 */     if (this.pickingColor) {
/* 130 */       return false;
/*     */     }
/* 132 */     if (this.disabled) {
/* 133 */       return true;
/*     */     }
/* 135 */     int x = getRenderX();
/* 136 */     int y = getRenderY();
/* 137 */     int mouseX = this.parent.getMouseX();
/* 138 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 140 */     if (this.startPos != null) {
/* 141 */       if (mouseX >= this.startPos.x && mouseX <= this.startPos.x + this.pickerWindowWidth && 
/* 142 */         mouseY >= this.startPos.y && mouseY <= this.startPos.y + this.pickerWindowHeight) {
/* 143 */         return false;
/*     */       }
/*     */     }
/* 146 */     else if (this.mouseDown && 
/* 147 */       mouseX >= x && mouseX <= x + this.width && 
/* 148 */       mouseY >= y && mouseY <= y + this.height + 1) {
/* 149 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 153 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/* 158 */     if (this.alphaSlider.getParent() == null && getParent() != null) {
/* 159 */       this.alphaSlider.setParent(getParent());
/*     */     }
/* 161 */     setPriority(this.pickingColor ? MenuPriority.HIGHEST : MenuPriority.HIGH);
/*     */     
/* 163 */     int x = getRenderX();
/* 164 */     int y = getRenderY();
/* 165 */     int width = this.width;
/* 166 */     int height = this.height;
/* 167 */     int mouseX = this.parent.getMouseX();
/* 168 */     int mouseY = this.parent.getMouseY();
/* 169 */     ButtonState state = ButtonState.NORMAL;
/* 170 */     if (!this.disabled)
/* 171 */     { if (mouseX >= x && mouseX <= x + width && 
/* 172 */         mouseY >= y && mouseY <= y + height + 1) {
/* 173 */         state = ButtonState.HOVER;
/*     */       }
/*     */ 
/*     */       
/* 177 */       if (this.startPos != null) {
/* 178 */         boolean hover = false;
/* 179 */         if (mouseX >= this.startPos.x && mouseX <= this.startPos.x + this.pickerWindowWidth && 
/* 180 */           mouseY >= this.startPos.y && mouseY <= this.startPos.y + this.pickerWindowHeight + 1) {
/* 181 */           hover = true;
/*     */         }
/*     */         
/* 184 */         this.pickingColor = ((this.mouseDown && hover) || (!this.mouseDown && this.pickingColor));
/* 185 */         if (this.pickingColor)
/* 186 */           state = ButtonState.HOVER; 
/* 187 */       } else if (state == ButtonState.HOVER && this.mouseDown) {
/* 188 */         this.pickingColor = true;
/*     */       }  }
/* 190 */     else { state = ButtonState.DISABLED; }
/*     */ 
/*     */     
/* 193 */     int lineColor = getColor(DrawType.LINE, state);
/*     */     
/* 195 */     int index = 0;
/* 196 */     for (int h = y; h < y + height; h++) {
/* 197 */       drawRect(x + 1, h, width - 1, 1, this.disabled ? lightenColor(index, 7, this.color).getRGB() : darkenColor(index, 7, this.color).getRGB());
/* 198 */       index++;
/*     */     } 
/*     */     
/* 201 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 202 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 203 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 204 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 205 */     this.lastState = state;
/* 206 */     drawPicker();
/*     */     
/* 208 */     this.mouseDown = false;
/* 209 */     this.mouseDragging = false;
/*     */   }
/*     */   
/*     */   public void drawPicker() {
/* 213 */     if (this.pickingColor) {
/* 214 */       int x = getRenderX();
/* 215 */       int y = getRenderY();
/* 216 */       int mouseX = this.parent.getMouseX();
/* 217 */       int mouseY = this.parent.getMouseY();
/* 218 */       int backgroundColor = getColor(DrawType.BACKGROUND, this.lastState);
/* 219 */       int lineColor = getColor(DrawType.LINE, this.lastState);
/*     */       
/* 221 */       if (!this.canPick && !this.mouseDown) {
/* 222 */         this.canPick = true;
/*     */       }
/* 224 */       backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
/* 225 */       lineColor = getColor(DrawType.LINE, ButtonState.HOVER);
/* 226 */       if (this.startPos == null) {
/* 227 */         ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
/* 228 */         int windowX = mouseX;
/* 229 */         int windowY = mouseY;
/*     */         
/* 231 */         if (windowX + this.pickerWindowWidth >= res.getScaledWidth()) {
/* 232 */           windowX -= this.pickerWindowWidth;
/*     */         }
/* 234 */         if (windowY + this.pickerWindowHeight >= res.getScaledHeight()) {
/* 235 */           windowY -= this.pickerWindowHeight;
/*     */         }
/* 237 */         this.startPos = new Point(windowX, windowY);
/* 238 */         this.alphaSlider.setX(this.startPos.x);
/* 239 */         this.alphaSlider.setY(this.startPos.y + this.pickerWindowHeight - this.alphaSlider.getHeight());
/*     */         
/* 241 */         this.canPick = false;
/*     */       } 
/*     */       
/* 244 */       drawRect(this.startPos.x + 1, this.startPos.y + 1, this.pickerWindowWidth - 1, this.pickerWindowHeight - 1, backgroundColor);
/* 245 */       drawHorizontalLine(this.startPos.x, this.startPos.y, this.pickerWindowWidth + 1, 1, lineColor);
/* 246 */       drawVerticalLine(this.startPos.x, this.startPos.y + 1, this.pickerWindowHeight - 1, 1, lineColor);
/* 247 */       drawHorizontalLine(this.startPos.x, this.startPos.y + this.pickerWindowHeight, this.pickerWindowWidth + 1, 1, lineColor);
/* 248 */       drawVerticalLine(this.startPos.x + this.pickerWindowWidth, this.startPos.y + 1, this.pickerWindowHeight - 1, 1, lineColor);
/*     */       
/* 250 */       for (y = this.startPos.y + 1; y < this.startPos.y + this.size; y++) {
/* 251 */         float blackMod = 255.0F * (y - this.startPos.y) / this.size;
/*     */         
/* 253 */         for (x = this.startPos.x + 1; x < this.startPos.x + this.size; x++) {
/* 254 */           Color color = new Color(clampColor(this.temp.getRed() - blackMod), clampColor(this.temp.getGreen() - blackMod), clampColor(this.temp.getBlue() - blackMod));
/* 255 */           if ((this.mouseDown || this.mouseDragging) && this.canPick && isInPixel(mouseX, mouseY, x, y)) {
/* 256 */             this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.round(this.alphaSlider.getValue() * 255.0F));
/* 257 */             onAction();
/*     */           } 
/*     */           
/* 260 */           drawPixel(x, y, color.getRGB());
/*     */         } 
/*     */       } 
/*     */       
/* 264 */       float colorSpeed = this.size / 3.8F;
/*     */       
/* 266 */       float red = 255.0F;
/* 267 */       float green = 255.0F;
/* 268 */       float blue = 255.0F;
/*     */       
/* 270 */       for (y = this.startPos.y + 1; y < this.startPos.y + this.size; y++) {
/* 271 */         if (red >= 255.0F && green >= 255.0F && blue > 0.0F) {
/* 272 */           blue -= colorSpeed;
/* 273 */         } else if (red >= 255.0F && green > 0.0F && blue <= 0.0F) {
/* 274 */           green -= colorSpeed;
/* 275 */         } else if (red >= 255.0F && green <= 0.0F && blue < 255.0F) {
/* 276 */           blue += colorSpeed;
/* 277 */         } else if (red > 0.0F && green <= 0.0F && blue >= 255.0F) {
/* 278 */           red -= colorSpeed;
/* 279 */         } else if (red <= 0.0F && green < 255.0F && blue >= 255.0F) {
/* 280 */           green += colorSpeed;
/* 281 */         } else if (red <= 0.0F && green >= 255.0F && blue > 0.0F) {
/* 282 */           blue -= colorSpeed;
/* 283 */         } else if (red < 255.0F && green >= 255.0F && blue <= 0.0F) {
/* 284 */           red += colorSpeed;
/*     */         } 
/* 286 */         for (x = this.startPos.x + this.size + 1; x < this.startPos.x + this.pickerWindowWidth; x++) {
/* 287 */           Color color = new Color(clampColor(red), clampColor(green), clampColor(blue));
/* 288 */           if ((this.mouseDown || this.mouseDragging) && this.canPick && isInPixel(mouseX, mouseY, x, y)) {
/* 289 */             this.temp = color;
/* 290 */             onMiniAction();
/*     */           } 
/* 292 */           drawPixel(x, y, color.getRGB());
/*     */         } 
/*     */       } 
/*     */       
/* 296 */       this.alphaSlider.onRender();
/* 297 */       drawVerticalLine(this.startPos.x + this.size, this.startPos.y + 1, this.pickerWindowHeight - this.alphaOffset - 1, 1, lineColor);
/* 298 */     } else if (this.startPos != null) {
/* 299 */       this.startPos = null;
/*     */     } 
/*     */   }
/*     */   private boolean isInPixel(int mouseX, int mouseY, int x, int y) {
/* 303 */     return (mouseX == x && mouseY == y);
/*     */   }
/*     */   
/*     */   private int clampColor(float color) {
/* 307 */     int theColor = Math.round(color);
/* 308 */     if (theColor > 255)
/* 309 */       return 255; 
/* 310 */     if (theColor < 0)
/* 311 */       return 0; 
/* 312 */     return theColor;
/*     */   }
/*     */   
/*     */   private Color darkenColor(int index, int modifier, Color color) {
/* 316 */     int newRed = color.getRed() - index * modifier;
/* 317 */     int newGreen = color.getGreen() - index * modifier;
/* 318 */     int newBlue = color.getBlue() - index * modifier;
/*     */     
/* 320 */     if (newRed < 0) {
/* 321 */       newRed = 0;
/*     */     }
/* 323 */     if (newGreen < 0) {
/* 324 */       newGreen = 0;
/*     */     }
/* 326 */     if (newBlue < 0) {
/* 327 */       newBlue = 0;
/*     */     }
/* 329 */     return new Color(newRed, newGreen, newBlue, Math.round(this.alphaSlider.getValue() * 255.0F));
/*     */   }
/*     */   
/*     */   private Color lightenColor(int index, int modifier, Color color) {
/* 333 */     int newRed = color.getRed() + index * modifier;
/* 334 */     int newGreen = color.getGreen() + index * modifier;
/* 335 */     int newBlue = color.getBlue() + index * modifier;
/*     */     
/* 337 */     if (newRed > 255) {
/* 338 */       newRed = 255;
/*     */     }
/* 340 */     if (newGreen > 255) {
/* 341 */       newGreen = 255;
/*     */     }
/* 343 */     if (newBlue > 255) {
/* 344 */       newBlue = 255;
/*     */     }
/* 346 */     return new Color(newRed, newGreen, newBlue, color.getAlpha());
/*     */   }
/*     */ 
/*     */   
/*     */   public Color getColor() {
/* 351 */     return this.color;
/*     */   }
/*     */   
/*     */   public void setColor(Color color) {
/* 355 */     this.color = color;
/* 356 */     this.alphaSlider.setValue(color.getAlpha() / 255.0F);
/*     */   }
/*     */   
/*     */   public void setColor(int color) {
/* 360 */     this.color = new Color(color, true);
/* 361 */     this.alphaSlider.setValue(this.color.getAlpha() / 255.0F);
/*     */   }
/*     */   
/*     */   public Color getColorCategory() {
/* 365 */     return this.temp;
/*     */   }
/*     */   
/*     */   public void setColorCategory(int color) {
/* 369 */     this.temp = new Color(color, true);
/*     */   }
/*     */   
/*     */   public MenuSlider getAlphaSlider() {
/* 373 */     return this.alphaSlider;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */   
/*     */   public void onMiniAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuColorPicker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */