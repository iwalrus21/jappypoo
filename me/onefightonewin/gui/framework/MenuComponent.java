/*     */ package me.onefightonewin.gui.framework;
/*     */ 
/*     */ public class MenuComponent extends MenuColorType implements DrawImpl {
/*     */   protected int x;
/*     */   protected int y;
/*     */   protected int renderOffsetX;
/*     */   protected int renderOffsetY;
/*     */   protected int width;
/*     */   protected int height;
/*     */   protected MenuPriority priority;
/*     */   protected boolean disabled;
/*     */   protected Menu parent;
/*     */   
/*     */   public MenuComponent(int x, int y, int width, int height) {
/*  15 */     this.x = x;
/*  16 */     this.y = y;
/*  17 */     this.renderOffsetX = 0;
/*  18 */     this.renderOffsetY = 0;
/*  19 */     this.width = width;
/*  20 */     this.height = height;
/*  21 */     this.priority = MenuPriority.MEDIUM;
/*  22 */     this.disabled = false;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/*  26 */     this.x = x;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/*  30 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getX() {
/*  34 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/*  38 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getRenderOffsetX() {
/*  42 */     return this.renderOffsetX;
/*     */   }
/*     */   
/*     */   public void setRenderOffsetX(int renderOffsetX) {
/*  46 */     this.renderOffsetX = renderOffsetX;
/*     */   }
/*     */   
/*     */   public int getRenderOffsetY() {
/*  50 */     return this.renderOffsetY;
/*     */   }
/*     */   
/*     */   public void setRenderOffsetY(int renderOffsetY) {
/*  54 */     this.renderOffsetY = renderOffsetY;
/*     */   }
/*     */   
/*     */   public int getRenderX() {
/*  58 */     return this.x + this.renderOffsetX;
/*     */   }
/*     */   
/*     */   public int getRenderY() {
/*  62 */     return this.y + this.renderOffsetY;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/*  66 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/*  70 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/*  74 */     this.width = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/*  78 */     this.height = height;
/*     */   }
/*     */   
/*     */   public boolean passesThrough() {
/*  82 */     return true;
/*     */   }
/*     */   
/*     */   public MenuPriority getPriority() {
/*  86 */     return this.priority;
/*     */   }
/*     */   
/*     */   public void setPriority(MenuPriority priority) {
/*  90 */     this.priority = priority;
/*     */   }
/*     */   
/*     */   public boolean isDisabled() {
/*  94 */     return this.disabled;
/*     */   }
/*     */   
/*     */   public void setDisabled(boolean disabled) {
/*  98 */     this.disabled = disabled;
/*     */   }
/*     */   
/*     */   public void setParent(Menu parent) {
/* 102 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public Menu getParent() {
/* 106 */     return this.parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onExitGui(int key) {
/* 111 */     return false;
/*     */   }
/*     */   
/*     */   public void onPreSort() {}
/*     */   
/*     */   public void onRender() {}
/*     */   
/*     */   public void onKeyDown(char character, int key) {}
/*     */   
/*     */   public void onMouseClick(int key) {}
/*     */   
/*     */   public void onMouseScroll(int scroll) {}
/*     */   
/*     */   public void onMouseClickMove(int key) {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\MenuComponent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */