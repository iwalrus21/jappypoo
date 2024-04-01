/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.ItemSword;
/*     */ import net.minecraft.item.ItemTool;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ 
/*     */ 
/*     */ public class AutoTool
/*     */   extends Feature
/*     */ {
/*     */   Field attackDamage;
/*     */   Field damageVSEntities;
/*     */   
/*     */   public AutoTool(Minecraft minecraft) {
/*  25 */     super(minecraft, "autotool", 0, Category.PAGE_5);
/*  26 */     this.attackDamage = ReflectionHelper.findField(ItemSword.class, new String[] { "attackDamage", "field_150934_a" });
/*  27 */     this.damageVSEntities = ReflectionHelper.findField(ItemTool.class, new String[] { "damageVsEntity", "field_77865_bY" });
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  32 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  36 */     if (this.mc.player.isSpectator() || this.mc.player.capabilities.isCreativeMode) {
/*     */       return;
/*     */     }
/*  39 */     if (this.mc.gameSettings.keyBindAttack.isKeyDown())
/*  40 */       setBestItem((this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)); 
/*     */   }
/*     */   
/*     */   public void setBestItem(boolean isBlock) {
/*  44 */     float bestHotbarStrength = 1.0F;
/*  45 */     int bestHotbarSlot = -1;
/*     */     
/*  47 */     float bestInvStrength = 1.0F;
/*  48 */     int bestInvSlot = -1;
/*  49 */     boolean foundSword = false;
/*     */     
/*  51 */     for (int i = 0; i < 36; i++) {
/*  52 */       ItemStack item = this.mc.player.inventory.getStackInSlot(i);
/*     */       
/*  54 */       if (item != null) {
/*     */ 
/*     */ 
/*     */         
/*  58 */         boolean isSword = false;
/*  59 */         if (item.getItem() instanceof ItemSword) {
/*  60 */           isSword = true;
/*     */         }
/*  62 */         if (!foundSword && isSword) {
/*  63 */           foundSword = true;
/*     */         }
/*  65 */         float strength = isBlock ? item.getDestroySpeed(this.mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock()) : getDamageVSEntity(item.getItem(), foundSword);
/*     */         
/*  67 */         if (i < 9) {
/*  68 */           if (strength > bestHotbarStrength || (item != null && item.getItem() != null && bestHotbarSlot == -1)) {
/*  69 */             bestHotbarStrength = strength;
/*  70 */             bestInvStrength = strength;
/*  71 */             bestHotbarSlot = i;
/*     */           }
/*     */         
/*  74 */         } else if (strength > bestInvStrength) {
/*  75 */           bestInvStrength = strength;
/*  76 */           bestInvSlot = i;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  81 */     if (bestHotbarSlot == -1) {
/*     */       return;
/*     */     }
/*  84 */     if (bestInvSlot != -1) {
/*  85 */       if ((AntiAFK.getInstance()).inventoryManager.hasPendingActions(this)) {
/*     */         return;
/*     */       }
/*  88 */       (AntiAFK.getInstance()).inventoryManager.moveItem(this, this.mc.player.inventoryContainer.windowId, bestInvSlot, bestHotbarSlot);
/*     */     } 
/*     */     
/*  91 */     this.mc.player.inventory.currentItem = bestHotbarSlot;
/*     */   }
/*     */   
/*     */   public float getDamageVSEntity(Item item, boolean foundSword) {
/*  95 */     if (item instanceof ItemSword) {
/*     */       try {
/*  97 */         return this.attackDamage.getFloat(item);
/*  98 */       } catch (IllegalArgumentException|IllegalAccessException e1) {
/*  99 */         e1.printStackTrace();
/*     */       } 
/* 101 */     } else if (!foundSword && item instanceof ItemTool) {
/*     */       try {
/* 103 */         return this.damageVSEntities.getFloat(item);
/* 104 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 105 */         e.printStackTrace();
/*     */       } 
/* 107 */     }  return 0.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AutoTool.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */