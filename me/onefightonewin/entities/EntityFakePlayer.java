/*    */ package me.onefightonewin.entities;
/*    */ 
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import net.minecraft.client.entity.AbstractClientPlayer;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityFakePlayer
/*    */   extends AbstractClientPlayer
/*    */ {
/*    */   public EntityFakePlayer(World worldIn, GameProfile gameProfileIn) {
/* 13 */     super(worldIn, gameProfileIn);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSpectator() {
/* 18 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBePushed() {
/* 23 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void collideWithEntity(Entity entity) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canBeCollidedWith() {
/* 33 */     return false;
/*    */   }
/*    */   
/*    */   public void stealInfo(EntityPlayer player) {
/* 37 */     stealInfo(player, player.rotationYaw, player.rotationPitch);
/*    */   }
/*    */   
/*    */   public void stealInfo(EntityPlayer player, float yaw, float pitch) {
/* 41 */     setLocationAndAngles(player.posX, player.posY, player.posZ, yaw, pitch);
/* 42 */     setSneaking(player.isSneaking());
/* 43 */     setSprinting(player.isSprinting());
/* 44 */     this.inventory.copyInventory(player.inventory);
/* 45 */     this.rotationYawHead = yaw;
/*    */     
/* 47 */     this.field_70754_ba = player.field_70754_ba;
/* 48 */     this.limbSwingAmount = player.limbSwingAmount;
/* 49 */     this.swingProgress = player.swingProgress;
/* 50 */     this.swingProgressInt = player.swingProgressInt;
/* 51 */     this.isSwingInProgress = player.isSwingInProgress;
/* 52 */     this.prevPosX = player.prevPosX;
/* 53 */     this.prevPosY = player.prevPosY;
/* 54 */     this.prevPosZ = player.prevPosZ;
/* 55 */     this.prevDistanceWalkedModified = player.prevDistanceWalkedModified;
/* 56 */     this.field_70722_aY = player.field_70722_aY;
/* 57 */     this.prevSwingProgress = player.prevSwingProgress;
/* 58 */     this.prevChasingPosX = player.prevChasingPosX;
/* 59 */     this.prevChasingPosY = player.prevChasingPosY;
/* 60 */     this.prevChasingPosZ = player.prevChasingPosZ;
/* 61 */     this.moveVertical = player.moveVertical;
/* 62 */     this.moveStrafing = player.moveStrafing;
/* 63 */     this.cameraPitch = player.cameraPitch;
/* 64 */     this.prevCameraPitch = player.prevCameraPitch;
/* 65 */     this.cameraYaw = player.cameraYaw;
/* 66 */     this.prevCameraYaw = player.prevCameraYaw;
/* 67 */     this.renderOffsetX = player.renderOffsetX;
/* 68 */     this.renderOffsetY = player.renderOffsetY;
/* 69 */     this.renderOffsetZ = player.renderOffsetZ;
/* 70 */     this.jumpMovementFactor = player.jumpMovementFactor;
/*    */     
/* 72 */     if (yaw == player.rotationYaw && pitch == player.rotationPitch) {
/* 73 */       this.prevRenderYawOffset = player.prevRenderYawOffset;
/* 74 */       this.renderYawOffset = player.renderYawOffset;
/*    */       
/* 76 */       this.prevRotationPitch = player.prevRotationPitch;
/* 77 */       this.prevRotationYaw = player.prevRotationYaw;
/* 78 */       this.prevRotationYawHead = player.prevRotationYawHead;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\entities\EntityFakePlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */