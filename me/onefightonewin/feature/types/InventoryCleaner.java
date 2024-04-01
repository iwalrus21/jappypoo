/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.concurrent.CopyOnWriteArrayList;
/*    */ import me.onefightonewin.AntiAFK;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InventoryCleaner
/*    */   extends Feature
/*    */ {
/*    */   public List<Integer> trashItems;
/* 20 */   int nerf = 0;
/* 21 */   int prevSlot = -1;
/*    */   
/*    */   public InventoryCleaner(Minecraft minecraft) {
/* 24 */     super(minecraft, "inventorycleaner", 0, Category.PAGE_5);
/* 25 */     this.trashItems = new CopyOnWriteArrayList<>();
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 30 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*    */       return;
/*    */     }
/*    */     
/* 38 */     if (this.mc.player.capabilities.isCreativeMode) {
/*    */       return;
/*    */     }
/* 41 */     if (this.nerf >= 2) {
/* 42 */       organizeInventory();
/*    */     } else {
/* 44 */       this.nerf++;
/*    */     } 
/* 46 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/* 47 */       if (this.mc.player.inventory.getStackInSlot(i) != null && isTrash(this.mc.player.inventory.getStackInSlot(i).getItem())) {
/* 48 */         this.prevSlot = this.mc.player.inventory.currentItem;
/* 49 */         this.mc.player.inventory.currentItem = i;
/*    */         
/* 51 */         this.mc.player.dropItem(false);
/*    */         
/* 53 */         this.mc.player.inventory.currentItem = this.prevSlot;
/*    */         break;
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isTrash(Item item) {
/* 60 */     return this.trashItems.contains(Integer.valueOf(Item.getIdFromItem(item)));
/*    */   }
/*    */   
/*    */   protected void organizeInventory() {
/* 64 */     this.nerf = 0;
/* 65 */     boolean somethingfound = false;
/* 66 */     for (int i = 9; i <= 35; i++) {
/* 67 */       if (this.mc.player.inventory.getStackInSlot(i) != null && isTrash(this.mc.player.inventory.getStackInSlot(i).getItem())) {
/* 68 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/* 69 */         somethingfound = true;
/*    */         
/*    */         break;
/*    */       } 
/* 73 */       if (somethingfound)
/*    */         break; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\InventoryCleaner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */