/*    */ package me.onefightonewin.gui.framework.components;
/*    */ 
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.MenuPriority;
/*    */ 
/*    */ public class MenuDataObject extends MenuComponent {
/*    */   private String key;
/*    */   private String value;
/*    */   
/*    */   public MenuDataObject(String key, String value) {
/* 11 */     super(0, 0, 0, 0);
/* 12 */     setPriority(MenuPriority.LOWEST);
/* 13 */     this.key = key;
/* 14 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 18 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 22 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 26 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 30 */     this.value = value;
/*    */   }
/*    */   
/*    */   public void setValue(int value) {
/* 34 */     this.value = "" + value;
/*    */   }
/*    */   
/*    */   public boolean isValueInt() {
/*    */     try {
/* 39 */       Integer.valueOf(this.value);
/* 40 */       return true;
/* 41 */     } catch (NumberFormatException e) {
/* 42 */       return false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getIntValue() {
/* 47 */     return Integer.valueOf(this.value).intValue();
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuDataObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */