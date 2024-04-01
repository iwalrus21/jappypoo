/*    */ package me.onefightonewin.gui.altmanager;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiListExtended;
/*    */ 
/*    */ public class GuiAltList
/*    */   extends GuiListExtended {
/*    */   protected final List<GuiAltEntry> accounts;
/*    */   int index;
/*    */   
/*    */   public GuiAltList(Minecraft mcIn, int widthIn, int heightIn, List<GuiAltEntry> accounts) {
/* 13 */     super(mcIn, widthIn, heightIn, 32, heightIn - 55 + 4, 22);
/* 14 */     this.accounts = accounts;
/* 15 */     this.index = -1;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isSelected(int slotIndex) {
/* 20 */     return (this.index == slotIndex);
/*    */   }
/*    */   
/*    */   public void setIndex(int index) {
/* 24 */     this.index = index;
/*    */   }
/*    */   
/*    */   public int getIndex() {
/* 28 */     return this.index;
/*    */   }
/*    */ 
/*    */   
/*    */   public GuiListExtended.IGuiListEntry getListEntry(int index) {
/* 33 */     return this.accounts.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getSize() {
/* 38 */     return this.accounts.size();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\altmanager\GuiAltList.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */