/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*    */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class BlockOverlay
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel colorLabel;
/*    */   public MenuColorPicker color;
/*    */   public MenuCheckbox opaqueBlock;
/*    */   
/*    */   public BlockOverlay(Minecraft minecraft) {
/* 21 */     super(minecraft, "blockoverlay", -1, Category.PAGE_6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 26 */     this.colorLabel = new MenuLabel("Block Overlay Color", 0, 0);
/* 27 */     this.color = new MenuColorPicker(0, 0, 20, 10, Color.GREEN.getRGB());
/* 28 */     this.opaqueBlock = new MenuCheckbox("Opaque block", 0, 0, 12, 12);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 33 */     components.add(this.colorLabel);
/* 34 */     components.add(this.color);
/* 35 */     components.add(this.opaqueBlock);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\BlockOverlay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */