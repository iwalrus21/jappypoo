/*    */ package me.onefightonewin.inventory;
/*    */ 
/*    */ import me.onefightonewin.feature.Feature;
/*    */ 
/*    */ public class InventoryAction {
/*    */   Feature feature;
/*    */   InventoryActionType type;
/*    */   int windowId;
/*    */   int slot;
/*    */   int newSlot;
/*    */   
/*    */   public InventoryAction(Feature feature, InventoryActionType type, int windowId, int slot, int newSlot) {
/* 13 */     this.feature = feature;
/* 14 */     this.type = type;
/* 15 */     this.windowId = windowId;
/* 16 */     this.slot = slot;
/* 17 */     this.newSlot = newSlot;
/*    */   }
/*    */   
/*    */   public Feature getFeature() {
/* 21 */     return this.feature;
/*    */   }
/*    */   
/*    */   public void setFeature(Feature feature) {
/* 25 */     this.feature = feature;
/*    */   }
/*    */   
/*    */   public InventoryActionType getType() {
/* 29 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(InventoryActionType type) {
/* 33 */     this.type = type;
/*    */   }
/*    */   
/*    */   public int getWindowId() {
/* 37 */     return this.windowId;
/*    */   }
/*    */   
/*    */   public void setWindowId(int windowId) {
/* 41 */     this.windowId = windowId;
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 45 */     return this.slot;
/*    */   }
/*    */   
/*    */   public void setSlot(int slot) {
/* 49 */     this.slot = slot;
/*    */   }
/*    */   
/*    */   public int getNewSlot() {
/* 53 */     return this.newSlot;
/*    */   }
/*    */   
/*    */   public void setNewSlot(int newSlot) {
/* 57 */     this.newSlot = newSlot;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\inventory\InventoryAction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */