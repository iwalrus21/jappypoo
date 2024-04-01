/*    */ package me.onefightonewin.gui.altmanager;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiListExtended;
/*    */ 
/*    */ public class GuiAltEntry
/*    */   implements GuiListExtended.IGuiListEntry {
/*    */   GuiAltList list;
/*    */   String string;
/*    */   int stringWidth;
/*    */   
/*    */   public GuiAltEntry(GuiAltList list, String string) {
/* 13 */     this.list = list;
/* 14 */     this.string = string;
/* 15 */     this.stringWidth = (Minecraft.getMinecraft()).fontRenderer.getStringWidth(string);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void func_178011_a(int p_178011_1_, int p_178011_2_, int p_178011_3_) {}
/*    */ 
/*    */   
/*    */   public void func_180790_a(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected_) {
/* 24 */     (Minecraft.getMinecraft()).fontRenderer.drawString(this.string, (Minecraft.getMinecraft()).currentScreen.width / 2 - this.stringWidth / 2, y + slotHeight / 2 - (Minecraft.getMinecraft()).fontRenderer.FONT_HEIGHT / 2 - 1, 16777215);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mousePressed(int slotIndex, int mouseX, int mouseY, int mouseEvent, int relativeX, int relativeY) {
/* 29 */     if (this.list != null) {
/* 30 */       this.list.setIndex(slotIndex);
/*    */     }
/* 32 */     return false;
/*    */   }
/*    */   
/*    */   public void mouseReleased(int slotIndex, int x, int y, int mouseEvent, int relativeX, int relativeY) {}
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\altmanager\GuiAltEntry.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */