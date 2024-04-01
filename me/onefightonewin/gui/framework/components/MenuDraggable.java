/*    */ package me.onefightonewin.gui.framework.components;
/*    */ 
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.MenuPriority;
/*    */ import org.lwjgl.input.Mouse;
/*    */ 
/*    */ public class MenuDraggable
/*    */   extends MenuComponent {
/*    */   boolean mouseDown = false;
/*    */   boolean dragging = false;
/*    */   boolean wantToDrag = false;
/* 12 */   int xSaved = -1;
/* 13 */   int ySaved = -1;
/* 14 */   int lastX = -1;
/* 15 */   int lastY = -1;
/*    */   
/*    */   public MenuDraggable(int x, int y, int width, int height) {
/* 18 */     super(x, y, width, height);
/* 19 */     setPriority(MenuPriority.LOWEST);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMouseClick(int button) {
/* 24 */     if (button == 0) {
/* 25 */       this.mouseDown = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void onMouseClickMove(int button) {
/* 30 */     if (button == 0) {
/* 31 */       this.dragging = true;
/*    */     }
/*    */   }
/*    */   
/*    */   public void onRender() {
/* 36 */     int x = getRenderX();
/* 37 */     int y = getRenderY();
/* 38 */     int mouseX = this.parent.getMouseX();
/* 39 */     int mouseY = this.parent.getMouseY();
/*    */     
/* 41 */     if (!this.disabled && 
/* 42 */       mouseX >= x && mouseX <= x + this.width && 
/* 43 */       mouseY >= y && mouseY <= y + this.height && 
/* 44 */       this.mouseDown && !this.wantToDrag) {
/* 45 */       this.wantToDrag = true;
/* 46 */       this.xSaved = this.parent.getX() - mouseX;
/* 47 */       this.ySaved = this.parent.getY() - mouseY;
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 53 */     if ((this.xSaved != x || this.ySaved != y) && this.xSaved != -1 && this.ySaved != -1 && (
/* 54 */       this.lastX != mouseX || this.lastY != mouseY)) {
/* 55 */       this.lastX = mouseX;
/* 56 */       this.lastY = mouseY;
/* 57 */       onAction();
/* 58 */       getParent().setLocation(mouseX + this.xSaved, mouseY + this.ySaved);
/*    */     } 
/*    */ 
/*    */     
/* 62 */     if (this.wantToDrag) {
/* 63 */       this.dragging = Mouse.isButtonDown(0);
/* 64 */       this.wantToDrag = this.dragging;
/* 65 */       if (!this.wantToDrag) {
/* 66 */         this.xSaved = -1;
/* 67 */         this.ySaved = -1;
/*    */       } 
/*    */     } 
/* 70 */     this.mouseDown = false;
/*    */   }
/*    */   
/*    */   public void onAction() {}
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\MenuDraggable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */