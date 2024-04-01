/*     */ package me.onefightonewin.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.HeadlessException;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.datatransfer.Clipboard;
/*     */ import java.awt.datatransfer.DataFlavor;
/*     */ import java.awt.datatransfer.StringSelection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.feature.types.Antiafk;
/*     */ import me.onefightonewin.feature.types.ArmorStatus;
/*     */ import me.onefightonewin.feature.types.Coordinates;
/*     */ import me.onefightonewin.feature.types.FPS;
/*     */ import me.onefightonewin.feature.types.Keystrokes;
/*     */ import me.onefightonewin.feature.types.StatusEffect;
/*     */ import me.onefightonewin.feature.types.TextMode;
/*     */ import me.onefightonewin.gui.framework.ButtonState;
/*     */ import me.onefightonewin.gui.framework.DrawType;
/*     */ import me.onefightonewin.gui.framework.Menu;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MinecraftMenuImpl;
/*     */ import me.onefightonewin.gui.framework.components.MenuBindButton;
/*     */ import me.onefightonewin.gui.framework.components.MenuButton;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuDraggable;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuNoDrag;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import me.onefightonewin.utils.FileUtils;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiMenu
/*     */   extends MinecraftMenuImpl
/*     */ {
/*  54 */   public static Color theColor = new Color(0, 0, 255);
/*  55 */   public static Color theColorHover = lightenColor(10, 10, theColor);
/*     */   
/*  57 */   public static int curBg = -1;
/*  58 */   int curtab = -1;
/*  59 */   Configs curConfig = Configs.BLATANT;
/*     */   
/*     */   boolean readyToClick = false;
/*     */   
/*  63 */   int background = (new Color(10, 10, 10, 255)).getRGB();
/*  64 */   int featureBackground = (new Color(15, 15, 15, 255)).getRGB();
/*  65 */   int panelBackground = (new Color(12, 12, 12, 255)).getRGB();
/*  66 */   int borderColor = (new Color(20, 20, 20, 255)).getRGB();
/*     */   
/*  68 */   int featureBackgroundMeme = (new Color(15, 15, 15, 100)).getRGB();
/*  69 */   int panelBackgroundMeme = (new Color(12, 12, 12, 100)).getRGB();
/*  70 */   int borderColorMeme = (new Color(20, 20, 20, 100)).getRGB();
/*     */   
/*  72 */   int text = (new Color(255, 255, 255, 255)).getRGB();
/*     */   
/*  74 */   public int savedWidth = -1;
/*  75 */   public int savedHeight = -1;
/*     */   
/*     */   public boolean initd;
/*     */   MenuDraggable drag;
/*     */   
/*     */   public GuiMenu(Menu menu, KeyBinding keybinding) {
/*  81 */     super(1.0F, menu, keybinding);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  86 */     if (this.initd) {
/*  87 */       clearOptions();
/*  88 */       loadOptions();
/*     */       return;
/*     */     } 
/*  91 */     this.initd = true;
/*     */ 
/*     */     
/*  94 */     theColor = new Color(0, 0, 255);
/*  95 */     theColorHover = lightenColor(10, 10, theColor);
/*     */     
/*  97 */     this.drag = new MenuDraggable(0, 0, this.menu.getWidth(), this.menu.getHeight());
/*     */     
/*  99 */     this.menu.addComponent((MenuComponent)this.drag);
/*     */   }
/*     */   
/*     */   public void renderMenu() {
/* 103 */     if ((AntiAFK.getInstance()).legit) {
/* 104 */       drawRectangle(this.menu.getX(), this.menu.getY(), this.menu.getWidth(), this.menu.getHeight(), this.featureBackground);
/* 105 */       drawRainbowBar(this.menu.getX(), this.menu.getY(), this.menu.getWidth(), 5);
/* 106 */       drawBox("Anti afkxxxxx", this.menu.getX() + 2, this.menu.getY(), 190, 190, false);
/*     */       
/*     */       return;
/*     */     } 
/* 110 */     drawRectangle(this.menu.getX(), this.menu.getY(), 60, this.menu.getHeight(), this.background);
/*     */     
/* 112 */     if (curBg == -1) {
/* 113 */       drawRectangle(this.menu.getX() + 60, this.menu.getY(), this.menu.getWidth() - 60, this.menu.getHeight(), this.featureBackground);
/*     */     } else {
/* 115 */       drawImage((AntiAFK.getInstance()).backgrounds[curBg], this.menu.getX() + 60, this.menu.getY(), this.menu.getWidth() - 60, this.menu.getHeight(), 1.0F);
/*     */     } 
/*     */     
/* 118 */     drawRainbowBar(this.menu.getX(), this.menu.getY(), this.menu.getWidth(), 2);
/*     */     
/* 120 */     int spacing = 10;
/* 121 */     int menuY = this.menu.getY() + this.menu.getHeight() / 2 - ((Category.values()).length - 1) * 30 - spacing;
/* 122 */     int hover = -1;
/* 123 */     boolean isMouseDown = Mouse.isButtonDown(0);
/*     */     
/* 125 */     if (this.menu.getMouseX() > this.menu.getX() && this.menu.getMouseX() - this.menu.getX() <= 60) {
/* 126 */       for (int j = 0; j < (Category.values()).length; j++) {
/* 127 */         Category category = Category.values()[j];
/*     */         
/* 129 */         if (this.menu.getMouseY() >= menuY + j * 50 + spacing * j && this.menu.getMouseY() <= menuY + 50 + j * 50 + spacing * j) {
/* 130 */           hover = category.getId();
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*     */     }
/* 136 */     if (isMouseDown) {
/* 137 */       if (hover != -1) {
/* 138 */         if (this.readyToClick) {
/* 139 */           this.readyToClick = false;
/*     */           
/* 141 */           boolean newTab = false;
/*     */           
/* 143 */           if (this.curtab != hover) {
/* 144 */             newTab = true;
/*     */           }
/*     */           
/* 147 */           this.curtab = hover;
/*     */           
/* 149 */           if (newTab) {
/* 150 */             clearOptions();
/* 151 */             loadOptions();
/*     */           } 
/*     */         } else {
/* 154 */           hover = -1;
/*     */         } 
/*     */       } else {
/* 157 */         this.readyToClick = false;
/*     */       } 
/*     */     } else {
/* 160 */       this.readyToClick = true;
/*     */     } 
/*     */     
/* 163 */     for (int i = 0; i < (Category.values()).length; i++) {
/* 164 */       Category category = Category.values()[i];
/* 165 */       drawImage(category.getResource(), this.menu.getX() + 5, menuY + i * 50 + spacing * i, 50, 50, (this.curtab == category.getId()) ? ((hover == category.getId()) ? 1.0F : 0.9F) : ((hover == category.getId()) ? 0.9F : 0.7F));
/*     */     } 
/*     */     
/* 168 */     GlStateManager.pushMatrix();
/* 169 */     float value = 4.0F / (new ScaledResolution(this.mc)).getScaleFactor();
/* 170 */     GlStateManager.scale(value, value, value);
/* 171 */     int width = Math.round(getStringWidth((AntiAFK.getInstance()).name) * 1.1F);
/* 172 */     int height = Math.round(getStringHeight((AntiAFK.getInstance()).name) * 1.1F);
/* 173 */     int menuWidth = Math.round(this.menu.getWidth() / value);
/* 174 */     int menuHeight = Math.round(this.menu.getHeight() / value);
/*     */     
/* 176 */     int x = Math.round(this.menu.getX() / value);
/* 177 */     int y = Math.round(this.menu.getY() / value);
/*     */     
/* 179 */     drawText((AntiAFK.getInstance()).name, x + menuWidth - width, y + menuHeight - height - 1, theColor.getRGB());
/* 180 */     GlStateManager.popMatrix();
/*     */     
/* 182 */     if (this.curtab == -1) {
/*     */       return;
/*     */     }
/* 185 */     x = this.menu.getX() + 75;
/* 186 */     y = this.menu.getY() + 25;
/*     */     
/* 188 */     if (this.curtab == Category.PAGE_MISC.getId()) {
/* 189 */       y += 210;
/*     */       
/* 191 */       y += 20;
/*     */       
/* 193 */       drawBox("Misc", x, y + 100, 200, 100, true);
/* 194 */       drawBox("Settings", x + 210, y + 100, 200, 120, true);
/*     */     } 
/*     */     
/* 197 */     spacing = 3;
/* 198 */     int[] ys = { this.menu.getY() + 25, this.menu.getY() + 25 };
/*     */     
/* 200 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 201 */       if (feature.getCategory().getId() != this.curtab) {
/*     */         continue;
/*     */       }
/*     */       
/* 205 */       x = this.menu.getX() + 75;
/*     */       
/* 207 */       int index = 0;
/*     */       
/* 209 */       if (ys[0] > ys[1]) {
/* 210 */         index = 1;
/* 211 */         x += 210;
/*     */       } 
/* 213 */       int orginalY = ys[index];
/* 214 */       ys[index] = ys[index] + spacing * 4;
/*     */       
/* 216 */       List<MenuComponent> toAdd = new ArrayList<>();
/* 217 */       feature.onMenuAdd(toAdd);
/*     */       
/* 219 */       for (MenuComponent adding : toAdd) {
/* 220 */         if (adding instanceof me.onefightonewin.gui.framework.components.MenuDataObject) {
/*     */           continue;
/*     */         }
/*     */         
/* 224 */         boolean picker = false;
/*     */         
/* 226 */         if (adding instanceof MenuColorPicker) {
/* 227 */           picker = true;
/*     */         }
/*     */         
/* 230 */         if (!picker) {
/* 231 */           ys[index] = ys[index] + adding.getHeight();
/*     */         }
/*     */         
/* 234 */         ys[index] = ys[index] + spacing;
/*     */       } 
/*     */       
/* 237 */       if (feature.getKey() != -1) {
/* 238 */         ys[index] = ys[index] + 20;
/*     */       }
/*     */       
/* 241 */       drawBox("", x, orginalY - 38, 200, ys[index] - orginalY - 49 + 12, false);
/* 242 */       ys[index] = ys[index] + 50;
/*     */     } 
/*     */   }
/*     */   
/*     */   enum Background {
/* 247 */     NO_BACKGROUND, BACKGROUND_1, BACKGROUND_2, BACKGROUND_3, BACKGROUND_4, BACKGROUND_5, BACKGROUND_6, BACKGROUND_7, BACKGROUND_8,
/* 248 */     BACKGROUND_9, BACKGROUND_10, BACKGROUND_11, BACKGROUND_12, BACKGROUND_13, BACKGROUND_14, BACKGROUND_15, BACKGROUND_16, BACKGROUND_17;
/*     */   }
/*     */   
/*     */   enum Configs {
/* 252 */     ASSASSIN, MAGE, KNIGHT, RANGER, BRUTE, BRIDGES, CAKEWARS, BLATANT, LEGIT, MINEPLEX, HYPIXEL;
/*     */   }
/*     */   
/*     */   public void applyColors(MenuComponent comp) {
/* 256 */     if (comp instanceof MenuCheckbox) {
/* 257 */       MenuCheckbox checkbox = (MenuCheckbox)comp;
/*     */       
/* 259 */       checkbox.setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, theColor);
/* 260 */       checkbox.setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, theColorHover);
/* 261 */     } else if (comp instanceof MenuSlider) {
/* 262 */       MenuSlider slider = (MenuSlider)comp;
/*     */       
/* 264 */       slider.setColor(DrawType.LINE, ButtonState.POPUP, theColor);
/* 265 */     } else if (comp instanceof MenuComboBox) {
/* 266 */       MenuComboBox combo = (MenuComboBox)comp;
/*     */       
/* 268 */       combo.setColor(DrawType.TEXT, ButtonState.ACTIVE, theColor);
/* 269 */       combo.setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, theColorHover);
/* 270 */     } else if (comp instanceof MenuDropdown) {
/* 271 */       MenuDropdown combo = (MenuDropdown)comp;
/*     */       
/* 273 */       combo.setColor(DrawType.TEXT, ButtonState.ACTIVE, theColor);
/* 274 */       combo.setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, theColorHover);
/*     */     } 
/*     */   }
/*     */   
/*     */   public <T extends Feature> void addLegitOption(final Class<T> feature, String name, int y) {
/* 279 */     MenuCheckbox checkbox = new MenuCheckbox(name, 15, y, 12, 12)
/*     */       {
/*     */         public void onAction() {
/* 282 */           AntiAFK.getInstance().getFeature(feature).setEnabled(isChecked());
/*     */         }
/*     */       };
/*     */     
/* 286 */     checkbox.setChecked(AntiAFK.getInstance().getFeature(feature).isEnabled());
/* 287 */     this.menu.addComponent((MenuComponent)checkbox);
/*     */   }
/*     */   
/*     */   public void loadOptions() {
/* 291 */     if ((AntiAFK.getInstance()).legit) {
/* 292 */       this.menu.addComponent((MenuComponent)new MenuNoDrag(5, 30, 190, 159));
/*     */       
/* 294 */       MenuCheckbox antiafk = new MenuCheckbox("Anti afk", 15, 25, 12, 12)
/*     */         {
/*     */           public void onAction() {
/* 297 */             ((Antiafk)AntiAFK.getInstance().getFeature(Antiafk.class)).setEnabled(isChecked());
/*     */           }
/*     */         };
/*     */       
/* 301 */       MenuLabel intervalLabel = new MenuLabel("Interval", 12, 45);
/*     */       
/* 303 */       MenuSlider interval = new MenuSlider(((Antiafk)AntiAFK.getInstance().getFeature(Antiafk.class)).antiAfkInterval.getIntValue(), 1000, 10000, 12, 60, 100, 7)
/*     */         {
/*     */           public void onAction() {
/* 306 */             ((Antiafk)AntiAFK.getInstance().getFeature(Antiafk.class)).antiAfkInterval.setValue(getIntValue());
/*     */           }
/*     */         };
/*     */       
/* 310 */       addLegitOption(Keystrokes.class, "Keystrokes", 75);
/* 311 */       addLegitOption(FPS.class, "FPS", 90);
/* 312 */       addLegitOption(ArmorStatus.class, "Armor status", 105);
/* 313 */       addLegitOption(StatusEffect.class, "Status effect", 120);
/* 314 */       addLegitOption(Coordinates.class, "Coordinates", 135);
/*     */       
/* 316 */       this.menu.addComponent((MenuComponent)antiafk);
/* 317 */       this.menu.addComponent((MenuComponent)intervalLabel);
/* 318 */       this.menu.addComponent((MenuComponent)interval);
/*     */       
/*     */       return;
/*     */     } 
/* 322 */     int x = 75;
/* 323 */     int y = 125;
/*     */     
/* 325 */     this.menu.addComponent((MenuComponent)new MenuNoDrag(0, 0, 60, this.menu.getHeight()));
/*     */     
/* 327 */     if (this.curtab == Category.PAGE_MISC.getId()) {
/* 328 */       this.menu.addComponent((MenuComponent)new MenuNoDrag(x, y + 210, 200, 200));
/* 329 */       this.menu.addComponent((MenuComponent)new MenuNoDrag(x + 210, y + 210, 200, 200));
/*     */       
/* 331 */       x += 10;
/* 332 */       y += 25;
/*     */       
/* 334 */       y += 210;
/*     */       
/* 336 */       y += 20;
/*     */       
/* 338 */       this.menu.addComponent((MenuComponent)new MenuLabel("Color", x + 2, y));
/* 339 */       this.menu.addComponent((MenuComponent)new MenuColorPicker(x + 160, y, 20, 10, theColor.getRGB())
/*     */           {
/*     */             public void onAction() {
/* 342 */               GuiMenu.theColor = getColor();
/* 343 */               GuiMenu.theColorHover = GuiMenu.lightenColor(10, 10, GuiMenu.theColor);
/*     */             }
/*     */           });
/*     */ 
/*     */ 
/*     */       
/* 349 */       MenuButton selfDestruct = new MenuButton("Selfdestruct", x, y + 15)
/*     */         {
/*     */           public void onAction() {
/* 352 */             if (isActive()) {
/* 353 */               AntiAFK.getInstance().selfDestruct(true);
/*     */             }
/*     */           }
/*     */         };
/*     */       
/* 358 */       MenuDropdown configs = new MenuDropdown(Configs.class, x + 210, y)
/*     */         {
/*     */           public void onAction() {
/*     */             try {
/* 362 */               GuiMenu.this.curConfig = GuiMenu.Configs.valueOf(getValue().toUpperCase());
/* 363 */             } catch (NumberFormatException e) {
/* 364 */               GuiMenu.curBg = -1;
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 369 */       configs.setValue(this.curConfig.toString().toLowerCase());
/* 370 */       this.menu.addComponent((MenuComponent)configs);
/*     */       
/* 372 */       MenuButton load = new MenuButton("Load", x + 210, y + 20, 80, 25)
/*     */         {
/*     */           public void onAction() {
/* 375 */             FileUtils.loadConfig(FileUtils.getConfigFromFile(GuiMenu.this.curConfig.toString().toLowerCase()));
/* 376 */             GuiMenu.this.curtab = -1;
/* 377 */             setActive(false);
/*     */           }
/*     */         };
/*     */       
/* 381 */       MenuButton save = new MenuButton("Save", x + 300, y + 20, 80, 25)
/*     */         {
/*     */           public void onAction() {
/* 384 */             FileUtils.saveConfig(GuiMenu.this.curConfig.toString().toLowerCase(), FileUtils.getInternalConfig());
/* 385 */             setActive(false);
/*     */           }
/*     */         };
/*     */       
/* 389 */       MenuButton importConfig = new MenuButton("Import", x + 210, y + 50, 80, 25)
/*     */         {
/*     */           public void onAction() {
/*     */             try {
/* 393 */               FileUtils.loadConfig(FileUtils.getConfigDataFromString((String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor)));
/* 394 */             } catch (HeadlessException|java.awt.datatransfer.UnsupportedFlavorException|java.io.IOException|NumberFormatException e) {
/* 395 */               e.printStackTrace();
/*     */             } 
/*     */             
/* 398 */             setActive(false);
/*     */           }
/*     */         };
/*     */       
/* 402 */       MenuButton export = new MenuButton("Export", x + 300, y + 50, 80, 25)
/*     */         {
/*     */           public void onAction() {
/* 405 */             StringSelection stringSelection = new StringSelection(FileUtils.getExportConfig());
/* 406 */             Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
/* 407 */             clipboard.setContents(stringSelection, null);
/* 408 */             setActive(false);
/*     */           }
/*     */         };
/*     */       
/* 412 */       this.menu.addComponent((MenuComponent)load);
/* 413 */       this.menu.addComponent((MenuComponent)save);
/* 414 */       this.menu.addComponent((MenuComponent)importConfig);
/* 415 */       this.menu.addComponent((MenuComponent)export);
/*     */       
/* 417 */       MenuDropdown comp = new MenuDropdown(Background.class, x, y + 31)
/*     */         {
/*     */           public void onAction() {
/*     */             try {
/* 421 */               String value = getValue().toString().split(" ")[1];
/* 422 */               System.out.println(getValue());
/* 423 */               GuiMenu.curBg = Integer.parseInt(value) - 1;
/* 424 */             } catch (NumberFormatException e) {
/* 425 */               GuiMenu.curBg = -1;
/*     */             } 
/*     */           }
/*     */         };
/*     */       
/* 430 */       applyColors((MenuComponent)configs);
/*     */       
/* 432 */       if (curBg != -1) {
/* 433 */         comp.setValue("Background_" + (curBg + 1));
/*     */       } else {
/* 435 */         comp.setValue("No_background");
/*     */       } 
/* 437 */       applyColors((MenuComponent)comp);
/* 438 */       this.menu.addComponent((MenuComponent)comp);
/*     */       
/* 440 */       this.menu.addComponent((MenuComponent)selfDestruct);
/*     */     } 
/*     */     
/* 443 */     int spacing = 3;
/* 444 */     int[] ys = { 25, 25 };
/*     */     
/* 446 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 447 */       if (feature.getCategory().getId() != this.curtab) {
/*     */         continue;
/*     */       }
/*     */       
/* 451 */       x = 80;
/*     */       
/* 453 */       int index = 0;
/*     */       
/* 455 */       if (ys[0] > ys[1]) {
/* 456 */         index = 1;
/* 457 */         x += 210;
/*     */       } 
/*     */       
/* 460 */       int orginalY = ys[index];
/*     */       
/* 462 */       MenuCheckbox checkbox = new MenuCheckbox(((TextMode)AntiAFK.getInstance().getFeature(TextMode.class)).getString(feature.getName()), x, ys[index], 12, 12)
/*     */         {
/*     */           public void onAction() {
/* 465 */             feature.setEnabled(isChecked());
/*     */           }
/*     */         };
/*     */       
/* 469 */       applyColors((MenuComponent)checkbox);
/*     */       
/* 471 */       checkbox.setChecked(feature.isEnabled());
/* 472 */       this.menu.addComponent((MenuComponent)checkbox);
/*     */       
/* 474 */       ys[index] = ys[index] + spacing * 4;
/*     */       
/* 476 */       List<MenuComponent> toAdd = new ArrayList<>();
/*     */       
/* 478 */       feature.onMenuAdd(toAdd);
/*     */       
/* 480 */       for (MenuComponent adding : toAdd) {
/* 481 */         if (adding instanceof me.onefightonewin.gui.framework.components.MenuDataObject) {
/*     */           continue;
/*     */         }
/*     */         
/* 485 */         boolean picker = false;
/*     */         
/* 487 */         if (adding instanceof MenuColorPicker) {
/* 488 */           picker = true;
/*     */         }
/*     */         
/* 491 */         if (!picker) {
/* 492 */           ys[index] = ys[index] + adding.getHeight();
/*     */         }
/*     */         
/* 495 */         adding.setX(x + (picker ? 160 : 0));
/* 496 */         adding.setY(ys[index] - (picker ? 3 : 0));
/*     */         
/* 498 */         if (adding instanceof MenuLabel) {
/* 499 */           adding.setY(adding.getY() + 2);
/* 500 */         } else if (adding instanceof MenuSlider) {
/* 501 */           adding.setY(adding.getY() + 3);
/*     */         } 
/*     */         
/* 504 */         applyColors(adding);
/* 505 */         this.menu.addComponent(adding);
/*     */         
/* 507 */         ys[index] = ys[index] + spacing;
/*     */       } 
/*     */       
/* 510 */       if (feature.getKey() != -1) {
/* 511 */         ys[index] = ys[index] + 20;
/*     */         
/* 513 */         MenuBindButton button = new MenuBindButton(x - 3, ys[index], 195, 20)
/*     */           {
/*     */             public void onBindAction() {
/* 516 */               feature.setKey(getKey());
/*     */             }
/*     */           };
/*     */         
/* 520 */         button.setKey(feature.getKey());
/*     */         
/* 522 */         applyColors((MenuComponent)button);
/* 523 */         this.menu.addComponent((MenuComponent)button);
/*     */       } 
/*     */       
/* 526 */       this.menu.addComponent((MenuComponent)new MenuNoDrag(x - 5, orginalY, 200, ys[index] - orginalY - 29));
/*     */       
/* 528 */       ys[index] = ys[index] + 50;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Color lightenColor(int index, int modifier, Color color) {
/* 533 */     int newRed = color.getRed() + index * modifier;
/* 534 */     int newGreen = color.getGreen() + index * modifier;
/* 535 */     int newBlue = color.getBlue() + index * modifier;
/*     */     
/* 537 */     if (newRed > 255) {
/* 538 */       newRed = 255;
/*     */     }
/* 540 */     if (newGreen > 255) {
/* 541 */       newGreen = 255;
/*     */     }
/* 543 */     if (newBlue > 255) {
/* 544 */       newBlue = 255;
/*     */     }
/* 546 */     return new Color(newRed, newGreen, newBlue, color.getAlpha());
/*     */   }
/*     */   
/*     */   private void drawBox(String name, int x, int y, int width, int height, boolean showText) {
/* 550 */     if (!showText) {
/* 551 */       y += 30;
/* 552 */       height -= 30;
/*     */     } 
/*     */     
/* 555 */     if (curBg != -1) {
/* 556 */       drawRectangle(x, y, width, height, this.borderColorMeme);
/* 557 */       drawRectangle(x + 2, y + 2, width - 4, height - 4, this.panelBackgroundMeme);
/* 558 */       if (!name.isEmpty()) {
/* 559 */         drawRectangle(x + 5, y, getStringWidth(name) + 4, 2, this.panelBackgroundMeme);
/*     */       }
/*     */     } else {
/* 562 */       drawRectangle(x, y, width, height, this.borderColor);
/* 563 */       drawRectangle(x + 2, y + 2, width - 4, height - 4, this.panelBackground);
/* 564 */       if (!name.isEmpty()) {
/* 565 */         drawRectangle(x + 5, y, getStringWidth(name) + 4, 2, this.panelBackground);
/*     */       }
/*     */     } 
/*     */     
/* 569 */     if (showText) {
/* 570 */       drawText(name, x + 7, y - 4, this.text);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 576 */     if (this.savedWidth != this.mc.displayWidth || this.savedHeight != this.mc.displayHeight) {
/* 577 */       this.savedWidth = this.mc.displayWidth;
/* 578 */       this.savedHeight = this.mc.displayHeight;
/* 579 */       this.menu.setX(this.savedWidth / 2 - this.menu.getWidth() / 2);
/* 580 */       this.menu.setY(this.savedHeight / 2 - this.menu.getHeight() / 2);
/*     */     } 
/*     */     
/* 583 */     GlStateManager.pushMatrix();
/* 584 */     float value = this.guiScale / (new ScaledResolution(this.mc)).getScaleFactor();
/* 585 */     GlStateManager.scale(value, value, value);
/* 586 */     renderMenu();
/* 587 */     GlStateManager.popMatrix();
/*     */     
/* 589 */     if (this.curtab == -1) {
/* 590 */       clearOptions();
/* 591 */       this.curtab = 5;
/* 592 */       loadOptions();
/*     */     } 
/*     */     
/* 595 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   private void clearOptions() {
/* 599 */     List<MenuComponent> toRemove = new ArrayList<>();
/*     */     
/* 601 */     for (int i = 0; i < this.menu.getComponents().size(); i++) {
/* 602 */       MenuComponent component = this.menu.getComponents().get(i);
/*     */       
/* 604 */       if (component != null && component != this.drag)
/*     */       {
/*     */         
/* 607 */         toRemove.add(component);
/*     */       }
/*     */     } 
/* 610 */     this.menu.getComponents().removeAll(toRemove);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawImage(ResourceLocation image, int x, int y, int width, int height, float alpha) {
/* 616 */     GlStateManager.enableAlpha();
/* 617 */     GlStateManager.enableBlend();
/* 618 */     GlStateManager.blendFunc(770, 771);
/* 619 */     Minecraft.getMinecraft().getTextureManager().bindTexture(image);
/* 620 */     GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
/* 621 */     GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
/* 622 */     GlStateManager.disableBlend();
/* 623 */     GlStateManager.disableAlpha();
/*     */   }
/*     */   
/*     */   public int getStringHeight(String string) {
/* 627 */     return (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String string) {
/* 631 */     return (Minecraft.getMinecraft()).fontRenderer.getStringWidth(string);
/*     */   }
/*     */   
/*     */   public void drawRectangle(int x, int y, int width, int height, int color) {
/* 635 */     GuiScreen.drawRect(x, y, x + width, y + height, color);
/*     */   }
/*     */   
/*     */   public void drawText(String text, int x, int y, int color) {
/* 639 */     (Minecraft.getMinecraft()).fontRenderer.drawString(text, x, y + 1, color);
/*     */   }
/*     */   
/*     */   public void drawVerticalLine(int x, int y, int height, int thickness, int color) {
/* 643 */     drawRect(x, y, thickness, height, color);
/*     */   }
/*     */   
/*     */   public void drawHorizontalLine(int x, int y, int width, int thickness, int color) {
/* 647 */     drawRect(x, y, width, thickness, color);
/*     */   }
/*     */   
/*     */   public void drawPixel(int x, int y, int color) {
/* 651 */     drawRect(x, y, 1, 1, color);
/*     */   }
/*     */   
/*     */   public void drawRainbowBar(int rainbowX, int rainbowY, int rainbowWidth, int rainbowHeight) {
/* 655 */     int colorSize = 6;
/* 656 */     drawGradientRect(rainbowX, rainbowY, rainbowX + rainbowWidth / colorSize, rainbowY + rainbowHeight, (new Color(104, 200, 119)).getRGB(), (new Color(122, 189, 216)).getRGB());
/* 657 */     drawGradientRect(rainbowX + rainbowWidth / colorSize, rainbowY, rainbowX + rainbowWidth / colorSize * 2, rainbowY + rainbowHeight, (new Color(122, 189, 216)).getRGB(), (new Color(45, 114, 185)).getRGB());
/* 658 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 2, rainbowY, rainbowX + rainbowWidth / colorSize * 3, rainbowY + rainbowHeight, (new Color(45, 114, 185)).getRGB(), (new Color(87, 157, 187)).getRGB());
/* 659 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 3, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 4, rainbowY + rainbowHeight, (new Color(87, 157, 187)).getRGB(), (new Color(98, 111, 164)).getRGB());
/* 660 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 4, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 5, rainbowY + rainbowHeight, (new Color(98, 111, 164)).getRGB(), (new Color(145, 82, 135)).getRGB());
/* 661 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 5, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 6, rainbowY + rainbowHeight, (new Color(145, 82, 135)).getRGB(), (new Color(187, 75, 100)).getRGB());
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
/* 666 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/* 667 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/* 668 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/* 669 */     float f3 = (startColor & 0xFF) / 255.0F;
/* 670 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/* 671 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/* 672 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/* 673 */     float f7 = (endColor & 0xFF) / 255.0F;
/* 674 */     GlStateManager.disableTexture2D();
/* 675 */     GlStateManager.enableBlend();
/* 676 */     GlStateManager.disableAlpha();
/* 677 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 678 */     GlStateManager.shadeModel(7425);
/* 679 */     Tessellator tessellator = Tessellator.getInstance();
/* 680 */     WorldRenderer bufferbuilder = tessellator.getBuffer();
/* 681 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 682 */     bufferbuilder.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 683 */     bufferbuilder.pos(right, top, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 684 */     bufferbuilder.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
/* 685 */     bufferbuilder.pos(left, bottom, 0.0D).color(f1, f2, f3, f).endVertex();
/* 686 */     tessellator.draw();
/* 687 */     GlStateManager.shadeModel(7424);
/* 688 */     GlStateManager.disableBlend();
/* 689 */     GlStateManager.enableAlpha();
/* 690 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */   
/*     */   public void setMenu(Menu menu) {
/* 694 */     this.menu = menu;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\GuiMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */