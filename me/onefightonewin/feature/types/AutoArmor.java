/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoArmor
/*     */   extends Feature
/*     */ {
/*     */   MenuCheckbox onlyInInventory;
/*  22 */   int nerf = 0;
/*     */   int type;
/*     */   
/*     */   public AutoArmor(Minecraft minecraft) {
/*  26 */     super(minecraft, "autoarmor", 0, Category.PAGE_5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  31 */     this.onlyInInventory = new MenuCheckbox("Only in inventory", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  36 */     components.add(this.onlyInInventory);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  41 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  45 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/*  48 */     if (this.onlyInInventory.getRValue() && !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiContainerCreative) && !(this.mc.currentScreen instanceof net.minecraft.client.gui.inventory.GuiInventory)) {
/*     */       return;
/*     */     }
/*     */     
/*  52 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */       return;
/*     */     }
/*  55 */     setBestArmorPart(this.type);
/*     */     
/*  57 */     if (this.type >= 3) {
/*  58 */       this.type = 0;
/*     */     } else {
/*  60 */       this.type++;
/*     */     } 
/*     */   }
/*     */   public void setBestArmorPart(int type) {
/*  64 */     int reduceAmount = 0;
/*  65 */     int slot = -1;
/*     */     
/*  67 */     if (this.mc.player.func_82169_q(3 - type) != null && this.mc.player.func_82169_q(3 - type).getItem() instanceof ItemArmor) {
/*  68 */       reduceAmount = ((ItemArmor)this.mc.player.func_82169_q(3 - type).getItem()).damageReduceAmount;
/*     */     }
/*     */     
/*  71 */     for (int i = 0; i < 36; i++) {
/*  72 */       if (this.mc.player.inventory.getStackInSlot(i) != null) {
/*     */ 
/*     */ 
/*     */         
/*  76 */         Item item = this.mc.player.inventory.getStackInSlot(i).getItem();
/*     */         
/*  78 */         if (item instanceof ItemArmor && ((ItemArmor)item).armorType == type) {
/*  79 */           int damageReduceAmount = ((ItemArmor)item).damageReduceAmount;
/*     */           
/*  81 */           if (damageReduceAmount > reduceAmount) {
/*  82 */             reduceAmount = damageReduceAmount;
/*  83 */             slot = i;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*  88 */     if (slot == -1) {
/*     */       return;
/*     */     }
/*  91 */     if (slot < 9) {
/*  92 */       slot += 36;
/*     */     }
/*  94 */     int newSlot = this.type;
/*     */     
/*  96 */     if (type == 0) {
/*  97 */       newSlot = 5;
/*  98 */     } else if (type == 1) {
/*  99 */       newSlot = 6;
/* 100 */     } else if (type == 2) {
/* 101 */       newSlot = 7;
/* 102 */     } else if (type == 3) {
/* 103 */       newSlot = 8;
/*     */     } 
/*     */     
/* 106 */     (AntiAFK.getInstance()).inventoryManager.moveItem(this, this.mc.player.inventoryContainer.windowId, slot, newSlot);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoArmor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */