/*     */ package me.onefightonewin.mixin;
/*     */ 
/*     */ import java.util.Map;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.types.MouseDelayFix;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.attributes.BaseAttributeMap;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagFloat;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.nbt.NBTTagShort;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.Vec3;
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
/*     */ @Mixin({EntityLivingBase.class})
/*     */ public abstract class MixinEntityLivingBase
/*     */   extends Entity
/*     */ {
/*     */   @Shadow
/*     */   Map<Integer, PotionEffect> activePotionsMap;
/*     */   @Shadow
/*     */   private int revengeTimer;
/*     */   @Shadow
/*     */   public int hurtTime;
/*     */   @Shadow
/*     */   public int deathTime;
/*     */   @Shadow
/*     */   public float prevRotationYawHead;
/*     */   @Shadow
/*     */   public float rotationYawHead;
/*     */   
/*     */   @Shadow
/*     */   abstract void setAbsorptionAmount(float paramFloat);
/*     */   
/*     */   @Shadow
/*     */   abstract BaseAttributeMap getAttributeMap();
/*     */   
/*     */   @Shadow
/*     */   abstract void setHealth(float paramFloat);
/*     */   
/*     */   @Shadow
/*     */   abstract float getMaxHealth();
/*     */   
/*     */   public MixinEntityLivingBase(World worldIn) {
/*  58 */     super(worldIn);
/*     */   }
/*     */   
/*     */   @Overwrite
/*     */   public Vec3 getLook(float partialTicks) {
/*  63 */     if (getUniqueID().equals((Minecraft.getMinecraft()).player.getUniqueID()) && (
/*  64 */       (MouseDelayFix)AntiAFK.getInstance().getFeature(MouseDelayFix.class)).isEnabled()) {
/*  65 */       return super.getLook(partialTicks);
/*     */     }
/*     */ 
/*     */     
/*  69 */     if (partialTicks == 1.0F) {
/*  70 */       return getVectorForRotation(this.rotationPitch, this.rotationYawHead);
/*     */     }
/*  72 */     float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
/*  73 */     float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
/*  74 */     return getVectorForRotation(f, f1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  80 */     this.dataManager.func_75682_a(7, Integer.valueOf(0));
/*  81 */     this.dataManager.func_75682_a(8, Byte.valueOf((byte)0));
/*  82 */     this.dataManager.func_75682_a(9, Byte.valueOf((byte)0));
/*  83 */     this.dataManager.func_75682_a(6, Float.valueOf(1.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  88 */     setAbsorptionAmount(tagCompund.getFloat("AbsorptionAmount"));
/*     */     
/*  90 */     if (tagCompund.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote)
/*     */     {
/*  92 */       SharedMonsterAttributes.setAttributeModifiers(getAttributeMap(), tagCompund.getTagList("Attributes", 10));
/*     */     }
/*     */     
/*  95 */     if (tagCompund.hasKey("ActiveEffects", 9)) {
/*     */       
/*  97 */       NBTTagList nbttaglist = tagCompund.getTagList("ActiveEffects", 10);
/*     */       
/*  99 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*     */         
/* 101 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 102 */         PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);
/*     */         
/* 104 */         if (potioneffect != null)
/*     */         {
/* 106 */           this.activePotionsMap.put(Integer.valueOf(potioneffect.func_76456_a()), potioneffect);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 111 */     if (tagCompund.hasKey("HealF", 99)) {
/*     */       
/* 113 */       setHealth(tagCompund.getFloat("HealF"));
/*     */     }
/*     */     else {
/*     */       
/* 117 */       NBTBase nbtbase = tagCompund.getTag("Health");
/*     */       
/* 119 */       if (nbtbase == null) {
/*     */         
/* 121 */         setHealth(getMaxHealth());
/*     */       }
/* 123 */       else if (nbtbase.getId() == 5) {
/*     */         
/* 125 */         setHealth(((NBTTagFloat)nbtbase).getFloat());
/*     */       }
/* 127 */       else if (nbtbase.getId() == 2) {
/*     */         
/* 129 */         setHealth(((NBTTagShort)nbtbase).getShort());
/*     */       } 
/*     */     } 
/*     */     
/* 133 */     this.hurtTime = tagCompund.getShort("HurtTime");
/* 134 */     this.deathTime = tagCompund.getShort("DeathTime");
/* 135 */     this.revengeTimer = tagCompund.getInteger("HurtByTimestamp");
/*     */   }
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\mixin\MixinEntityLivingBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */