/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ public class Antibot
/*     */   extends Feature {
/*  18 */   final long threatExpireTime = 2750L;
/*  19 */   final long maxLastAttackTime = 500L;
/*     */   
/*     */   List<PlayerData> players;
/*     */   
/*     */   public Antibot(Minecraft minecraft) {
/*  24 */     super(minecraft, "antibot", -1, Category.PAGE_2);
/*  25 */     this.players = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  30 */     (AntiAFK.getInstance()).bots.clear();
/*  31 */     this.players.clear();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMenuInit() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {}
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  46 */     if (this.mc.world == null || this.mc.player == null) {
/*  47 */       this.players.clear();
/*     */       
/*     */       return;
/*     */     } 
/*  51 */     if (this.mc.playerController.isInCreativeMode()) {
/*     */       return;
/*     */     }
/*  54 */     for (EntityPlayer entity : new ArrayList(this.mc.world.playerEntities)) {
/*  55 */       if (entity == this.mc.player) {
/*     */         continue;
/*     */       }
/*  58 */       boolean found = false;
/*     */       
/*  60 */       for (PlayerData playerData : this.players) {
/*  61 */         if (playerData.getEntity() == entity) {
/*  62 */           found = true;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/*  67 */       if (!found) {
/*  68 */         this.players.add(new PlayerData((Entity)entity));
/*     */       }
/*     */     } 
/*     */     
/*  72 */     Iterator<PlayerData> iterator = this.players.iterator();
/*  73 */     while (iterator.hasNext()) {
/*  74 */       PlayerData playerData = iterator.next();
/*  75 */       EntityPlayer user = (EntityPlayer)playerData.getEntity();
/*     */       
/*  77 */       if (user == null) {
/*  78 */         iterator.remove();
/*     */         
/*     */         continue;
/*     */       } 
/*  82 */       if (user.capabilities == null || user.capabilities.isFlying || user.capabilities.disableDamage || user.capabilities.isCreativeMode) {
/*  83 */         setBot((Entity)user, "invalid_capability", true);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         continue;
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 119 */       if (Math.abs(this.mc.player.getDistanceSq(user.posX, this.mc.player.posY, user.posZ)) < 1.0D && user.posY > this.mc.player.posY + 2.0D && (user.isAirBorne || !user.onGround)) {
/* 120 */         long l = System.currentTimeMillis();
/*     */         
/* 122 */         if (playerData.getThreats().isEmpty()) {
/* 123 */           playerData.getThreats().add(Long.valueOf(l));
/*     */         }
/*     */       } 
/*     */       
/* 127 */       if (user.isInvisible()) {
/* 128 */         setBot((Entity)user, "entity_invisible", true);
/*     */         
/*     */         continue;
/*     */       } 
/* 132 */       Iterator<Long> threatIterator = playerData.getThreats().iterator();
/* 133 */       boolean hasThreats = false;
/* 134 */       long time = System.currentTimeMillis();
/*     */       
/* 136 */       while (threatIterator.hasNext()) {
/* 137 */         long next = ((Long)threatIterator.next()).longValue();
/*     */         
/* 139 */         if (time - next > 2750L) {
/* 140 */           threatIterator.remove(); continue;
/*     */         } 
/* 142 */         hasThreats = true;
/*     */       } 
/*     */ 
/*     */       
/* 146 */       if (hasThreats) {
/* 147 */         setBot((Entity)user, "active_threat", true);
/*     */         
/*     */         continue;
/*     */       } 
/* 151 */       if (playerData.getLastHit() != -1L) {
/* 152 */         setBot((Entity)user, "last_hit_timer_not_expired", false);
/*     */         
/*     */         continue;
/*     */       } 
/* 156 */       setBot((Entity)user, "", false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onAttacKEntity(Entity entity) {
/* 162 */     for (PlayerData playerData : this.players) {
/* 163 */       if (playerData.getEntity() == entity) {
/* 164 */         playerData.updateLastHit();
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setBot(Entity user, String type, boolean state) {
/* 171 */     if (state) {
/* 172 */       if (!(AntiAFK.getInstance()).bots.contains(user)) {
/* 173 */         (AntiAFK.getInstance()).bots.add(user);
/*     */       }
/*     */     }
/* 176 */     else if ((AntiAFK.getInstance()).bots.contains(user)) {
/* 177 */       (AntiAFK.getInstance()).bots.remove(user);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   class PlayerData
/*     */   {
/*     */     private Entity entity;
/*     */     
/*     */     private List<Long> threats;
/*     */     
/*     */     private long lastHit;
/*     */ 
/*     */     
/*     */     public PlayerData(Entity entity) {
/* 193 */       this.entity = entity;
/* 194 */       this.threats = new ArrayList<>();
/* 195 */       this.lastHit = -1L;
/*     */     }
/*     */     
/*     */     public Entity getEntity() {
/* 199 */       return this.entity;
/*     */     }
/*     */     
/*     */     public List<Long> getThreats() {
/* 203 */       return this.threats;
/*     */     }
/*     */     
/*     */     public void updateLastHit() {
/* 207 */       this.lastHit = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     public long getLastHit() {
/* 211 */       return (this.lastHit != -1L && System.currentTimeMillis() - this.lastHit > 500L) ? this.lastHit : -1L;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Antibot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */