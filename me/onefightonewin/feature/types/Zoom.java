/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ 
/*    */ public class Zoom
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel amountLabel;
/*    */   public MenuSlider amount;
/*    */   public MenuCheckbox holdType;
/*    */   
/*    */   public Zoom(Minecraft minecraft) {
/* 21 */     super(minecraft, "zoom", 0, Category.PAGE_MISC);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 26 */     this.amountLabel = new MenuLabel("Zoom speed", 0, 0);
/* 27 */     this.amount = new MenuSlider(15.0F, 1.0F, 70.0F, 1, 0, 0, 100, 6);
/* 28 */     this.holdType = new MenuCheckbox("Zoom use on hold bind", 0, 0, 12, 12);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 33 */     components.add(this.amountLabel);
/* 34 */     components.add(this.amount);
/* 35 */     components.add(this.holdType);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Zoom.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */