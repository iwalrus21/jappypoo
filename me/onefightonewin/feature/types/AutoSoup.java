/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ public class AutoSoup
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel delayMsLabel;
/*     */   MenuSlider delayMs;
/*     */   MenuLabel atHeartsLabel;
/*     */   MenuSlider atHearts;
/*     */   MenuCheckbox onMove;
/*     */   long lastAction;
/*  30 */   int nerf = 0;
/*  31 */   int prevSlot = -1;
/*     */   Item item;
/*     */   Item trash;
/*     */   
/*     */   public AutoSoup(Minecraft minecraft) {
/*  36 */     super(minecraft, "autosoup", 0, Category.PAGE_2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  41 */     this.delayMsLabel = new MenuLabel("Autosoup delay", 0, 0);
/*  42 */     this.delayMs = new MenuSlider(1.0F, 1.0F, 5.0F, 1, 0, 0, 100, 6);
/*  43 */     this.atHeartsLabel = new MenuLabel("Autosoup at hearts", 0, 0);
/*  44 */     this.atHearts = new MenuSlider(1, 1, 20, 0, 0, 100, 6);
/*  45 */     this.onMove = new MenuCheckbox("Autosoup - On move only", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  50 */     components.add(this.delayMsLabel);
/*  51 */     components.add(this.delayMs);
/*  52 */     components.add(this.atHeartsLabel);
/*  53 */     components.add(this.atHearts);
/*  54 */     components.add(this.onMove);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  59 */     this.item = Items.MUSHROOM_STEW;
/*  60 */     this.trash = Items.BOWL;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  65 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  69 */     if (this.onMove.getRValue() && this.mc.player.posX == this.mc.player.prevPosX && this.mc.player.posY == this.mc.player.prevPosY && this.mc.player.posZ == this.mc.player.prevPosZ) {
/*     */       return;
/*     */     }
/*     */     
/*  73 */     if (this.mc.gameSettings.keyBindAttack.isKeyDown() || this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */     
/*  77 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     if (this.mc.player.getHealth() > this.atHearts.getValue()) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     if (this.lastAction > System.currentTimeMillis()) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     if (this.mc.player.capabilities.isCreativeMode) {
/*     */       return;
/*     */     }
/*  92 */     if (this.nerf >= 2) {
/*  93 */       organizeInventory();
/*     */     } else {
/*  95 */       this.nerf++;
/*     */     } 
/*  97 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/*  98 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
/*  99 */         this.prevSlot = this.mc.player.inventory.currentItem;
/* 100 */         this.mc.player.inventory.currentItem = i;
/*     */         
/* 102 */         if (this.mc.playerController.func_78769_a((EntityPlayer)this.mc.player, (World)this.mc.world, this.mc.player.inventory.getStackInSlot(i))) {
/* 103 */           this.mc.entityRenderer.itemRenderer.func_78445_c();
/*     */         }
/*     */         
/* 106 */         this.lastAction = System.currentTimeMillis() + Math.round(this.delayMs.getValue() * 1000.0F);
/*     */         
/* 108 */         this.mc.player.inventory.currentItem = this.prevSlot;
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void organizeInventory() {
/* 115 */     this.nerf = 0;
/* 116 */     boolean somethingfound = false;
/* 117 */     for (int i = 9; i <= 35; i++) {
/* 118 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() == this.trash) {
/* 119 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/* 120 */         somethingfound = true;
/*     */         
/*     */         break;
/*     */       } 
/* 124 */       if (this.mc.player.inventory.getStackInSlot(i) != null && this.mc.player.inventory.getStackInSlot(i).getItem() == this.item) {
/* 125 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/* 126 */         somethingfound = true;
/*     */         
/*     */         break;
/*     */       } 
/* 130 */       if (somethingfound)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoSoup.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */