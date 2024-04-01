/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.gui.ScaledResolution;
/*    */ import net.minecraft.client.renderer.GlStateManager;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ 
/*    */ public class MinecraftMenuImpl
/*    */   extends GuiScreen
/*    */ {
/*    */   protected Menu menu;
/*    */   protected KeyBinding keybinding;
/*    */   protected boolean ready = false;
/* 17 */   protected float guiScale = 2.0F;
/*    */   
/*    */   public MinecraftMenuImpl(float guiScale, Menu menu, KeyBinding keybinding) {
/* 20 */     this.guiScale = guiScale;
/* 21 */     this.menu = menu;
/* 22 */     this.keybinding = keybinding;
/*    */   }
/*    */ 
/*    */   
/*    */   public void drawScreen(int mouseX, int mouseY, float partialTicks) {
/* 27 */     GlStateManager.pushMatrix();
/* 28 */     float value = this.guiScale / (new ScaledResolution(this.mc)).getScaleFactor();
/* 29 */     GlStateManager.scale(value, value, value);
/* 30 */     this.menu.onRender(Math.round(mouseX / value), Math.round(mouseY / value));
/* 31 */     GlStateManager.popMatrix();
/*    */     
/* 33 */     onMouseScroll(Mouse.getDWheel());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
/* 38 */     this.menu.onMouseClick(mouseButton);
/*    */   }
/*    */   
/*    */   protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
/* 42 */     this.menu.onMouseClickMove(clickedMouseButton);
/*    */   }
/*    */   
/*    */   public void onMouseScroll(int scroll) {
/* 46 */     this.menu.onScroll(scroll);
/*    */   }
/*    */ 
/*    */   
/*    */   public void keyTyped(char typedChar, int keyCode) throws IOException {
/* 51 */     if (keyCode == 1 || (this.keybinding != null && this.keybinding.getKeyCode() == keyCode)) {
/* 52 */       if (this.menu.onMenuExit(keyCode)) {
/*    */         return;
/*    */       }
/* 55 */       this.mc.displayGuiScreen(null);
/*    */     } else {
/* 57 */       this.menu.onKeyDown(typedChar, keyCode);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean doesGuiPauseGame() {
/* 63 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGuiClosed() {
/* 68 */     this.ready = false;
/* 69 */     super.onGuiClosed();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\MinecraftMenuImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */