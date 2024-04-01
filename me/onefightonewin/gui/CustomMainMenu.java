/*     */ package me.onefightonewin.gui;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.gui.altmanager.GuiAltManager;
/*     */ import me.onefightonewin.gui.framework.Menu;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.MinecraftMenuImpl;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.mainmenu.MenuButtonMainMenu;
/*     */ import me.onefightonewin.gui.framework.components.mainmenu.MenuButtonMainMenuImage;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiLanguage;
/*     */ import net.minecraft.client.gui.GuiMultiplayer;
/*     */ import net.minecraft.client.gui.GuiOptions;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiSelectWorld;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.fml.common.FMLCommonHandler;
/*     */ 
/*     */ public class CustomMainMenu
/*     */   extends MinecraftMenuImpl {
/*  28 */   public int savedWidth = -1;
/*  29 */   public int savedHeight = -1;
/*     */   
/*     */   public boolean initd;
/*     */   
/*  33 */   private static final ResourceLocation bg = new ResourceLocation("antiafk", "textures/gui/main-menu-bg.png");
/*     */   
/*  35 */   private static final ResourceLocation LOGO = new ResourceLocation("antiafk", "textures/gui/main-menu-logo.png");
/*  36 */   private static final ResourceLocation OPTION_ICON = new ResourceLocation("antiafk", "textures/gui/option-icon.png");
/*  37 */   private static final ResourceLocation QUIT_ICON = new ResourceLocation("antiafk", "textures/gui/quit-icon.png");
/*  38 */   private static final ResourceLocation LANGUAGE_ICON = new ResourceLocation("antiafk", "textures/gui/language-icon.png");
/*     */   
/*     */   public CustomMainMenu() {
/*  41 */     super(2.0F, new Menu("", Math.round((Minecraft.getMinecraft()).displayWidth / 2.0F), Math.round((Minecraft.getMinecraft()).displayHeight / 2.0F)), null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGui() {
/*  46 */     if (this.initd) {
/*     */       return;
/*     */     }
/*  49 */     this.initd = true;
/*  50 */     this.menu.getComponents().clear();
/*  51 */     this.menu.setWidth(Math.round((Minecraft.getMinecraft()).displayWidth / this.guiScale));
/*  52 */     this.menu.setHeight(Math.round((Minecraft.getMinecraft()).displayHeight / this.guiScale));
/*     */ 
/*     */     
/*  55 */     String copyrightText = "Copyright Mojang AB. Do not distribute!";
/*  56 */     this.menu.addComponent((MenuComponent)new MenuLabel(copyrightText, this.menu.getWidth() - 1 - getStringWidth(copyrightText), this.menu.getHeight() - 1 - getStringHeight(copyrightText)));
/*  57 */     this.menu.addComponent((MenuComponent)new MenuLabel((AntiAFK.getInstance()).name, 5, this.menu.getHeight() - 1 - getStringHeight((AntiAFK.getInstance()).name)));
/*     */     
/*  59 */     int centerX = this.menu.getWidth() / 2;
/*  60 */     int centerY = this.menu.getHeight() / 2;
/*     */     
/*  62 */     MenuButtonMainMenu menuButtonMainMenu1 = new MenuButtonMainMenu("SINGLEPLAYER", centerX - 75, centerY, 150, 20)
/*     */       {
/*     */         public void onAction() {
/*  65 */           super.onAction();
/*  66 */           CustomMainMenu.this.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)CustomMainMenu.this));
/*     */         }
/*     */       };
/*     */     
/*  70 */     MenuButtonMainMenu menuButtonMainMenu2 = new MenuButtonMainMenu("MULTIPLAYER", centerX - 75, centerY + 30, 150, 20)
/*     */       {
/*     */         public void onAction() {
/*  73 */           super.onAction();
/*  74 */           CustomMainMenu.this.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)CustomMainMenu.this));
/*     */         }
/*     */       };
/*     */     
/*  78 */     MenuButtonMainMenu menuButtonMainMenu3 = new MenuButtonMainMenu(this.mc.getSession().getProfile().getName(), 5, 5, 80, 20)
/*     */       {
/*     */         public void onAction() {
/*  81 */           super.onAction();
/*  82 */           CustomMainMenu.this.mc.displayGuiScreen((GuiScreen)new GuiAltManager());
/*     */         }
/*     */       };
/*     */     
/*  86 */     MenuButtonMainMenuImage menuButtonMainMenuImage1 = new MenuButtonMainMenuImage(OPTION_ICON, centerX + 5, this.menu.getHeight() - 15 - 20, 20, 20)
/*     */       {
/*     */         public void onAction() {
/*  89 */           super.onAction();
/*  90 */           CustomMainMenu.this.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)CustomMainMenu.this, CustomMainMenu.this.mc.gameSettings));
/*     */         }
/*     */       };
/*     */     
/*  94 */     MenuButtonMainMenuImage menuButtonMainMenuImage2 = new MenuButtonMainMenuImage(LANGUAGE_ICON, centerX - 20, this.menu.getHeight() - 15 - 20, 20, 20)
/*     */       {
/*     */         public void onAction() {
/*  97 */           super.onAction();
/*  98 */           CustomMainMenu.this.mc.displayGuiScreen((GuiScreen)new GuiLanguage((GuiScreen)CustomMainMenu.this, CustomMainMenu.this.mc.gameSettings, CustomMainMenu.this.mc.getLanguageManager()));
/*     */         }
/*     */       };
/*     */     
/* 102 */     MenuButtonMainMenuImage menuButtonMainMenuImage3 = new MenuButtonMainMenuImage(QUIT_ICON, this.menu.getWidth() - 5 - 20, 5, 20, 20)
/*     */       {
/*     */         public void onAction() {
/* 105 */           super.onAction();
/* 106 */           FMLCommonHandler.instance().exitJava(0, false);
/*     */         }
/*     */       };
/*     */     
/* 110 */     this.menu.addComponent((MenuComponent)menuButtonMainMenu1);
/* 111 */     this.menu.addComponent((MenuComponent)menuButtonMainMenu2);
/* 112 */     this.menu.addComponent((MenuComponent)menuButtonMainMenu3);
/* 113 */     this.menu.addComponent((MenuComponent)menuButtonMainMenuImage1);
/* 114 */     this.menu.addComponent((MenuComponent)menuButtonMainMenuImage2);
/* 115 */     this.menu.addComponent((MenuComponent)menuButtonMainMenuImage3);
/*     */     
/* 117 */     super.initGui();
/*     */   }
/*     */   
/*     */   public void renderMenu() {
/* 121 */     int centerX = this.menu.getWidth();
/* 122 */     int centerY = this.menu.getHeight();
/* 123 */     int width = 100;
/* 124 */     int height = 100;
/*     */     
/* 126 */     drawImage(LOGO, centerX - width / 2, centerY - height / 2 - 100, width, height, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 131 */     if (this.savedWidth != this.mc.displayWidth || this.savedHeight != this.mc.displayHeight) {
/* 132 */       this.savedWidth = this.mc.displayWidth;
/* 133 */       this.savedHeight = this.mc.displayHeight;
/* 134 */       this.menu.setX(0);
/* 135 */       this.menu.setY(0);
/* 136 */       this.initd = false;
/*     */     } 
/*     */     
/* 139 */     GlStateManager.disableAlpha();
/* 140 */     drawImage(bg, 0, 0, this.width, this.height, 1.0F);
/* 141 */     GlStateManager.enableAlpha();
/*     */     
/* 143 */     GlStateManager.pushMatrix();
/* 144 */     float value = 1.0F / (new ScaledResolution(this.mc)).getScaleFactor();
/* 145 */     GlStateManager.scale(value, value, value);
/* 146 */     renderMenu();
/* 147 */     GlStateManager.popMatrix();
/*     */     
/* 149 */     super.drawScreen(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void drawImage(ResourceLocation image, int x, int y, int width, int height, float alpha) {
/* 155 */     GlStateManager.enableAlpha();
/* 156 */     GlStateManager.enableBlend();
/* 157 */     GlStateManager.blendFunc(770, 771);
/* 158 */     Minecraft.getMinecraft().getTextureManager().bindTexture(image);
/* 159 */     GlStateManager.color(1.0F, 1.0F, 1.0F, alpha);
/* 160 */     GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
/* 161 */     GlStateManager.disableBlend();
/* 162 */     GlStateManager.disableAlpha();
/*     */   }
/*     */   
/*     */   public int getStringHeight(String string) {
/* 166 */     return (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT;
/*     */   }
/*     */   
/*     */   public int getStringWidth(String string) {
/* 170 */     return (Minecraft.getMinecraft()).fontRenderer.getStringWidth(string);
/*     */   }
/*     */   
/*     */   public void drawRectangle(int x, int y, int width, int height, int color) {
/* 174 */     GuiScreen.drawRect(x, y, x + width, y + height, color);
/*     */   }
/*     */   
/*     */   public void drawText(String text, int x, int y, int color) {
/* 178 */     (Minecraft.getMinecraft()).fontRenderer.drawString(text, x, y + 1, color);
/*     */   }
/*     */   
/*     */   public void drawVerticalLine(int x, int y, int height, int thickness, int color) {
/* 182 */     drawRect(x, y, thickness, height, color);
/*     */   }
/*     */   
/*     */   public void drawHorizontalLine(int x, int y, int width, int thickness, int color) {
/* 186 */     drawRect(x, y, width, thickness, color);
/*     */   }
/*     */   
/*     */   public void drawPixel(int x, int y, int color) {
/* 190 */     drawRect(x, y, 1, 1, color);
/*     */   }
/*     */   
/*     */   public void drawRainbowBar(int rainbowX, int rainbowY, int rainbowWidth, int rainbowHeight) {
/* 194 */     int colorSize = 6;
/* 195 */     drawGradientRect(rainbowX, rainbowY, rainbowX + rainbowWidth / colorSize, rainbowY + rainbowHeight, (new Color(104, 200, 119)).getRGB(), (new Color(122, 189, 216)).getRGB());
/* 196 */     drawGradientRect(rainbowX + rainbowWidth / colorSize, rainbowY, rainbowX + rainbowWidth / colorSize * 2, rainbowY + rainbowHeight, (new Color(122, 189, 216)).getRGB(), (new Color(45, 114, 185)).getRGB());
/* 197 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 2, rainbowY, rainbowX + rainbowWidth / colorSize * 3, rainbowY + rainbowHeight, (new Color(45, 114, 185)).getRGB(), (new Color(87, 157, 187)).getRGB());
/* 198 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 3, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 4, rainbowY + rainbowHeight, (new Color(87, 157, 187)).getRGB(), (new Color(98, 111, 164)).getRGB());
/* 199 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 4, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 5, rainbowY + rainbowHeight, (new Color(98, 111, 164)).getRGB(), (new Color(145, 82, 135)).getRGB());
/* 200 */     drawGradientRect(rainbowX + rainbowWidth / colorSize * 5, rainbowY, rainbowWidth - rainbowWidth / colorSize * colorSize + rainbowX + rainbowWidth / colorSize * 6, rainbowY + rainbowHeight, (new Color(145, 82, 135)).getRGB(), (new Color(187, 75, 100)).getRGB());
/*     */   }
/*     */ 
/*     */   
/*     */   public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
/* 205 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/* 206 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/* 207 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/* 208 */     float f3 = (startColor & 0xFF) / 255.0F;
/* 209 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/* 210 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/* 211 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/* 212 */     float f7 = (endColor & 0xFF) / 255.0F;
/* 213 */     GlStateManager.disableTexture2D();
/* 214 */     GlStateManager.enableBlend();
/* 215 */     GlStateManager.disableAlpha();
/* 216 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 217 */     GlStateManager.shadeModel(7425);
/* 218 */     Tessellator tessellator = Tessellator.getInstance();
/* 219 */     WorldRenderer bufferbuilder = tessellator.getBuffer();
/* 220 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 221 */     bufferbuilder.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 222 */     bufferbuilder.pos(right, top, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 223 */     bufferbuilder.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
/* 224 */     bufferbuilder.pos(left, bottom, 0.0D).color(f1, f2, f3, f).endVertex();
/* 225 */     tessellator.draw();
/* 226 */     GlStateManager.shadeModel(7424);
/* 227 */     GlStateManager.disableBlend();
/* 228 */     GlStateManager.enableAlpha();
/* 229 */     GlStateManager.enableTexture2D();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\CustomMainMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */