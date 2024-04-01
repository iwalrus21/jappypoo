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
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AutoSmokeBomb
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel atHeartsLabel;
/*     */   MenuSlider atHearts;
/*     */   long lastAction;
/*     */   int usesLeft;
/*  27 */   int nerf = 0;
/*  28 */   int prevSlot = -1;
/*     */   
/*     */   public AutoSmokeBomb(Minecraft minecraft) {
/*  31 */     super(minecraft, "autosmokebomb", 0, Category.PAGE_7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  36 */     this.atHeartsLabel = new MenuLabel("Auto smoke bomb at hearts", 0, 0);
/*  37 */     this.atHearts = new MenuSlider(1, 1, 20, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  42 */     components.add(this.atHeartsLabel);
/*  43 */     components.add(this.atHearts);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  48 */     this.usesLeft = 5;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  53 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  61 */     if (this.mc.player.getHealth() > this.atHearts.getValue()) {
/*  62 */       this.usesLeft = 5;
/*     */       
/*     */       return;
/*     */     } 
/*  66 */     if (this.mc.player.capabilities.isCreativeMode) {
/*     */       return;
/*     */     }
/*  69 */     if (this.nerf >= 2) {
/*  70 */       organizeInventory();
/*     */     } else {
/*  72 */       this.nerf++;
/*     */     } 
/*  74 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/*  75 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemAxe) {
/*  76 */         if (this.usesLeft > 0) {
/*  77 */           this.usesLeft--;
/*  78 */           this.prevSlot = this.mc.player.inventory.currentItem;
/*  79 */           this.mc.player.inventory.currentItem = i;
/*     */           
/*  81 */           this.mc.player.dropItem(false);
/*  82 */           this.lastAction = System.currentTimeMillis();
/*     */           
/*  84 */           this.mc.player.inventory.currentItem = this.prevSlot;
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void organizeInventory() {
/*  92 */     this.nerf = 0;
/*  93 */     boolean somethingfound = false;
/*  94 */     for (int i = 9; i <= 35; i++) {
/*  95 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() instanceof net.minecraft.item.ItemAxe) {
/*  96 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/*  97 */         somethingfound = true;
/*     */         
/*     */         break;
/*     */       } 
/* 101 */       if (somethingfound)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoSmokeBomb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */