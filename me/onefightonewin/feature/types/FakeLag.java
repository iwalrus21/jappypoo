/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.entities.EntityFakePlayer;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FakeLag
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel blocksUntilStartLabel;
/*     */   MenuSlider blocksUntilStart;
/*     */   MenuLabel blocksUntilStopLabel;
/*     */   MenuSlider blocksUntilStop;
/*     */   MenuLabel maxTicksLabel;
/*     */   MenuSlider maxTicks;
/*     */   List<Packet<?>> packets;
/*     */   int refreshTime;
/*  34 */   final int refreshRate = 5;
/*     */   
/*     */   int ticksChoked;
/*     */   
/*     */   boolean choke;
/*     */   boolean shouldStopChoke;
/*     */   public EntityFakePlayer fakePlayer;
/*     */   
/*     */   public FakeLag(Minecraft minecraft) {
/*  43 */     super(minecraft, "fakelag", 0, Category.PAGE_3);
/*  44 */     this.packets = new ArrayList<>();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  49 */     this.blocksUntilStartLabel = new MenuLabel("Blocks until start", 0, 0);
/*  50 */     this.blocksUntilStart = new MenuSlider(7.0F, 1.0F, 10.0F, 1, 0, 0, 100, 6);
/*  51 */     this.blocksUntilStopLabel = new MenuLabel("Blocks until stop", 0, 0);
/*  52 */     this.blocksUntilStop = new MenuSlider(3.0F, 1.0F, 10.0F, 1, 0, 0, 100, 6);
/*  53 */     this.maxTicksLabel = new MenuLabel("Max ticks", 0, 0);
/*  54 */     this.maxTicks = new MenuSlider(80, 1, 120, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  59 */     components.add(this.blocksUntilStartLabel);
/*  60 */     components.add(this.blocksUntilStart);
/*  61 */     components.add(this.blocksUntilStopLabel);
/*  62 */     components.add(this.blocksUntilStop);
/*  63 */     components.add(this.maxTicksLabel);
/*  64 */     components.add(this.maxTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  69 */     if (this.fakePlayer != null && this.mc.player != null) {
/*  70 */       this.mc.player.copyLocationAndAnglesFrom((Entity)this.fakePlayer);
/*  71 */       this.mc.world.removeEntityFromWorld(this.fakePlayer.getEntityId());
/*  72 */       this.fakePlayer = null;
/*     */     } 
/*     */     
/*  75 */     this.ticksChoked = 0;
/*  76 */     this.choke = false;
/*  77 */     this.shouldStopChoke = false;
/*     */     
/*  79 */     if (this.mc.getConnection() == null) {
/*  80 */       for (Packet<?> packet : this.packets) {
/*  81 */         this.mc.getConnection().sendPacket(packet);
/*     */       }
/*     */     }
/*     */     
/*  85 */     this.packets.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  91 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  95 */     if (this.shouldStopChoke) {
/*     */       return;
/*     */     }
/*     */     
/*  99 */     this.refreshTime++;
/*     */     
/* 101 */     if (this.choke) {
/* 102 */       boolean isClose = false;
/*     */       
/* 104 */       if (this.refreshTime > 5) {
/* 105 */         this.refreshTime = 0;
/*     */         
/* 107 */         for (Entity entity : this.mc.world.loadedEntityList) {
/* 108 */           if (!(entity instanceof EntityPlayer) || 
/* 109 */             !isValidTarget(entity)) {
/*     */             continue;
/*     */           }
/*     */           
/* 113 */           float distance = this.mc.player.getDistance(entity);
/*     */           
/* 115 */           if (distance < this.blocksUntilStop.getValue()) {
/* 116 */             isClose = true;
/*     */             
/*     */             break;
/*     */           } 
/*     */         } 
/*     */       } else {
/* 122 */         this.refreshTime++;
/*     */       } 
/*     */       
/* 125 */       if (isClose || this.ticksChoked > this.maxTicks.getIntValue()) {
/* 126 */         this.shouldStopChoke = true;
/* 127 */       } else if (this.choke) {
/* 128 */         this.ticksChoked++;
/*     */       }
/*     */     
/* 131 */     } else if (this.refreshTime > 5) {
/* 132 */       this.refreshTime = 0;
/*     */       
/* 134 */       boolean shouldChoke = false;
/*     */       
/* 136 */       for (Entity entity : this.mc.world.loadedEntityList) {
/* 137 */         if (!(entity instanceof EntityPlayer) || 
/* 138 */           !isValidTarget(entity)) {
/*     */           continue;
/*     */         }
/*     */         
/* 142 */         float distance = this.mc.player.getDistance(entity);
/*     */         
/* 144 */         if (distance < this.blocksUntilStop.getValue()) {
/* 145 */           shouldChoke = false;
/*     */           
/*     */           break;
/*     */         } 
/* 149 */         if (distance < this.blocksUntilStart.getValue()) {
/* 150 */           shouldChoke = true;
/*     */         }
/*     */       } 
/*     */ 
/*     */       
/* 155 */       if (shouldChoke && this.mc.player.onGround) {
/* 156 */         if (this.fakePlayer == null) {
/* 157 */           this.fakePlayer = new EntityFakePlayer((World)this.mc.world, this.mc.player.getGameProfile());
/* 158 */           this.fakePlayer.stealInfo((EntityPlayer)this.mc.player);
/* 159 */           this.mc.world.addEntityToWorld(this.fakePlayer.getEntityId(), (Entity)this.fakePlayer);
/*     */         } 
/*     */         
/* 162 */         this.choke = true;
/* 163 */         this.ticksChoked++;
/* 164 */       } else if (shouldChoke) {
/* 165 */         this.refreshTime = 6;
/*     */       } 
/*     */     } else {
/* 168 */       this.refreshTime++;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onPacketSend(Packet<?> packet) {
/* 175 */     if (packet instanceof net.minecraft.network.handshake.client.C00Handshake) {
/* 176 */       this.choke = false;
/* 177 */       this.shouldStopChoke = false;
/* 178 */       this.ticksChoked = 0;
/* 179 */       this.packets.clear();
/* 180 */       return true;
/*     */     } 
/*     */     
/* 183 */     if (this.choke) {
/* 184 */       this.packets.add(packet);
/*     */       
/* 186 */       if (this.shouldStopChoke) {
/* 187 */         this.choke = false;
/* 188 */         this.shouldStopChoke = false;
/* 189 */         this.ticksChoked = 0;
/*     */         
/* 191 */         if (this.fakePlayer != null) {
/* 192 */           this.mc.world.removeEntity((Entity)this.fakePlayer);
/* 193 */           this.fakePlayer = null;
/*     */         } 
/*     */         
/* 196 */         for (Packet<?> p : this.packets) {
/* 197 */           this.mc.getConnection().sendPacket(p);
/*     */         }
/*     */         
/* 200 */         this.packets.clear();
/*     */       } 
/* 202 */       return false;
/*     */     } 
/* 204 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidTarget(Entity target) {
/* 209 */     if (target instanceof EntityPlayer && target != this.mc.player) {
/* 210 */       if (((Antibot)AntiAFK.getInstance().getFeature(Antibot.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/* 211 */         return false;
/*     */       }
/*     */       
/* 214 */       if ((AntiAFK.getInstance()).friends.contains(target.getName())) {
/* 215 */         return false;
/*     */       }
/*     */       
/* 218 */       if (target instanceof EntityFakePlayer) {
/* 219 */         return false;
/*     */       }
/*     */       
/* 222 */       return true;
/*     */     } 
/*     */     
/* 225 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FakeLag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */