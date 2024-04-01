/*    */ package me.onefightonewin.inventory;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ public class InventoryManager
/*    */ {
/*    */   private List<InventoryAction> actions;
/*    */   private Minecraft mc;
/* 14 */   private int lastDelay = 0;
/* 15 */   public int minDelay = 5;
/*    */   
/*    */   public InventoryManager() {
/* 18 */     this.mc = Minecraft.getMinecraft();
/* 19 */     this.actions = new ArrayList<>();
/*    */   }
/*    */   
/*    */   public void runHandler() {
/* 23 */     if (this.lastDelay < this.minDelay) {
/* 24 */       this.lastDelay++;
/*    */       return;
/*    */     } 
/* 27 */     this.lastDelay = 0;
/*    */ 
/*    */     
/* 30 */     if (!this.actions.isEmpty()) {
/* 31 */       ItemStack stack; InventoryAction action = this.actions.get(0);
/*    */       
/* 33 */       if (action.getFeature() != null && !action.getFeature().isEnabled()) {
/* 34 */         this.actions.remove(action);
/* 35 */         this.lastDelay = this.minDelay;
/* 36 */         runHandler();
/*    */         
/*    */         return;
/*    */       } 
/* 40 */       switch (action.getType()) {
/*    */         case MOVE:
/* 42 */           stack = null;
/*    */           
/* 44 */           if (action.getFeature() instanceof me.onefightonewin.feature.types.AutoArmor && action.getWindowId() == this.mc.player.inventoryContainer.windowId && (action.getNewSlot() >= 5 || action.getNewSlot() <= 8)) {
/* 45 */             stack = this.mc.player.func_82169_q(3 - action.getNewSlot() - 5);
/*    */           } else {
/* 47 */             stack = this.mc.player.inventory.getStackInSlot(action.getNewSlot());
/*    */           } 
/*    */ 
/*    */           
/* 51 */           if (stack != null && stack.getItem() != null) {
/* 52 */             this.mc.playerController.func_78753_a(action.getWindowId(), action.getSlot(), 0, 0, (EntityPlayer)this.mc.player);
/* 53 */             this.mc.playerController.func_78753_a(action.getWindowId(), action.getNewSlot(), 0, 0, (EntityPlayer)this.mc.player);
/* 54 */             this.mc.playerController.func_78753_a(action.getWindowId(), action.getSlot(), 0, 0, (EntityPlayer)this.mc.player); break;
/*    */           } 
/* 56 */           this.mc.playerController.func_78753_a(action.getWindowId(), action.getSlot(), 0, 0, (EntityPlayer)this.mc.player);
/* 57 */           this.mc.playerController.func_78753_a(action.getWindowId(), action.getNewSlot(), 0, 0, (EntityPlayer)this.mc.player);
/*    */           break;
/*    */ 
/*    */         
/*    */         case QUICKMOVE:
/* 62 */           this.mc.playerController.func_78753_a(action.getWindowId(), action.getSlot(), 0, 1, (EntityPlayer)this.mc.player);
/*    */           break;
/*    */       } 
/* 65 */       this.actions.remove(action);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void moveItem(Feature feature, int windowId, int slot, int newSlot) {
/* 70 */     this.actions.add(new InventoryAction(feature, InventoryActionType.MOVE, windowId, slot, newSlot));
/*    */   }
/*    */   
/*    */   public void quickMove(Feature feature, int windowId, int slot) {
/* 74 */     this.actions.add(new InventoryAction(feature, InventoryActionType.QUICKMOVE, windowId, slot, -1));
/*    */   }
/*    */   
/*    */   public boolean hasPendingActions(Feature feature) {
/* 78 */     for (InventoryAction action : this.actions) {
/* 79 */       if (action.getFeature() == feature)
/* 80 */         return true; 
/*    */     } 
/* 82 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\inventory\InventoryManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */