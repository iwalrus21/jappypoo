/*    */ package me.onefightonewin.gui.framework;
/*    */ 
/*    */ public enum DrawType {
/*  4 */   LINE("line"), BACKGROUND("background"), TEXT("text");
/*    */   String type;
/*    */   
/*    */   DrawType(String type) {
/*  8 */     this.type = type;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 13 */     return this.type;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\DrawType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */