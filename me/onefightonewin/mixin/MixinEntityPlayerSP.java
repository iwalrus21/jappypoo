/*     */ package me.onefightonewin.mixin;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.audio.ISound;
/*     */ import net.minecraft.client.audio.PositionedSoundRecord;
/*     */ import net.minecraft.client.entity.AbstractClientPlayer;
/*     */ import net.minecraft.client.entity.EntityPlayerSP;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.util.MovementInput;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.world.World;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Overwrite;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mixin({EntityPlayerSP.class})
/*     */ public abstract class MixinEntityPlayerSP
/*     */   extends AbstractClientPlayer
/*     */ {
/*     */   @Shadow
/*     */   public int sprintingTicksLeft;
/*     */   @Shadow
/*     */   protected int sprintToggleTimer;
/*     */   @Shadow
/*     */   public float prevTimeInPortal;
/*     */   @Shadow
/*     */   public float timeInPortal;
/*     */   @Shadow
/*     */   protected Minecraft mc;
/*     */   @Shadow
/*     */   public MovementInput movementInput;
/*     */   @Shadow
/*     */   private int horseJumpPowerCounter;
/*     */   @Shadow
/*     */   private float horseJumpPower;
/*     */   
/*     */   @Shadow
/*     */   abstract boolean isRidingHorse();
/*     */   
/*     */   @Shadow
/*     */   abstract boolean isCurrentViewEntity();
/*     */   
/*     */   @Shadow
/*     */   abstract void sendHorseJump();
/*     */   
/*     */   public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
/*  57 */     super(worldIn, playerProfile);
/*     */   }
/*     */   
/*     */   @Overwrite
/*     */   public void onLivingUpdate() {
/*  62 */     if (this.sprintingTicksLeft > 0) {
/*  63 */       this.sprintingTicksLeft--;
/*     */       
/*  65 */       if (this.sprintingTicksLeft == 0) {
/*  66 */         setSprinting(false);
/*     */       }
/*     */     } 
/*     */     
/*  70 */     if (this.sprintToggleTimer > 0) {
/*  71 */       this.sprintToggleTimer--;
/*     */     }
/*     */     
/*  74 */     this.prevTimeInPortal = this.timeInPortal;
/*     */     
/*  76 */     if (this.inPortal) {
/*  77 */       if (this.mc.currentScreen != null && !this.mc.currentScreen.doesGuiPauseGame()) {
/*  78 */         this.mc.displayGuiScreen((GuiScreen)null);
/*     */       }
/*     */       
/*  81 */       if (this.timeInPortal == 0.0F) {
/*  82 */         this.mc.getSoundHandler().playSound((ISound)PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), this.rand
/*  83 */               .nextFloat() * 0.4F + 0.8F));
/*     */       }
/*     */       
/*  86 */       this.timeInPortal += 0.0125F;
/*     */       
/*  88 */       if (this.timeInPortal >= 1.0F) {
/*  89 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */       
/*  92 */       this.inPortal = false;
/*  93 */     } else if (isPotionActive(Potion.NAUSEA) && 
/*  94 */       getActivePotionEffect(Potion.NAUSEA).getDuration() > 60) {
/*  95 */       this.timeInPortal += 0.006666667F;
/*     */       
/*  97 */       if (this.timeInPortal > 1.0F) {
/*  98 */         this.timeInPortal = 1.0F;
/*     */       }
/*     */     } else {
/* 101 */       if (this.timeInPortal > 0.0F) {
/* 102 */         this.timeInPortal -= 0.05F;
/*     */       }
/*     */       
/* 105 */       if (this.timeInPortal < 0.0F) {
/* 106 */         this.timeInPortal = 0.0F;
/*     */       }
/*     */     } 
/*     */     
/* 110 */     if (this.timeUntilPortal > 0) {
/* 111 */       this.timeUntilPortal--;
/*     */     }
/*     */     
/* 114 */     boolean flag = this.movementInput.jump;
/* 115 */     boolean flag1 = this.movementInput.sneak;
/* 116 */     float f = 0.8F;
/* 117 */     boolean flag2 = (this.movementInput.field_78900_b >= f);
/* 118 */     this.movementInput.updatePlayerMoveState();
/*     */     
/* 120 */     boolean shouldCancel = false;
/*     */     
/* 122 */     for (Feature feature : (AntiAFK.getInstance()).features) {
/* 123 */       if (feature.isEnabled() && 
/* 124 */         !feature.shouldApplyItemSlowdown()) {
/* 125 */         shouldCancel = true;
/*     */         
/*     */         break;
/*     */       } 
/*     */     } 
/*     */     
/* 131 */     if (func_71039_bw() && !shouldCancel && !func_70115_ae()) {
/* 132 */       this.movementInput.moveStrafe *= 0.2F;
/* 133 */       this.movementInput.field_78900_b *= 0.2F;
/* 134 */       this.sprintToggleTimer = 0;
/*     */     } 
/*     */     
/* 137 */     pushOutOfBlocks(this.posX - this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ + this.width * 0.35D);
/*     */     
/* 139 */     pushOutOfBlocks(this.posX - this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ - this.width * 0.35D);
/*     */     
/* 141 */     pushOutOfBlocks(this.posX + this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ - this.width * 0.35D);
/*     */     
/* 143 */     pushOutOfBlocks(this.posX + this.width * 0.35D, (getEntityBoundingBox()).minY + 0.5D, this.posZ + this.width * 0.35D);
/*     */     
/* 145 */     boolean flag3 = (getFoodStats().getFoodLevel() > 6.0F || this.capabilities.allowFlying);
/*     */     
/* 147 */     if (this.onGround && !flag1 && !flag2 && this.movementInput.field_78900_b >= f && !isSprinting() && flag3 && (shouldCancel || 
/* 148 */       !func_71039_bw()) && !isPotionActive(Potion.BLINDNESS)) {
/* 149 */       if (this.sprintToggleTimer <= 0 && !this.mc.gameSettings.keyBindSprint.isKeyDown()) {
/* 150 */         this.sprintToggleTimer = 7;
/*     */       } else {
/* 152 */         setSprinting(true);
/*     */       } 
/*     */     }
/*     */     
/* 156 */     if (!isSprinting() && this.movementInput.field_78900_b >= f && flag3 && (shouldCancel || !func_71039_bw()) && 
/* 157 */       !isPotionActive(Potion.BLINDNESS) && this.mc.gameSettings.keyBindSprint.isKeyDown()) {
/* 158 */       setSprinting(true);
/*     */     }
/*     */     
/* 161 */     if (isSprinting() && (this.movementInput.field_78900_b < f || this.collidedHorizontally || !flag3)) {
/* 162 */       setSprinting(false);
/*     */     }
/*     */     
/* 165 */     if (this.capabilities.allowFlying) {
/* 166 */       if (this.mc.playerController.isSpectatorMode()) {
/* 167 */         if (!this.capabilities.isFlying) {
/* 168 */           this.capabilities.isFlying = true;
/* 169 */           sendPlayerAbilities();
/*     */         } 
/* 171 */       } else if (!flag && this.movementInput.jump) {
/* 172 */         if (this.flyToggleTimer == 0) {
/* 173 */           this.flyToggleTimer = 7;
/*     */         } else {
/* 175 */           this.capabilities.isFlying = !this.capabilities.isFlying;
/* 176 */           sendPlayerAbilities();
/* 177 */           this.flyToggleTimer = 0;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 182 */     if (this.capabilities.isFlying && isCurrentViewEntity()) {
/* 183 */       if (this.movementInput.sneak) {
/* 184 */         this.motionY -= (this.capabilities.getFlySpeed() * 3.0F);
/*     */       }
/*     */       
/* 187 */       if (this.movementInput.jump) {
/* 188 */         this.motionY += (this.capabilities.getFlySpeed() * 3.0F);
/*     */       }
/*     */     } 
/*     */     
/* 192 */     if (isRidingHorse()) {
/* 193 */       if (this.horseJumpPowerCounter < 0) {
/* 194 */         this.horseJumpPowerCounter++;
/*     */         
/* 196 */         if (this.horseJumpPowerCounter == 0) {
/* 197 */           this.horseJumpPower = 0.0F;
/*     */         }
/*     */       } 
/*     */       
/* 201 */       if (flag && !this.movementInput.jump) {
/* 202 */         this.horseJumpPowerCounter = -10;
/* 203 */         sendHorseJump();
/* 204 */       } else if (!flag && this.movementInput.jump) {
/* 205 */         this.horseJumpPowerCounter = 0;
/* 206 */         this.horseJumpPower = 0.0F;
/* 207 */       } else if (flag) {
/* 208 */         this.horseJumpPowerCounter++;
/*     */         
/* 210 */         if (this.horseJumpPowerCounter < 10) {
/* 211 */           this.horseJumpPower = this.horseJumpPowerCounter * 0.1F;
/*     */         } else {
/* 213 */           this.horseJumpPower = 0.8F + 2.0F / (this.horseJumpPowerCounter - 9) * 0.1F;
/*     */         } 
/*     */       } 
/*     */     } else {
/* 217 */       this.horseJumpPower = 0.0F;
/*     */     } 
/*     */     
/* 220 */     super.onLivingUpdate();
/*     */     
/* 222 */     if (this.onGround && this.capabilities.isFlying && !this.mc.playerController.isSpectatorMode()) {
/* 223 */       this.capabilities.isFlying = false;
/* 224 */       sendPlayerAbilities();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinEntityPlayerSP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */