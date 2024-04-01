/*     */ package me.onefightonewin.gui.framework;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class Menu
/*     */ {
/*     */   private String title;
/*     */   private int x;
/*     */   private int y;
/*     */   private int width;
/*     */   private int height;
/*     */   private int mouseX;
/*     */   private int mouseY;
/*     */   private List<MenuComponent> components;
/*     */   
/*     */   public Menu(String title, int width, int height) {
/*  21 */     this.title = title;
/*  22 */     this.x = 0;
/*  23 */     this.y = 0;
/*  24 */     this.width = width;
/*  25 */     this.height = height;
/*  26 */     this.components = new ArrayList<>();
/*     */   }
/*     */   
/*     */   public void onRender(int mouseX, int mouseY) {
/*  30 */     this.mouseX = mouseX;
/*  31 */     this.mouseY = mouseY;
/*     */     
/*  33 */     for (MenuComponent component : this.components) {
/*  34 */       component.onPreSort();
/*     */     }
/*     */     
/*  37 */     Collections.sort(this.components, new Comparator<MenuComponent>()
/*     */         {
/*     */           public int compare(MenuComponent a, MenuComponent b) {
/*  40 */             return Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority());
/*     */           }
/*     */         });
/*  43 */     Collections.reverse(this.components);
/*     */     
/*  45 */     int passThroughIndex = -1;
/*  46 */     int index = this.components.size();
/*     */     
/*  48 */     for (MenuComponent component : this.components) {
/*  49 */       component.setRenderOffsetX(this.x);
/*  50 */       component.setRenderOffsetY(this.y);
/*     */       
/*  52 */       if (!component.passesThrough() && passThroughIndex == -1) {
/*  53 */         passThroughIndex = index;
/*     */       }
/*  55 */       index--;
/*     */     } 
/*     */     
/*  58 */     Collections.reverse(this.components);
/*     */     
/*  60 */     int oldIndex = index;
/*     */     
/*  62 */     this.mouseX = Integer.MAX_VALUE;
/*  63 */     this.mouseY = Integer.MAX_VALUE;
/*     */     
/*  65 */     index = oldIndex;
/*     */     
/*  67 */     for (MenuComponent component : this.components) {
/*  68 */       if (index >= passThroughIndex - 1) {
/*  69 */         this.mouseX = mouseX;
/*  70 */         this.mouseY = mouseY;
/*  71 */       } else if (component instanceof me.onefightonewin.gui.framework.components.MenuDraggable) {
/*  72 */         index++;
/*     */         
/*     */         continue;
/*     */       } 
/*  76 */       component.onRender();
/*  77 */       index++;
/*     */     } 
/*     */     
/*  80 */     this.mouseX = mouseX;
/*  81 */     this.mouseY = mouseY;
/*     */   }
/*     */   
/*     */   public void onMouseClick(int button) {
/*  85 */     Collections.sort(this.components, new Comparator<MenuComponent>()
/*     */         {
/*     */           public int compare(MenuComponent a, MenuComponent b) {
/*  88 */             return Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority());
/*     */           }
/*     */         });
/*     */     
/*  92 */     Collections.reverse(this.components);
/*     */     
/*  94 */     for (MenuComponent component : this.components) {
/*  95 */       component.onMouseClick(button);
/*     */       
/*  97 */       if (!component.passesThrough())
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void onMouseClickMove(int button) {
/* 103 */     for (MenuComponent component : this.components) {
/* 104 */       component.onMouseClickMove(button);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onKeyDown(char character, int key) {
/* 109 */     for (MenuComponent component : this.components)
/* 110 */       component.onKeyDown(character, key); 
/*     */   }
/*     */   
/*     */   public boolean onMenuExit(int key) {
/* 114 */     boolean cancel = false;
/* 115 */     for (MenuComponent component : this.components) {
/* 116 */       if (component.onExitGui(key))
/* 117 */         cancel = true; 
/* 118 */     }  return cancel;
/*     */   }
/*     */   
/*     */   public void onScroll(int scroll) {
/* 122 */     Collections.sort(this.components, new Comparator<MenuComponent>()
/*     */         {
/*     */           public int compare(MenuComponent a, MenuComponent b) {
/* 125 */             return Integer.compare(a.getPriority().getPriority(), b.getPriority().getPriority());
/*     */           }
/*     */         });
/*     */     
/* 129 */     Collections.reverse(this.components);
/*     */     
/* 131 */     for (MenuComponent component : this.components) {
/* 132 */       component.onMouseScroll(scroll);
/*     */       
/* 134 */       if (!component.passesThrough())
/*     */         break; 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setLocation(int x, int y) {
/* 140 */     this.x = x;
/* 141 */     this.y = y;
/*     */   }
/*     */   
/*     */   public void addComponent(MenuComponent component) {
/* 145 */     component.setParent(this);
/* 146 */     this.components.add(component);
/*     */   }
/*     */   
/*     */   public String getTitle() {
/* 150 */     return this.title;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 154 */     this.title = title;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 158 */     return this.x;
/*     */   }
/*     */   
/*     */   public void setX(int x) {
/* 162 */     this.x = x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 166 */     return this.y;
/*     */   }
/*     */   
/*     */   public void setY(int y) {
/* 170 */     this.y = y;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 174 */     return this.width;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 178 */     this.width = width;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 182 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 186 */     this.height = height;
/*     */   }
/*     */   
/*     */   public int getMouseX() {
/* 190 */     return this.mouseX;
/*     */   }
/*     */   
/*     */   public void setMouseX(int mouseX) {
/* 194 */     this.mouseX = mouseX;
/*     */   }
/*     */   
/*     */   public int getMouseY() {
/* 198 */     return this.mouseY;
/*     */   }
/*     */   
/*     */   public void setMouseY(int mouseY) {
/* 202 */     this.mouseY = mouseY;
/*     */   }
/*     */   
/*     */   public List<MenuComponent> getComponents() {
/* 206 */     return this.components;
/*     */   }
/*     */   
/*     */   public void setComponents(List<MenuComponent> components) {
/* 210 */     this.components = components;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\gui\framework\Menu.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */