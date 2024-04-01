/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class AutoWaterBottle
/*     */   extends Feature {
/*  24 */   int nerf = 0;
/*  25 */   int prevSlot = -1;
/*     */   
/*     */   boolean attemptUse;
/*     */   
/*     */   long ticksLeft;
/*     */   boolean nextSafe;
/*     */   MenuLabel delayLabel;
/*     */   MenuSlider delay;
/*     */   MenuCheckbox onMove;
/*     */   
/*     */   public AutoWaterBottle(Minecraft minecraft) {
/*  36 */     super(minecraft, "autowaterbottle", 0, Category.PAGE_2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  41 */     this.delayLabel = new MenuLabel("Autowaterbottle - Delay (Ticks)", 0, 0);
/*  42 */     this.delay = new MenuSlider(1, 1, 60, 0, 0, 100, 6);
/*  43 */     this.onMove = new MenuCheckbox("Autowaterbottle - On move only", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  48 */     components.add(this.delayLabel);
/*  49 */     components.add(this.delay);
/*  50 */     components.add(this.onMove);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  55 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  59 */     if (this.onMove.getRValue() && this.mc.player.posX == this.mc.player.prevPosX && this.mc.player.posY == this.mc.player.prevPosY && this.mc.player.posZ == this.mc.player.prevPosZ) {
/*     */       return;
/*     */     }
/*     */     
/*  63 */     if (this.mc.gameSettings.keyBindAttack.isKeyDown() || this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */     
/*  67 */     if (this.ticksLeft > 0L) {
/*  68 */       this.ticksLeft--;
/*     */       
/*     */       return;
/*     */     } 
/*  72 */     boolean found = false;
/*     */     
/*  74 */     for (PotionEffect effect : this.mc.player.getActivePotionEffects()) {
/*  75 */       if (effect.func_76456_a() == 2) {
/*  76 */         found = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*  81 */     if (!found && !this.mc.player.isBurning() && !this.attemptUse) {
/*     */       return;
/*     */     }
/*     */     
/*  85 */     if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */       return;
/*     */     }
/*     */     
/*  89 */     if (this.mc.player.capabilities.isCreativeMode) {
/*  90 */       this.attemptUse = false;
/*     */       
/*     */       return;
/*     */     } 
/*  94 */     if (this.nerf >= 2) {
/*  95 */       organizeInventory();
/*     */     } else {
/*  97 */       this.nerf++;
/*     */     } 
/*  99 */     if (!this.nextSafe) {
/* 100 */       this.nextSafe = true;
/* 101 */       this.ticksLeft = this.delay.getIntValue();
/*     */     } 
/*     */     
/* 104 */     for (int i = 0; i < InventoryPlayer.getHotbarSize(); i++) {
/* 105 */       if (this.mc.player.inventory.getStackInSlot(i) != null && Item.getIdFromItem(this.mc.player.inventory.getStackInSlot(i).getItem()) == 373) {
/* 106 */         this.prevSlot = this.mc.player.inventory.currentItem;
/* 107 */         this.mc.player.inventory.currentItem = i;
/*     */         
/* 109 */         if (this.mc.playerController.func_78769_a((EntityPlayer)this.mc.player, (World)this.mc.world, this.mc.player.inventory.getStackInSlot(i))) {
/* 110 */           this.mc.entityRenderer.itemRenderer.func_78445_c();
/*     */         }
/*     */         
/* 113 */         this.mc.player.inventory.currentItem = this.prevSlot;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/* 118 */     this.nextSafe = false;
/* 119 */     this.attemptUse = false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onAppendMessage(IChatComponent packet) {
/* 124 */     String text = packet.getUnformattedText();
/*     */     
/* 126 */     text = ChatColor.stripColors(text);
/*     */     
/* 128 */     if (text.toLowerCase().contains("hit you with silencing arrow")) {
/* 129 */       this.attemptUse = true;
/*     */     }
/*     */     
/* 132 */     return true;
/*     */   }
/*     */   
/*     */   protected void organizeInventory() {
/* 136 */     this.nerf = 0;
/* 137 */     boolean somethingfound = false;
/* 138 */     for (int i = 9; i <= 35; i++) {
/* 139 */       if (this.mc.player.inventory.getStackInSlot(i) != null && Item.getIdFromItem(this.mc.player.inventory.getStackInSlot(i).getItem()) == 373) {
/* 140 */         (AntiAFK.getInstance()).inventoryManager.quickMove(this, this.mc.player.inventoryContainer.windowId, i);
/* 141 */         somethingfound = true;
/*     */         
/*     */         break;
/*     */       } 
/* 145 */       if (somethingfound)
/*     */         break; 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoWaterBottle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */