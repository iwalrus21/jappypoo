/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import me.onefightonewin.entities.EntityFakePlayer;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.network.Packet;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Freecam
/*    */   extends Feature
/*    */ {
/*    */   boolean recover = false;
/*    */   public EntityFakePlayer fakePlayer;
/*    */   
/*    */   public Freecam(Minecraft minecraft) {
/* 25 */     super(minecraft, "freecam", 0, Category.PAGE_4);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDisable() {
/* 30 */     if (this.fakePlayer != null && this.mc.player != null) {
/* 31 */       this.mc.player.copyLocationAndAnglesFrom((Entity)this.fakePlayer);
/* 32 */       this.mc.world.removeEntityFromWorld(this.fakePlayer.getEntityId());
/* 33 */       this.fakePlayer = null;
/*    */     } 
/*    */     
/* 36 */     this.mc.player.capabilities.isFlying = this.recover;
/* 37 */     this.mc.player.noClip = false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean onPacketSend(Packet<?> packet) {
/* 42 */     if (packet instanceof net.minecraft.network.play.client.C00PacketKeepAlive) {
/* 43 */       return true;
/*    */     }
/* 45 */     if (packet instanceof net.minecraft.network.play.client.C02PacketUseEntity) {
/* 46 */       return true;
/*    */     }
/* 48 */     if (packet instanceof net.minecraft.network.play.client.C01PacketChatMessage) {
/* 49 */       return true;
/*    */     }
/* 51 */     if (packet instanceof net.minecraft.network.play.client.C0DPacketCloseWindow || packet instanceof net.minecraft.network.play.client.C0EPacketClickWindow) {
/* 52 */       return true;
/*    */     }
/* 54 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 60 */     if (this.mc.world == null || this.mc.player == null) {
/*    */       return;
/*    */     }
/*    */     
/* 64 */     if (!isEnabled()) {
/* 65 */       onDisable();
/*    */       
/*    */       return;
/*    */     } 
/* 69 */     if (this.mc.player.onGround && this.fakePlayer == null) {
/* 70 */       this.recover = this.mc.player.capabilities.isFlying;
/* 71 */       this.fakePlayer = new EntityFakePlayer((World)this.mc.world, new GameProfile(this.mc.player.getUniqueID(), this.mc.player.getName()));
/* 72 */       this.fakePlayer.stealInfo((EntityPlayer)this.mc.player);
/* 73 */       this.mc.world.addEntityToWorld(this.fakePlayer.getEntityId(), (Entity)this.fakePlayer);
/* 74 */     } else if (this.fakePlayer != null) {
/* 75 */       this.mc.player.noClip = true;
/*    */       
/* 77 */       if (!this.mc.player.capabilities.isFlying)
/* 78 */         this.mc.player.capabilities.isFlying = true; 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Freecam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */