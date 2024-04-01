/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class FovStatic
/*    */   extends Feature {
/*    */   MenuLabel amountLabel;
/*    */   public MenuSlider amount;
/*    */   
/*    */   public FovStatic(Minecraft minecraft) {
/* 17 */     super(minecraft, "fovstatic", -1, Category.PAGE_7);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 22 */     this.amountLabel = new MenuLabel("Fov static speed", 0, 0);
/* 23 */     this.amount = new MenuSlider(this.mc.gameSettings.fovSetting, 1.0F, 150.0F, 1, 0, 0, 100, 6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 28 */     components.add(this.amountLabel);
/* 29 */     components.add(this.amount);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FovStatic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */