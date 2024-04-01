/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.Gui;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.renderer.Tessellator;
/*    */ import net.minecraft.client.renderer.WorldRenderer;
/*    */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public interface DrawImpl
/*    */ {
/*    */   default void drawImage(ResourceLocation image, int x, int y, int width, int height) {
/* 17 */     GlStateManager.enableAlpha();
/* 18 */     GlStateManager.enableBlend();
/* 19 */     GlStateManager.blendFunc(770, 771);
/* 20 */     Minecraft.getMinecraft().getTextureManager().bindTexture(image);
/* 21 */     GlStateManager.color(1.0F, 1.0F, 1.0F);
/* 22 */     GuiScreen.drawModalRectWithCustomSizedTexture(x, y, 0.0F, 0.0F, width, height, width, height);
/* 23 */     GlStateManager.disableBlend();
/* 24 */     GlStateManager.disableAlpha();
/*    */   }
/*    */   
/*    */   default int getStringHeight(String string) {
/* 28 */     return (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT;
/*    */   }
/*    */   
/*    */   default int getStringWidth(String string) {
/* 32 */     return (Minecraft.getMinecraft()).fontRenderer.getStringWidth(string);
/*    */   }
/*    */   
/*    */   default void drawRect(int x, int y, int width, int height, int color) {
/* 36 */     Gui.drawRect(x, y, x + width, y + height, color);
/*    */   }
/*    */   
/*    */   default void drawText(String text, int x, int y, int color) {
/* 40 */     if (AntiAFK.FONT_RENDERER == null) {
/* 41 */       (Minecraft.getMinecraft()).fontRenderer.drawStringWithShadow(text, x, (y + 1), color);
/*    */       
/*    */       return;
/*    */     } 
/* 45 */     AntiAFK.FONT_RENDERER.drawStringWithShadow(text, x, (y + 1), color);
/*    */   }
/*    */   
/*    */   default void drawVerticalLine(int x, int y, int height, int thickness, int color) {
/* 49 */     drawRect(x, y, thickness, height, color);
/*    */   }
/*    */   
/*    */   default void drawHorizontalLine(int x, int y, int width, int thickness, int color) {
/* 53 */     drawRect(x, y, width, thickness, color);
/*    */   }
/*    */   
/*    */   default void drawPixel(int x, int y, int color) {
/* 57 */     drawRect(x, y, 1, 1, color);
/*    */   }
/*    */   
/*    */   default void drawRainbowBar(int rainbowX, int rainbowY, int rainbowWidth, int rainbowHeight) {
/* 61 */     drawGradientRect(rainbowX, rainbowY, rainbowX + rainbowWidth / 4, rainbowY + rainbowHeight, (new Color(45, 135, 166)).getRGB(), (new Color(103, 93, 161)).getRGB());
/* 62 */     drawGradientRect(rainbowX + rainbowWidth / 4, rainbowY, rainbowX + rainbowWidth / 4 * 2, rainbowY + rainbowHeight, (new Color(103, 93, 161)).getRGB(), (new Color(156, 58, 154)).getRGB());
/* 63 */     drawGradientRect(rainbowX + rainbowWidth / 4 * 2, rainbowY, rainbowX + rainbowWidth / 4 * 3, rainbowY + rainbowHeight, (new Color(156, 58, 154)).getRGB(), (new Color(156, 120, 94)).getRGB());
/* 64 */     drawGradientRect(rainbowX + rainbowWidth / 4 * 3, rainbowY, rainbowX + rainbowWidth / 4 * 4, rainbowY + rainbowHeight, (new Color(156, 120, 94)).getRGB(), (new Color(156, 173, 45)).getRGB());
/*    */   }
/*    */ 
/*    */   
/*    */   default void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
/* 69 */     float f = (startColor >> 24 & 0xFF) / 255.0F;
/* 70 */     float f1 = (startColor >> 16 & 0xFF) / 255.0F;
/* 71 */     float f2 = (startColor >> 8 & 0xFF) / 255.0F;
/* 72 */     float f3 = (startColor & 0xFF) / 255.0F;
/* 73 */     float f4 = (endColor >> 24 & 0xFF) / 255.0F;
/* 74 */     float f5 = (endColor >> 16 & 0xFF) / 255.0F;
/* 75 */     float f6 = (endColor >> 8 & 0xFF) / 255.0F;
/* 76 */     float f7 = (endColor & 0xFF) / 255.0F;
/* 77 */     GlStateManager.disableTexture2D();
/* 78 */     GlStateManager.enableBlend();
/* 79 */     GlStateManager.disableAlpha();
/* 80 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 81 */     GlStateManager.shadeModel(7425);
/* 82 */     Tessellator tessellator = Tessellator.getInstance();
/* 83 */     WorldRenderer bufferbuilder = tessellator.getBuffer();
/* 84 */     bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 85 */     bufferbuilder.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 86 */     bufferbuilder.pos(right, top, 0.0D).color(f5, f6, f7, f4).endVertex();
/* 87 */     bufferbuilder.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
/* 88 */     bufferbuilder.pos(left, bottom, 0.0D).color(f1, f2, f3, f).endVertex();
/* 89 */     tessellator.draw();
/* 90 */     GlStateManager.shadeModel(7424);
/* 91 */     GlStateManager.disableBlend();
/* 92 */     GlStateManager.enableAlpha();
/* 93 */     GlStateManager.enableTexture2D();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\DrawImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */