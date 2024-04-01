/*     */ package me.onefightonewin.gui.framework.components;
/*     */ 
/*     */ import java.awt.Color;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MenuDropdown
/*     */   extends MenuComponent
/*     */ {
/*     */   String[] values;
/*     */   String lastValueString;
/*     */   String selected;
/*     */   boolean open;
/*     */   boolean mouseDown;
/*     */   boolean mouseDragging;
/*     */   int textOffset;
/*     */   int totalHeight;
/*     */   int maxHeight;
/*     */   int maxWidth;
/*     */   int arrowOffset;
/*     */   
/*     */   public MenuDropdown(Class<?> theEnum, int x, int y) {
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
/*     */     //   60: ifeq -> 192
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
/*     */     //   187: ldc ''
/*     */     //   189: putfield selected : Ljava/lang/String;
/*     */     //   192: aload_0
/*     */     //   193: getfield values : [Ljava/lang/String;
/*     */     //   196: arraylength
/*     */     //   197: ifne -> 205
/*     */     //   200: aload_0
/*     */     //   201: iconst_1
/*     */     //   202: putfield disabled : Z
/*     */     //   205: aload_0
/*     */     //   206: getfield disabled : Z
/*     */     //   209: ifne -> 303
/*     */     //   212: aload_0
/*     */     //   213: getfield values : [Ljava/lang/String;
/*     */     //   216: astore #4
/*     */     //   218: aload #4
/*     */     //   220: arraylength
/*     */     //   221: istore #5
/*     */     //   223: iconst_0
/*     */     //   224: istore #6
/*     */     //   226: iload #6
/*     */     //   228: iload #5
/*     */     //   230: if_icmpge -> 303
/*     */     //   233: aload #4
/*     */     //   235: iload #6
/*     */     //   237: aaload
/*     */     //   238: astore #7
/*     */     //   240: aload_0
/*     */     //   241: aload #7
/*     */     //   243: invokevirtual getStringWidth : (Ljava/lang/String;)I
/*     */     //   246: istore #8
/*     */     //   248: aload_0
/*     */     //   249: aload #7
/*     */     //   251: invokevirtual getStringHeight : (Ljava/lang/String;)I
/*     */     //   254: istore #9
/*     */     //   256: aload_0
/*     */     //   257: dup
/*     */     //   258: getfield totalHeight : I
/*     */     //   261: iload #9
/*     */     //   263: iadd
/*     */     //   264: putfield totalHeight : I
/*     */     //   267: aload_0
/*     */     //   268: getfield maxHeight : I
/*     */     //   271: iload #9
/*     */     //   273: if_icmpge -> 282
/*     */     //   276: aload_0
/*     */     //   277: iload #9
/*     */     //   279: putfield maxHeight : I
/*     */     //   282: aload_0
/*     */     //   283: getfield maxWidth : I
/*     */     //   286: iload #8
/*     */     //   288: if_icmpge -> 297
/*     */     //   291: aload_0
/*     */     //   292: iload #8
/*     */     //   294: putfield maxWidth : I
/*     */     //   297: iinc #6, 1
/*     */     //   300: goto -> 226
/*     */     //   303: aload_0
/*     */     //   304: aload_0
/*     */     //   305: getfield maxWidth : I
/*     */     //   308: putfield width : I
/*     */     //   311: aload_0
/*     */     //   312: aload_0
/*     */     //   313: getfield maxHeight : I
/*     */     //   316: iconst_1
/*     */     //   317: iadd
/*     */     //   318: putfield height : I
/*     */     //   321: return
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
/*     */     //   #29	-> 72
/*     */     //   #30	-> 100
/*     */     //   #31	-> 107
/*     */     //   #29	-> 160
/*     */     //   #34	-> 166
/*     */     //   #35	-> 186
/*     */     //   #38	-> 192
/*     */     //   #39	-> 200
/*     */     //   #41	-> 205
/*     */     //   #42	-> 212
/*     */     //   #43	-> 240
/*     */     //   #44	-> 248
/*     */     //   #46	-> 256
/*     */     //   #48	-> 267
/*     */     //   #49	-> 276
/*     */     //   #50	-> 282
/*     */     //   #51	-> 291
/*     */     //   #42	-> 297
/*     */     //   #54	-> 303
/*     */     //   #55	-> 311
/*     */     //   #56	-> 321
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   107	53	9	string	Ljava/lang/String;
/*     */     //   100	60	8	object	Ljava/lang/Object;
/*     */     //   72	120	4	tempVals	Ljava/util/ArrayList;
/*     */     //   248	49	8	tWidth	I
/*     */     //   256	41	9	tHeight	I
/*     */     //   240	57	7	value	Ljava/lang/String;
/*     */     //   0	322	0	this	Lme/onefightonewin/gui/framework/components/MenuDropdown;
/*     */     //   0	322	1	theEnum	Ljava/lang/Class;
/*     */     //   0	322	2	x	I
/*     */     //   0	322	3	y	I
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   72	120	4	tempVals	Ljava/util/ArrayList<Ljava/lang/String;>;
/*     */     //   0	322	1	theEnum	Ljava/lang/Class<*>;
/*     */   }
/*     */   
/*     */   public void onInitColors() {
/*  60 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(35, 35, 35, 255));
/*  61 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(65, 65, 65, 255));
/*  62 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(50, 50, 50, 255));
/*  63 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(65, 65, 65, 255));
/*  64 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  66 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(10, 10, 10, 255));
/*  67 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(10, 10, 10, 255));
/*  68 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(20, 20, 20, 255));
/*  69 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(20, 20, 20, 255));
/*  70 */     setColor(DrawType.LINE, ButtonState.POPUP, new Color(10, 10, 10, 255));
/*  71 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(100, 100, 100, 255));
/*     */     
/*  73 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(200, 200, 200, 255));
/*  74 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(100, 120, 255, 255));
/*  75 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/*  76 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(110, 130, 255, 255));
/*  77 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onExitGui(int key) {
/*  82 */     if (this.open)
/*  83 */       this.open = false; 
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouseClick(int button) {
/*  89 */     if (button == 0) {
/*  90 */       this.mouseDown = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouseClickMove(int button) {
/*  95 */     if (button == 0) {
/*  96 */       this.mouseDragging = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/* 101 */     if (this.disabled) {
/* 102 */       return true;
/*     */     }
/* 104 */     if (this.mouseDown) {
/* 105 */       int x = getRenderX();
/* 106 */       int y = getRenderY();
/* 107 */       int width = this.width + this.textOffset;
/* 108 */       int height = this.height;
/* 109 */       int mouseX = this.parent.getMouseX();
/* 110 */       int mouseY = this.parent.getMouseY();
/* 111 */       if (mouseX >= x && mouseX <= x + width + this.arrowOffset - 1 && 
/* 112 */         mouseY >= y && mouseY <= y + height) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return !this.open;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRender() {
/* 122 */     setPriority(this.open ? MenuPriority.HIGHEST : MenuPriority.HIGH);
/*     */     
/* 124 */     int x = getRenderX();
/* 125 */     int y = getRenderY();
/* 126 */     int width = this.width + this.textOffset;
/* 127 */     int height = this.height;
/* 128 */     int mouseX = this.parent.getMouseX();
/* 129 */     int mouseY = this.parent.getMouseY();
/*     */     
/* 131 */     ButtonState state = this.open ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*     */     
/* 133 */     if (!this.disabled) {
/* 134 */       if (mouseX >= x && mouseX <= x + width + this.arrowOffset - 1 && 
/* 135 */         mouseY >= y && mouseY <= y + height + 1) {
/* 136 */         state = this.open ? ButtonState.HOVERACTIVE : ButtonState.HOVER;
/* 137 */         if (this.mouseDown) {
/* 138 */           this.open = true;
/*     */         }
/*     */       } 
/*     */     } else {
/* 142 */       state = ButtonState.DISABLED;
/*     */     } 
/* 144 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 145 */     int lineColor = getColor(DrawType.LINE, state);
/* 146 */     int textColor = getColor(DrawType.TEXT, ButtonState.NORMAL);
/*     */     
/* 148 */     drawRect(x + 1, y + 1, width - 2 + this.arrowOffset, height - 1, backgroundColor);
/* 149 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 150 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 151 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 152 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 153 */     drawHorizontalLine(x + width, y, this.arrowOffset, 1, lineColor);
/* 154 */     drawHorizontalLine(x + width, y + height, this.arrowOffset, 1, lineColor);
/* 155 */     drawVerticalLine(x + width + this.arrowOffset - 1, y + 1, height - 1, 1, lineColor);
/* 156 */     int arrowX = x + width - 1 + this.arrowOffset / 2;
/* 157 */     int arrowY = y + (height - 1) / 2;
/* 158 */     if (this.open) {
/* 159 */       drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
/* 160 */       drawHorizontalLine(arrowX - 2, arrowY + 2, 7, 1, textColor);
/* 161 */       drawHorizontalLine(arrowX - 3, arrowY + 3, 9, 1, textColor);
/* 162 */       drawHorizontalLine(arrowX, arrowY, 3, 1, textColor);
/* 163 */       drawPixel(arrowX + 1, arrowY - 1, textColor);
/*     */     } else {
/* 165 */       drawHorizontalLine(arrowX - 3, arrowY - 1, 9, 1, textColor);
/* 166 */       drawHorizontalLine(arrowX - 2, arrowY, 7, 1, textColor);
/* 167 */       drawHorizontalLine(arrowX - 1, arrowY + 1, 5, 1, textColor);
/* 168 */       drawHorizontalLine(arrowX, arrowY + 2, 3, 1, textColor);
/* 169 */       drawPixel(arrowX + 1, arrowY + 3, textColor);
/*     */     } 
/* 171 */     drawText(this.selected, x + 1 + width / 2 - getStringWidth(this.selected) / 2, y + 1, textColor);
/* 172 */     drawDropdown(state);
/* 173 */     this.mouseDown = false;
/*     */   }
/*     */   
/*     */   public void drawDropdown(ButtonState state) {
/* 177 */     if (this.open) {
/* 178 */       int x = getRenderX();
/* 179 */       int y = getRenderY();
/* 180 */       int mouseX = this.parent.getMouseX();
/* 181 */       int mouseY = this.parent.getMouseY();
/* 182 */       int backgroundColor = getColor(DrawType.BACKGROUND, ButtonState.POPUP);
/* 183 */       int lineColor = getColor(DrawType.LINE, ButtonState.POPUP);
/* 184 */       int width = this.width + this.textOffset + this.arrowOffset - 1;
/* 185 */       int height = this.totalHeight + this.values.length;
/* 186 */       drawRect(x, y + this.height + 1, width, height, backgroundColor);
/* 187 */       drawHorizontalLine(x, y + this.height + 1, width + 1, 1, lineColor);
/* 188 */       drawVerticalLine(x, y + this.height + 2, height - 1, 1, lineColor);
/* 189 */       drawHorizontalLine(x, y + this.height + 1 + height, width + 1, 1, lineColor);
/* 190 */       drawVerticalLine(x + width, y + this.height + 2, height - 1, 1, lineColor);
/* 191 */       y += 2;
/* 192 */       boolean inHover = false;
/* 193 */       for (int i = 0; i < this.values.length; i++) {
/* 194 */         String value = this.values[i];
/* 195 */         int sHeight = getStringHeight(value);
/* 196 */         y += sHeight + 1;
/*     */         
/* 198 */         boolean hover = false;
/* 199 */         if (mouseX >= x + 1 && mouseX <= x + width - 1 && 
/* 200 */           mouseY >= y && mouseY <= y + sHeight) {
/* 201 */           hover = true;
/* 202 */           drawRect(x + 1, y, width - 1, sHeight, getColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE));
/* 203 */           if (this.mouseDown) {
/* 204 */             this.selected = this.values[i];
/* 205 */             onAction();
/*     */           } 
/* 207 */           inHover = true;
/*     */         } 
/*     */         
/* 210 */         drawText(value, x + 1 + width / 2 - getStringWidth(value) / 2, y, getColor(DrawType.TEXT, this.selected.equals(value) ? (hover ? ButtonState.HOVERACTIVE : ButtonState.ACTIVE) : (hover ? ButtonState.HOVER : ButtonState.NORMAL)));
/*     */       } 
/* 212 */       if (this.open && !inHover && this.mouseDown && state != ButtonState.HOVER && state != ButtonState.HOVERACTIVE)
/* 213 */         this.open = false; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getValuesFormatted() {
/* 218 */     return getValue().replaceAll("_", " ");
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 222 */     return this.selected;
/*     */   }
/*     */   
/*     */   public void setValue(String values) {
/* 226 */     if (values == null) {
/*     */       return;
/*     */     }
/* 229 */     for (int i = 0; i < this.values.length; i++) {
/* 230 */       for (int ii = 0; ii < this.values.length; ii++) {
/* 231 */         if (values.replaceAll(" ", "_").equalsIgnoreCase(this.values[i].replaceAll(" ", "_"))) {
/* 232 */           this.selected = this.values[i];
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     } 
/* 238 */     String label = getValuesFormatted();
/* 239 */     int labelWidth = getStringWidth(label);
/* 240 */     while (labelWidth >= this.width + this.textOffset) {
/* 241 */       label = label.substring(0, label.length() - 1);
/* 242 */       labelWidth = getStringWidth(label);
/*     */     } 
/* 244 */     this.lastValueString = label;
/*     */   }
/*     */   
/*     */   public void onAction() {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuDropdown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */