/*    */ package me.onefightonewin.gui.framework.components.mainmenu;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.onefightonewin.gui.framework.ButtonState;
/*    */ import me.onefightonewin.gui.framework.DrawType;
/*    */ import me.onefightonewin.gui.framework.components.MenuButton;
/*    */ 
/*    */ public class MenuButtonMainMenu
/*    */   extends MenuButton {
/*    */   public MenuButtonMainMenu(String text, int x, int y) {
/* 11 */     super(text, x, y);
/*    */   }
/*    */   
/*    */   public MenuButtonMainMenu(String text, int x, int y, int width, int height) {
/* 15 */     super(text, x, y, width, height);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onInitColors() {
/* 20 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(255, 255, 255, 100));
/* 21 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(255, 255, 255, 100));
/* 22 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(200, 200, 200, 100));
/* 23 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(225, 225, 225, 100));
/* 24 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(255, 255, 255, 155));
/*    */     
/* 26 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(255, 255, 255, 150));
/* 27 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(255, 255, 255, 150));
/* 28 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(200, 200, 200, 150));
/* 29 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(225, 225, 225, 150));
/* 30 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(255, 255, 255, 175));
/*    */     
/* 32 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, 255));
/* 33 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/* 34 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/* 35 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/* 36 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onAction() {
/* 41 */     setActive(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\mainmenu\MenuButtonMainMenu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */