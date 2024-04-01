/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ public enum ButtonState {
/*  4 */   NORMAL(""), HOVER("Hover"), ACTIVE("Active"), POPUP("Popup"), DISABLED("Disabled"), HOVERACTIVE("HoverActive");
/*    */   String state;
/*    */   
/*    */   ButtonState(String state) {
/*  8 */     this.state = state;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 13 */     return this.state;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\ButtonState.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */