/*    */ package me.onefightonewin.gui.framework.components.mainmenu;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import me.onefightonewin.gui.framework.ButtonState;
/*    */ import me.onefightonewin.gui.framework.DrawType;
/*    */ import me.onefightonewin.gui.framework.components.MenuButton;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ 
/*    */ public class MenuButtonMainMenuImage
/*    */   extends MenuButton {
/*    */   ResourceLocation location;
/*    */   
/*    */   public MenuButtonMainMenuImage(ResourceLocation resource, int x, int y) {
/* 14 */     super("", x, y);
/* 15 */     this.location = resource;
/*    */   }
/*    */   
/*    */   public MenuButtonMainMenuImage(ResourceLocation resource, int x, int y, int width, int height) {
/* 19 */     super("", x, y, width, height);
/* 20 */     this.location = resource;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onInitColors() {
/* 25 */     setColor(DrawType.BACKGROUND, ButtonState.NORMAL, new Color(255, 255, 255, 100));
/* 26 */     setColor(DrawType.BACKGROUND, ButtonState.ACTIVE, new Color(255, 255, 255, 100));
/* 27 */     setColor(DrawType.BACKGROUND, ButtonState.HOVER, new Color(200, 200, 200, 100));
/* 28 */     setColor(DrawType.BACKGROUND, ButtonState.HOVERACTIVE, new Color(225, 225, 225, 100));
/* 29 */     setColor(DrawType.BACKGROUND, ButtonState.DISABLED, new Color(255, 255, 255, 155));
/*    */     
/* 31 */     setColor(DrawType.LINE, ButtonState.NORMAL, new Color(255, 255, 255, 150));
/* 32 */     setColor(DrawType.LINE, ButtonState.ACTIVE, new Color(255, 255, 255, 150));
/* 33 */     setColor(DrawType.LINE, ButtonState.HOVER, new Color(200, 200, 200, 150));
/* 34 */     setColor(DrawType.LINE, ButtonState.HOVERACTIVE, new Color(225, 225, 225, 150));
/* 35 */     setColor(DrawType.LINE, ButtonState.DISABLED, new Color(255, 255, 255, 175));
/*    */     
/* 37 */     setColor(DrawType.TEXT, ButtonState.NORMAL, new Color(255, 255, 255, 255));
/* 38 */     setColor(DrawType.TEXT, ButtonState.ACTIVE, new Color(235, 235, 235, 255));
/* 39 */     setColor(DrawType.TEXT, ButtonState.HOVER, new Color(225, 225, 225, 255));
/* 40 */     setColor(DrawType.TEXT, ButtonState.HOVERACTIVE, new Color(235, 235, 235, 255));
/* 41 */     setColor(DrawType.TEXT, ButtonState.DISABLED, new Color(255, 255, 255, 255));
/*    */   }
/*    */ 
/*    */   
/*    */   public void onRender() {
/* 46 */     int x = getRenderX();
/* 47 */     int y = getRenderY();
/* 48 */     int width = (this.width == -1 && this.height == -1) ? (getStringWidth(this.text) + this.minOffset * 2) : this.width;
/* 49 */     int height = (this.width == -1 && this.height == -1) ? (getStringHeight(this.text) + this.minOffset * 2) : this.height;
/* 50 */     int mouseX = this.parent.getMouseX();
/* 51 */     int mouseY = this.parent.getMouseY();
/*    */     
/* 53 */     ButtonState state = this.active ? ButtonState.ACTIVE : ButtonState.NORMAL;
/*    */     
/* 55 */     if (!this.disabled) {
/* 56 */       if (mouseX >= x && mouseX <= x + width && 
/* 57 */         mouseY >= y && mouseY <= y + height + 1) {
/* 58 */         state = ButtonState.HOVER;
/*    */         
/* 60 */         if (this.mouseDown) {
/* 61 */           this.active = !this.active;
/* 62 */           onAction();
/*    */         } 
/*    */       } 
/*    */     } else {
/*    */       
/* 67 */       state = ButtonState.DISABLED;
/*    */     } 
/*    */     
/* 70 */     int backgroundColor = getColor(DrawType.BACKGROUND, state);
/* 71 */     int lineColor = getColor(DrawType.LINE, state);
/*    */     
/* 73 */     drawRect(x + 1, y + 1, width - 1, height - 1, backgroundColor);
/* 74 */     drawHorizontalLine(x, y, width + 1, 1, lineColor);
/* 75 */     drawVerticalLine(x, y + 1, height - 1, 1, lineColor);
/* 76 */     drawHorizontalLine(x, y + height, width + 1, 1, lineColor);
/* 77 */     drawVerticalLine(x + width, y + 1, height - 1, 1, lineColor);
/* 78 */     drawImage(this.location, x + 6, y + 6, this.width - 10, this.height - 10);
/* 79 */     this.mouseDown = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void onAction() {
/* 84 */     setActive(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\components\mainmenu\MenuButtonMainMenuImage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */