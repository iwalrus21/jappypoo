/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ public class AntiDisengage
/*     */   extends Feature
/*     */ {
/*  22 */   final long immuneTime = 2000L;
/*  23 */   final long gracePeriod = 16000L;
/*     */   
/*     */   List<PlayerData> players;
/*     */   
/*     */   public AntiDisengage(Minecraft minecraft) {
/*  28 */     super(minecraft, "antidisengage", 0, Category.PAGE_2);
/*  29 */     this.players = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  34 */     this.players.clear();
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  39 */     if (this.mc.world == null || this.mc.player == null) {
/*  40 */       this.players.clear();
/*     */       
/*     */       return;
/*     */     } 
/*  44 */     if (this.mc.playerController.isInCreativeMode()) {
/*     */       return;
/*     */     }
/*  47 */     for (EntityPlayer entity : new ArrayList(this.mc.world.playerEntities)) {
/*  48 */       if (entity == this.mc.player) {
/*     */         continue;
/*     */       }
/*     */       
/*  52 */       if (!isWearingChainArmor(entity)) {
/*     */         continue;
/*     */       }
/*     */       
/*  56 */       PlayerData found = null;
/*     */       
/*  58 */       for (PlayerData playerData : this.players) {
/*  59 */         if (playerData.getUUID().equals(entity.getUniqueID())) {
/*  60 */           found = playerData;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  65 */       if (!isBlocking(entity)) {
/*  66 */         if (found != null) {
/*  67 */           found.markForRemoval();
/*     */         }
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/*  73 */       if (found == null) {
/*  74 */         this.players.add(new PlayerData(entity.getUniqueID()));
/*     */       }
/*     */     } 
/*     */     
/*  78 */     Iterator<PlayerData> iterator = this.players.iterator();
/*     */     
/*  80 */     while (iterator.hasNext()) {
/*  81 */       PlayerData playerData = iterator.next();
/*     */       
/*  83 */       if (playerData.isMarkedForRemoval()) {
/*  84 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean isWearingChainArmor(EntityPlayer entity) {
/*  90 */     ItemStack headStack = entity.func_82169_q(3);
/*  91 */     ItemStack bodyStack = entity.func_82169_q(2);
/*  92 */     ItemStack legsStack = entity.func_82169_q(1);
/*  93 */     ItemStack feetStack = entity.func_82169_q(0);
/*     */     
/*  95 */     if (headStack == null || bodyStack == null || legsStack == null || feetStack == null) {
/*  96 */       return false;
/*     */     }
/*     */     
/*  99 */     Item head = headStack.getItem();
/* 100 */     Item body = bodyStack.getItem();
/* 101 */     Item legs = legsStack.getItem();
/* 102 */     Item feet = feetStack.getItem();
/*     */     
/* 104 */     if (head != Items.CHAINMAIL_HELMET) {
/* 105 */       return false;
/*     */     }
/*     */     
/* 108 */     if (body != Items.CHAINMAIL_CHESTPLATE) {
/* 109 */       return false;
/*     */     }
/*     */     
/* 112 */     if (legs != Items.CHAINMAIL_LEGGINGS) {
/* 113 */       return false;
/*     */     }
/*     */     
/* 116 */     if (feet != Items.CHAINMAIL_BOOTS) {
/* 117 */       return false;
/*     */     }
/*     */     
/* 120 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isBlocking(EntityPlayer player) {
/* 124 */     if (player.func_70632_aY()) {
/* 125 */       return true;
/*     */     }
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isEntityEvading(Entity entity) {
/* 132 */     if (!isEnabled()) {
/* 133 */       return false;
/*     */     }
/*     */     
/* 136 */     boolean evading = false;
/*     */     
/* 138 */     for (PlayerData data : this.players) {
/* 139 */       if (!data.getUUID().equals(entity.getUniqueID())) {
/*     */         continue;
/*     */       }
/*     */       
/* 143 */       long time = System.currentTimeMillis();
/* 144 */       long delta = time - data.getStart();
/*     */       
/* 146 */       if (delta > 2000L) {
/* 147 */         if (delta > 16000L) {
/* 148 */           data.markForRemoval();
/*     */         }
/*     */         
/* 151 */         evading = false;
/*     */         
/*     */         break;
/*     */       } 
/* 155 */       evading = true;
/*     */     } 
/*     */ 
/*     */     
/* 159 */     if (entity instanceof EntityPlayer) {
/* 160 */       EntityPlayer target = (EntityPlayer)entity;
/*     */       
/* 162 */       if (!evading && 
/* 163 */         isWearingChainArmor(target) && !isBlocking(target)) {
/* 164 */         for (PlayerData data : this.players) {
/* 165 */           if (data.getUUID().equals(entity.getUniqueID())) {
/*     */             continue;
/*     */           }
/*     */           
/* 169 */           long time = System.currentTimeMillis();
/* 170 */           long delta = time - data.getStart();
/*     */           
/* 172 */           if (delta > 2000L) {
/*     */             continue;
/*     */           }
/*     */           
/* 176 */           EntityPlayer player = this.mc.world.getPlayerEntityByUUID(data.getUUID());
/*     */           
/* 178 */           if (player == null) {
/*     */             continue;
/*     */           }
/*     */           
/* 182 */           if (entity.getDistance((Entity)player) > this.mc.playerController.getBlockReachDistance() * 2.0F) {
/* 183 */             evading = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 191 */     return evading;
/*     */   }
/*     */   
/*     */   class PlayerData {
/*     */     private UUID uuid;
/*     */     private long start;
/*     */     private boolean markedForRemoval;
/*     */     
/*     */     public PlayerData(UUID uuid) {
/* 200 */       this.uuid = uuid;
/* 201 */       this.start = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     public UUID getUUID() {
/* 205 */       return this.uuid;
/*     */     }
/*     */     
/*     */     public long getStart() {
/* 209 */       return this.start;
/*     */     }
/*     */     
/*     */     public void markForRemoval() {
/* 213 */       this.markedForRemoval = true;
/*     */     }
/*     */     
/*     */     public boolean isMarkedForRemoval() {
/* 217 */       return this.markedForRemoval;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\AntiDisengage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */