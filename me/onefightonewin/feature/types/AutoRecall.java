/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.KeyBinding;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoRecall
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel atHeartsLabel;
/*     */   MenuSlider atHearts;
/*     */   int usesLeft;
/*  30 */   int nerf = 0;
/*  31 */   int prevSlot = -1;
/*     */   Item item;
/*     */   
/*     */   public AutoRecall(Minecraft minecraft) {
/*  35 */     super(minecraft, "autorecall", 0, Category.PAGE_7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  40 */     this.atHeartsLabel = new MenuLabel("Autorecall at hearts", 0, 0);
/*  41 */     this.atHearts = new MenuSlider(1, 1, 20, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  46 */     components.add(this.atHeartsLabel);
/*  47 */     components.add(this.atHearts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  52 */     this.item = Items.MUSHROOM_STEW;
/*  53 */     this.usesLeft = 5;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  58 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  62 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     if (this.mc.player.getHealth() > this.atHearts.getValue()) {
/*  67 */       this.usesLeft = 5;
/*     */       
/*     */       return;
/*     */     } 
/*  71 */     if (this.mc.player.capabilities.isCreativeMode) {
/*     */       return;
/*     */     }
/*  74 */     if (this.nerf >= 2) {
/*  75 */       organizeInventory();
/*     */     } else {
/*  77 */       this.nerf++;
/*     */     } 
/*  79 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/*  80 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
/*  81 */         if (this.usesLeft > 0) {
/*  82 */           this.usesLeft--;
/*     */           
/*  84 */           this.prevSlot = this.mc.player.inventory.currentItem;
/*  85 */           this.mc.player.inventory.currentItem = i;
/*     */           
/*  87 */           this.mc.player.dropItem(false);
/*     */           
/*  89 */           this.mc.player.inventory.currentItem = this.prevSlot;
/*     */         } 
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  95 */     boolean found = false;
/*     */     
/*  97 */     for (PotionEffect effect : this.mc.player.getActivePotionEffects()) {
/*  98 */       if (effect.func_76456_a() == 2) {
/*  99 */         found = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 105 */     if (found) {
/* 106 */       boolean hasWaterBottle = false;
/*     */       int j;
/* 108 */       for (j = 0; j < InventoryPlayer.getHotbarSize(); j++) {
/* 109 */         if (this.mc.player.inventory.getStackInSlot(j) != null && Item.getIdFromItem(this.mc.player.inventory.getStackInSlot(j).getItem()) == 373) {
/* 110 */           hasWaterBottle = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 115 */       if (!hasWaterBottle) {
/* 116 */         for (j = 0; j < InventoryPlayer.getHotbarSize(); j++) {
/* 117 */           if (this.mc.player.inventory.getStackInSlot(j) != null && (this.mc.player.inventory.getStackInSlot(j).getItem() instanceof net.minecraft.item.ItemAxe || this.mc.player.inventory.getStackInSlot(j).getItem() instanceof net.minecraft.item.ItemSword)) {
/* 118 */             this.prevSlot = this.mc.player.inventory.currentItem;
/* 119 */             this.mc.player.inventory.currentItem = j;
/*     */             
/* 121 */             KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), true);
/*     */             
/* 123 */             this.mc.player.dropItem(false);
/*     */             
/* 125 */             KeyBinding.setKeyBindState(this.mc.gameSettings.keyBindSneak.getKeyCode(), false);
/* 126 */             this.mc.player.inventory.currentItem = this.prevSlot;
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void organizeInventory() {
/* 135 */     this.nerf = 0;
/* 136 */     boolean somethingfound = false;
/* 137 */     for (int i = 9; i <= 35; i++) {
/* 138 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
/* 139 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/* 140 */         somethingfound = true;
/*     */         
/*     */         break;
/*     */       } 
/* 144 */       if (somethingfound)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoRecall.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */