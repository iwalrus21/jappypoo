/*    */ package me.onefightonewin.gui.framework.components;
/*    */ 
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.MenuPriority;
/*    */ 
/*    */ public class MenuNoDrag extends MenuComponent {
/*    */   boolean mouseDown = false;
/*    */   
/*    */   public MenuNoDrag(int x, int y, int width, int height) {
/* 10 */     super(x, y, width, height);
/* 11 */     setPriority(MenuPriority.LOW);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseClick(int button) {
/* 16 */     if (button == 0) {
/* 17 */       this.mouseDown = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean passesThrough() {
/* 22 */     if (this.disabled) {
/* 23 */       return true;
/*    */     }
/* 25 */     int x = getRenderX();
/* 26 */     int y = getRenderY();
/* 27 */     int mouseX = this.parent.getMouseX();
/* 28 */     int mouseY = this.parent.getMouseY();
/*    */     
/* 30 */     if (this.mouseDown && 
/* 31 */       mouseX >= x && mouseX <= x + this.width && 
/* 32 */       mouseY >= y && mouseY <= y + this.height) {
/* 33 */       return false;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 38 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender() {
/* 43 */     this.mouseDown = false;
/*    */   }
/*    */   
/*    */   public void onAction() {}
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuNoDrag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */