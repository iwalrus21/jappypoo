/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.HashMap;
/*    */ 
/*    */ public class MenuColorType {
/*    */   HashMap<String, Color> colors;
/*    */   
/*    */   public MenuColorType() {
/* 10 */     this.colors = new HashMap<>();
/* 11 */     onInitColors();
/*    */   }
/*    */   
/*    */   public int getColor(DrawType type, ButtonState state) {
/* 15 */     if (this.colors.containsKey(type.toString() + state.toString())) {
/* 16 */       return ((Color)this.colors.get(type.toString() + state.toString())).getRGB();
/*    */     }
/* 18 */     return ((Color)this.colors.get(type.toString())).getRGB();
/*    */   }
/*    */   
/*    */   public void setColor(DrawType type, ButtonState state, Color color) {
/* 22 */     this.colors.put(type.toString() + state.toString(), color);
/*    */   }
/*    */   
/*    */   public void onInitColors() {}
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\MenuColorType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */