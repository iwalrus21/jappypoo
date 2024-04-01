/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ public enum MenuPriority {
/*  4 */   LOWEST(0), LOW(1), MEDIUM(2), HIGH(3), HIGHEST(4);
/*    */   int priority;
/*    */   
/*    */   MenuPriority(int priority) {
/*  8 */     this.priority = priority;
/*    */   }
/*    */   
/*    */   public int getPriority() {
/* 12 */     return this.priority;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\MenuPriority.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */