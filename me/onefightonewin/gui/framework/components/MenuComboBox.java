/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.ArrayList;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MenuPriority;
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
/*     */ public class MenuComboBox
/*     */   extends MenuComponent
/*     */ {
/*     */   String[] values;
/*     */   String lastValueString;
/*     */   boolean[] items;
/*     */   boolean open;
/*     */   boolean mouseDown;
/*     */   boolean mouseDragging;
/*     */   int textOffset;
/*     */   int totalHeight;
/*     */   int maxHeight;
/*     */   int maxWidth;
/*     */   int arrowOffset;
/*     */   
/*     */   public MenuComboBox(Class<?> theEnum, int x, int y) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iload_2
/*     */     //   2: iload_3
/*     */     //   3: iconst_0
/*     */     //   4: iconst_0
/*     */     //   5: invokespecial <init> : (IIII)V
/*     */     //   8: aload_0
/*     */     //   9: ldc ''
/*     */     //   11: putfield lastValueString : Ljava/lang/String;
/*     */     //   14: aload_0
/*     */     //   15: iconst_0
/*     */     //   16: putfield open : Z
/*     */     //   19: aload_0
/*     */     //   20: iconst_0
/*     */     //   21: putfield mouseDown : Z
/*     */     //   24: aload_0
/*     */     //   25: iconst_0
/*     */     //   26: putfield mouseDragging : Z
/*     */     //   29: aload_0
/*     */     //   30: bipush #25
/*     */     //   32: putfield textOffset : I
/*     */     //   35: aload_0
/*     */     //   36: iconst_0
/*     */     //   37: putfield totalHeight : I
/*     */     //   40: aload_0
/*     */     //   41: iconst_0
/*     */     //   42: putfield maxHeight : I
/*     */     //   45: aload_0
/*     */     //   46: iconst_0
/*     */     //   47: putfield maxWidth : I
/*     */     //   50: aload_0
/*     */     //   51: bipush #15
/*     */     //   53: putfield arrowOffset : I
/*     */     //   56: aload_1
/*     */     //   57: invokevirtual isEnum : ()Z
/*     */     //   60: ifeq -> 197
/*     */     //   63: new java/util/ArrayList
/*     */     //   66: dup
/*     */     //   67: invokespecial <init> : ()V
/*     */     //   70: astore #4
/*     */     //   72: aload_1
/*     */     //   73: invokevirtual getEnumConstants : ()[Ljava/lang/Object;
/*     */     //   76: astore #5
/*     */     //   78: aload #5
/*     */     //   80: arraylength
/*     */     //   81: istore #6
/*     */     //   83: iconst_0
/*     */     //   84: istore #7
/*     */     //   86: iload #7
/*     */     //   88: iload #6
/*     */     //   90: if_icmpge -> 166
/*     */     //   93: aload #5
/*     */     //   95: iload #7
/*     */     //   97: aaload
/*     */     //   98: astore #8
/*     */     //   100: aload #8
/*     */     //   102: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   105: astore #9
/*     */     //   107: aload #4
/*     */     //   109: new java/lang/StringBuilder
/*     */     //   112: dup
/*     */     //   113: invokespecial <init> : ()V
/*     */     //   116: aload #9
/*     */     //   118: invokevirtual toUpperCase : ()Ljava/lang/String;
/*     */     //   121: iconst_0
/*     */     //   122: iconst_1
/*     */     //   123: invokevirtual substring : (II)Ljava/lang/String;
/*     */     //   126: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   129: aload #9
/*     */     //   131: invokevirtual toLowerCase : ()Ljava/lang/String;
/*     */     //   134: iconst_1
/*     */     //   135: aload #9
/*     */     //   137: invokevirtual length : ()I
/*     */     //   140: invokevirtual substring : (II)Ljava/lang/String;
/*     */     //   143: invokevirtual append : (Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   146: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   149: ldc '_'
/*     */     //   151: ldc ' '
/*     */     //   153: invokevirtual replaceAll : (Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
/*     */     //   156: invokevirtual add : (Ljava/lang/Object;)Z
/*     */     //   159: pop
/*     */     //   160: iinc #7, 1
/*     */     //   163: goto -> 86
/*     */     //   166: aload_0
/*     */     //   167: aload #4
/*     */     //   169: aload #4
/*     */     //   171: invokevirtual size : ()I
/*     */     //   174: anewarray java/lang/String
/*     */     //   177: invokevirtual toArray : ([Ljava/lang/Object;)[Ljava/lang/Object;
/*     */     //   180: checkcast [Ljava/lang/String;
/*     */     //   183: putfield values : [Ljava/lang/String;
/*     */     //   186: aload_0
/*     */     //   187: aload_0
/*     */     //   188: getfield values : [Ljava/lang/String;
/*     */     //   191: arraylength
/*     */     //   192: newarray boolean
/*     */     //   194: putfield items : [Z
/*     */     //   197: aload_0
/*     */     //   198: getfield values : [Ljava/lang/String;
/*     */     //   201: arraylength
/*     */     //   202: ifne -> 210
/*     */     //   205: aload_0
/*     */     //   206: iconst_1
/*     */     //   207: putfield disabled : Z
/*     */     //   210: aload_0
/*     */     //   211: getfield disabled : Z
/*     */     //   214: ifne -> 308
/*     */     //   217: aload_0
/*     */     //   218: getfield values : [Ljava/lang/String;
/*     */     //   221: astore #4
/*     */     //   223: aload #4
/*     */     //   225: arraylength
/*     */     //   226: istore #5
/*     */     //   228: iconst_0
/*     */     //   229: istore #6
/*     */     //   231: iload #6
/*     */     //   233: iload #5
/*     */     //   235: if_icmpge -> 308
/*     */     //   238: aload #4
/*     */     //   240: iload #6
/*     */     //   242: aaload
/*     */     //   243: astore #7
/*     */     //   245: aload_0
/*     */     //   246: aload #7
/*     */     //   248: invokevirtual getStringWidth : (Ljava/lang/String;)I
/*     */     //   251: istore #8
/*     */     //   253: aload_0
/*     */     //   254: aload #7
/*     */     //   256: invokevirtual getStringHeight : (Ljava/lang/String;)I
/*     */     //   259: istore #9
/*     */     //   261: aload_0
/*     */     //   262: dup
/*     */     //   263: getfield totalHeight : I
/*     */     //   266: iload #9
/*     */     //   268: iadd
/*     */     //   269: putfield totalHeight : I
/*     */     //   272: aload_0
/*     */     //   273: getfield maxHeight : I
/*     */     //   276: iload #9
/*     */     //   278: if_icmpge -> 287
/*     */     //   281: aload_0
/*     */     //   282: iload #9
/*     */     //   284: putfield maxHeight : I
/*     */     //   287: aload_0
/*     */     //   288: getfield maxWidth : I
/*     */     //   291: iload #8
/*     */     //   293: if_icmpge -> 302
/*     */     //   296: aload_0
/*     */     //   297: iload #8
/*     */     //   299: putfield maxWidth : I
/*     */     //   302: iinc #6, 1
/*     */     //   305: goto -> 231
/*     */     //   308: aload_0
/*     */     //   309: aload_0
/*     */     //   310: getfield maxWidth : I
/*     */     //   313: putfield width : I
/*     */     //   316: aload_0
/*     */     //   317: aload_0
/*     */     //   318: getfield maxHeight : I
/*     */     //   321: iconst_1
/*     */     //   322: iadd
/*     */     //   323: putfield height : I
/*     */     //   326: return
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #25	-> 0
/*     */     //   #13	-> 8
/*     */     //   #15	-> 14
/*     */     //   #16	-> 19
/*     */     //   #17	-> 24
/*     */     //   #18	-> 29
/*     */     //   #19	-> 35
/*     */     //   #20	-> 40
/*     */     //   #21	-> 45
/*     */     //   #22	-> 50
/*     */     //   #26	-> 56
/*     */     //   #27	-> 63
/*     */     //   #28	-> 72
/*     */     //   #29	-> 100
/*     */     //   #30	-> 107
/*     */     //   #28	-> 160
/*     */     //   #32	-> 166
/*     */     //   #33	-> 186
/*     */     //   #36	-> 197
/*     */     //   #37	-> 205
/*     */     //   #39	-> 210
/*     */     //   #40	-> 217
/*     */     //   #41	-> 245
/*     */     //   #42	-> 253
/*     */     //   #44	-> 261
/*     */     //   #46	-> 272
/*     */     //   #47	-> 281
/*     */     //   #48	-> 287
/*     */     //   #49	-> 296
/*     */     //   #40	-> 302
/*     */     //   #52	-> 308
/*     */     //   #53	-> 316
/*     */     //   #54	-> 326
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   107	53	9	string	Ljava/lang/String;
/*     */     //   100	60	8	object	Ljava/lang/Object;
/*     */     //   72	125	4	tempVals	Ljava/util/ArrayList;
/*     */     //   253	49	8	tWidth	I
/*     */     //   261	41	9	tHeight	I
/*     */     //   245	57	7	value	Ljava/lang/String;
/*     */     //   0	327	0	this	Lme/onefightonewin/gui/framework/components/MenuComboBox;
/*     */     //   0	327	1	theEnum	Ljava/lang/Class;
/*     */     //   0	327	2	x	I
/*     */     //   0	327	3	y	I
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   72	125	4	tempVals	Ljava/util/ArrayList<Ljava/lang/String;>;
/*     */     //   0	327	1	theEnum	Ljava/lang/Class<*>;
/*     */   }
/*     */   
/*     */   public void onInitColors() {
/*  58 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  59 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  60 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  61 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  62 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  64 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  65 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  66 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
/*  67 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
/*  68 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(10, 10, 10, 255));
/*  69 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  71 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  72 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(100, 120, 255, 255));
/*  73 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  74 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(110, 130, 255, 255));
/*  75 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onExitGui(int key) {
/*  80 */     if (this.open)
/*  81 */       this.open = false; 
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  87 */     if (button == 0) {
/*  88 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouseClickMove(int button) {
/*  93 */     if (button == 0) {
/*  94 */       this.mouseDragging = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  99 */     if (this.disabled) {
/* 100 */       return true;
/*     */     }
/* 102 */     if (this.mouseDown) {
/* 103 */       int x = getRenderX();
/* 104 */       int y = getRenderY();
/* 105 */       int width = this.width + this.textOffset;
/* 106 */       int height = this.height;
/* 107 */       int mouseX = this.parent.getMouseX();
/* 108 */       int mouseY = this.parent.getMouseY();
/* 109 */       if (mouseX >= x && mouseX <= x + width + this.arrowOffset - 1 && 
/* 110 */         mouseY >= y && mouseY <= y + height) {
/* 111 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 115 */     return !this.open;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/* 120 */     setPriority(this.open ? MenuPriority.HIGHEST : MenuPriority.HIGH);
/*     */     
/* 122 */     int x = getRenderX();
/* 123 */     int y = getRenderY();
/* 124 */     int width = this.width + this.textOffset;
/* 125 */     int height = this.height;
/* 126 */     int mouseX = this.parent.getMouseX();
/* 127 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 129 */     ButtonState state = this.open ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*     */     
/* 131 */     if (!this.disabled) {
/* 132 */       if (mouseX >= x && mouseX <= x + width + this.arrowOffset - 1 && 
/* 133 */         mouseY >= y && mouseY <= y + height + 1) {
/* 134 */         state = this.open ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
/* 135 */         if (this.mouseDown) {
/* 136 */           this.open = true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 140 */       state = ButtonState.DISABLED;
/*     */     } 
/* 142 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 143 */     int lineColor = getColor(DrawType.LINE, state);
/* 144 */     int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
/*     */     
/* 146 */     drawRect(x + 1, y + 1, width - 2 + this.arrowOffset, height - 1, backgroundColor);
/* 147 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 148 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 149 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 150 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 151 */     drawHorizontalLine(x + width, y, this.arrowOffset, 1, lineColor);
/* 152 */     drawHorizontalLine(x + width, y + height, this.arrowOffset, 1, lineColor);
/* 153 */     drawVerticalLine(x + width + this.arrowOffset - 1, y + 1, height - 1, 1, lineColor);
/* 154 */     int arrowX = x + width - 1 + this.arrowOffset / 2;
/* 155 */     int arrowY = y + (height - 1) / 2;
/* 156 */     if (this.open) {
/* 157 */       drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
/* 158 */       drawHorizontalLine(arrowX - 2, arrowY + 2, 7, 1, textColor);
/* 159 */       drawHorizontalLine(arrowX - 3, arrowY + 3, 9, 1, textColor);
/* 160 */       drawHorizontalLine(arrowX, arrowY, 3, 1, textColor);
/* 161 */       drawPixel(arrowX + 1, arrowY - 1, textColor);
/*     */     } else {
/* 163 */       drawHorizontalLine(arrowX - 3, arrowY - 1, 9, 1, textColor);
/* 164 */       drawHorizontalLine(arrowX - 2, arrowY, 7, 1, textColor);
/* 165 */       drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
/* 166 */       drawHorizontalLine(arrowX, arrowY + 2, 3, 1, textColor);
/* 167 */       drawPixel(arrowX + 1, arrowY + 3, textColor);
/*     */     } 
/* 169 */     drawText(this.lastValueString, x + 1 + width / 2 - getStringWidth(this.lastValueString) / 2, y + 1, textColor);
/* 170 */     drawDropdown(state);
/* 171 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public void drawDropdown(ButtonState state) {
/* 175 */     if (this.open) {
/* 176 */       int x = getRenderX();
/* 177 */       int y = getRenderY();
/* 178 */       int mouseX = this.parent.getMouseX();
/* 179 */       int mouseY = this.parent.getMouseY();
/* 180 */       int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
/* 181 */       int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
/* 182 */       int width = this.width + this.textOffset + this.arrowOffset - 1;
/* 183 */       int height = this.totalHeight + this.values.length;
/* 184 */       drawRect(x, y + this.height + 1, width, height, backgroundColor);
/* 185 */       drawHorizontalLine(x, y + this.height + 1, width + 1, 1, lineColor);
/* 186 */       drawVerticalLine(x, y + this.height + 2, height - 1, 1, lineColor);
/* 187 */       drawHorizontalLine(x, y + this.height + 1 + height, width + 1, 1, lineColor);
/* 188 */       drawVerticalLine(x + width, y + this.height + 2, height - 1, 1, lineColor);
/* 189 */       y += 2;
/* 190 */       boolean inHover = false;
/* 191 */       for (int i = 0; i < this.values.length; i++) {
/* 192 */         String value = this.values[i];
/* 193 */         int sHeight = getStringHeight(value);
/* 194 */         y += sHeight + 1;
/*     */         
/* 196 */         boolean hover = false;
/* 197 */         if (mouseX >= x + 1 && mouseX <= x + width - 1 && 
/* 198 */           mouseY >= y && mouseY <= y + sHeight) {
/* 199 */           hover = true;
/* 200 */           drawRect(x + 1, y, width - 1, sHeight, getColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE));
/* 201 */           if (this.mouseDown) {
/* 202 */             this.items[i] = !this.items[i];
/* 203 */             String label = getValuesFormatted();
/* 204 */             int labelWidth = getStringWidth(label);
/* 205 */             while (labelWidth >= width - this.arrowOffset) {
/* 206 */               label = label.substring(0, label.length() - 1);
/* 207 */               labelWidth = getStringWidth(label);
/*     */             } 
/* 209 */             this.lastValueString = label;
/* 210 */             onAction();
/*     */           } 
/* 212 */           inHover = true;
/*     */         } 
/*     */         
/* 215 */         drawText(value, x + 1 + width / 2 - getStringWidth(value) / 2, y, getColor(DrawType.TEXT, this.items[i] ? (hover ? ButtonState.HOVERACTIVE : ButtonState.ACTIVE) : (hover ? ButtonState.HOVER : ButtonState.NORMAL)));
/*     */       } 
/* 217 */       if (this.open && !inHover && this.mouseDown && state != ButtonState.HOVER && state != ButtonState.HOVERACTIVE)
/* 218 */         this.open = false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getValuesFormatted() {
/* 223 */     String[] values = getValues();
/* 224 */     String string = "";
/* 225 */     for (String value : values) {
/* 226 */       if (string.length() > 0)
/* 227 */         string = string + ", "; 
/* 228 */       string = string + value.replaceAll("_", " ");
/*     */     } 
/* 230 */     return string;
/*     */   }
/*     */   
/*     */   public String[] getValues() {
/* 234 */     ArrayList<String> values = new ArrayList<>();
/* 235 */     for (int i = 0; i < this.values.length; i++) {
/* 236 */       if (this.items[i]) {
/* 237 */         values.add(this.values[i].replaceAll(" ", "_"));
/*     */       }
/*     */     } 
/* 240 */     return values.<String>toArray(new String[values.size()]);
/*     */   }
/*     */   
/*     */   public void setValues(String[] values) {
/* 244 */     if (values == null) {
/*     */       return;
/*     */     }
/* 247 */     for (int i = 0; i < this.values.length; i++) {
/* 248 */       for (int ii = 0; ii < values.length; ii++) {
/* 249 */         if (values[ii].equalsIgnoreCase(this.values[i].replaceAll(" ", "_"))) {
/* 250 */           this.items[i] = true; break;
/*     */         } 
/* 252 */         if (this.items[i]) {
/* 253 */           this.items[i] = false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 258 */     String label = getValuesFormatted();
/* 259 */     int labelWidth = getStringWidth(label);
/* 260 */     while (labelWidth >= this.width + this.textOffset) {
/* 261 */       label = label.substring(0, label.length() - 1);
/* 262 */       labelWidth = getStringWidth(label);
/*     */     } 
/* 264 */     this.lastValueString = label;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuComboBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */