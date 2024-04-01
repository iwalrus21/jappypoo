/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.vecmath.Vector2f;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.monster.EntityZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class QuickShop
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel typeLabel;
/*     */   MenuDropdown type;
/*     */   
/*     */   public QuickShop(Minecraft minecraft) {
/*  29 */     super(minecraft, "quickshop", 0, Category.PAGE_7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  34 */     this.typeLabel = new MenuLabel("QuickKit type", 0, 0);
/*  35 */     this.type = new MenuDropdown(ArmorType.class, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  40 */     components.add(this.typeLabel);
/*  41 */     components.add(this.type);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {
/*  46 */     if (this.type.getValue().length() == 0) {
/*     */       return;
/*     */     }
/*     */     
/*  50 */     onAction(ArmorType.valueOf(this.type.getValue().toUpperCase()));
/*     */   }
/*     */   
/*     */   public void onAction(ArmorType type) {
/*  54 */     for (Entity entity : this.mc.world.loadedEntityList) {
/*  55 */       if (!(entity instanceof EntityZombie)) {
/*     */         continue;
/*     */       }
/*     */       
/*  59 */       EntityZombie ent = (EntityZombie)entity;
/*     */       
/*  61 */       if (isArmorType(ent, type)) {
/*  62 */         boolean flag = true;
/*     */         
/*  64 */         double x = entity.posX - this.mc.player.posX;
/*  65 */         double y = entity.posY - this.mc.player.posY - (this.mc.player.getEyeHeight() - entity.getEyeHeight() / 3.0F * 2.0F);
/*  66 */         double z = entity.posZ - this.mc.player.posZ;
/*     */         
/*  68 */         Vec3 hitVecIn = new Vec3(x, y, z);
/*     */         
/*  70 */         MovingObjectPosition objOver = new MovingObjectPosition((Entity)ent, hitVecIn);
/*     */         
/*  72 */         if (this.mc.playerController.func_178894_a((EntityPlayer)this.mc.player, entity, objOver)) {
/*  73 */           flag = false;
/*  74 */         } else if (this.mc.playerController.func_78768_b((EntityPlayer)this.mc.player, entity)) {
/*  75 */           flag = false;
/*     */         } 
/*     */         
/*  78 */         if (flag) {
/*  79 */           ItemStack itemstack1 = this.mc.player.inventory.getCurrentItem();
/*     */           
/*  81 */           if (itemstack1 != null && this.mc.playerController.func_78769_a((EntityPlayer)this.mc.player, (World)this.mc.world, itemstack1)) {
/*  82 */             this.mc.entityRenderer.itemRenderer.func_78445_c();
/*     */           }
/*     */         } 
/*     */         break;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isArmorType(EntityZombie entity, ArmorType type) {
/*  92 */     ItemStack helmet = entity.func_82169_q(3);
/*  93 */     ItemStack chestplate = entity.func_82169_q(2);
/*  94 */     ItemStack leggings = entity.func_82169_q(1);
/*  95 */     ItemStack boots = entity.func_82169_q(0);
/*     */     
/*  97 */     if (helmet == null || helmet.getItem() == null) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     if (chestplate == null || chestplate.getItem() == null) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     if (leggings == null || leggings.getItem() == null) {
/* 106 */       return false;
/*     */     }
/*     */     
/* 109 */     if (boots == null || boots.getItem() == null) {
/* 110 */       return false;
/*     */     }
/*     */     
/* 113 */     switch (type) {
/*     */       case CHAIN:
/* 115 */         if (helmet.getItem() == Items.CHAINMAIL_HELMET && chestplate
/* 116 */           .getItem() == Items.CHAINMAIL_CHESTPLATE && leggings
/* 117 */           .getItem() == Items.CHAINMAIL_LEGGINGS && boots
/* 118 */           .getItem() == Items.CHAINMAIL_BOOTS) {
/* 119 */           return true;
/*     */         }
/*     */         break;
/*     */       
/*     */       case DIAMOND:
/* 124 */         if (helmet.getItem() == Items.DIAMOND_HELMET && chestplate
/* 125 */           .getItem() == Items.DIAMOND_CHESTPLATE && leggings
/* 126 */           .getItem() == Items.DIAMOND_LEGGINGS && boots
/* 127 */           .getItem() == Items.DIAMOND_BOOTS) {
/* 128 */           return true;
/*     */         }
/*     */         break;
/*     */       
/*     */       case GOLD:
/* 133 */         if (helmet.getItem() == Items.GOLDEN_HELMET && chestplate
/* 134 */           .getItem() == Items.GOLDEN_CHESTPLATE && leggings
/* 135 */           .getItem() == Items.GOLDEN_LEGGINGS && boots
/* 136 */           .getItem() == Items.GOLDEN_BOOTS) {
/* 137 */           return true;
/*     */         }
/*     */         break;
/*     */       
/*     */       case IRON:
/* 142 */         if (helmet.getItem() == Items.IRON_HELMET && chestplate
/* 143 */           .getItem() == Items.IRON_CHESTPLATE && leggings
/* 144 */           .getItem() == Items.IRON_LEGGINGS && boots
/* 145 */           .getItem() == Items.IRON_BOOTS) {
/* 146 */           return true;
/*     */         }
/*     */         break;
/*     */       
/*     */       case LEATHER:
/* 151 */         if (helmet.getItem() == Items.LEATHER_HELMET && chestplate
/* 152 */           .getItem() == Items.LEATHER_CHESTPLATE && leggings
/* 153 */           .getItem() == Items.LEATHER_LEGGINGS && boots
/* 154 */           .getItem() == Items.LEATHER_BOOTS) {
/* 155 */           return true;
/*     */         }
/*     */         break;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   enum ArmorType {
/* 168 */     DIAMOND, CHAIN, GOLD, IRON, LEATHER;
/*     */   }
/*     */   
/*     */   public Vector2f getAimPoint(Entity entity, boolean projectile, boolean predictPosition) {
/* 172 */     double x = entity.posX - this.mc.player.posX;
/* 173 */     double y = entity.posY - this.mc.player.posY - (this.mc.player.getEyeHeight() - entity.getEyeHeight());
/* 174 */     double z = entity.posZ - this.mc.player.posZ;
/* 175 */     double sq = Math.sqrt(x * x + z * z);
/* 176 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 177 */     if ((projectile || predictPosition) && 
/* 178 */       predictPosition) {
/* 179 */       double eyePosDist = this.mc.player.getPositionEyes(1.0F).distanceTo(getCenter(entity.getEntityBoundingBox()));
/*     */       
/* 181 */       if (entity.prevPosX != entity.posX) {
/* 182 */         x = entity.posX + (entity.posX - entity.prevPosX) * eyePosDist - this.mc.player.posX;
/*     */       }
/* 184 */       if (entity.prevPosZ != entity.posZ) {
/* 185 */         z = entity.posZ + (entity.posZ - entity.prevPosZ) * eyePosDist - this.mc.player.posZ;
/*     */       }
/*     */     } 
/*     */     
/* 189 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/* 190 */     return clampAngles(yaw, pitch);
/*     */   }
/*     */   
/*     */   public Vec3 getCenter(AxisAlignedBB bb) {
/* 194 */     return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5D, bb.minY + (bb.maxY - bb.minY) * 0.5D, bb.minZ + (bb.maxZ - bb.minZ) * 0.5D);
/*     */   }
/*     */   
/*     */   public Vector2f clampAngles(float yaw, float pitch) {
/* 198 */     float pitchDelta = clamp(pitch - this.mc.player.rotationPitch, 90.0F);
/* 199 */     float yawDelta = clamp(MathHelper.wrapDegrees(yaw - this.mc.player.rotationYaw), 180.0F);
/*     */     
/* 201 */     return new Vector2f(this.mc.player.rotationYaw + yawDelta, this.mc.player.rotationPitch + pitchDelta);
/*     */   }
/*     */   
/*     */   public float clamp(float value, float max) {
/* 205 */     if (value > max) {
/* 206 */       value = max;
/*     */     }
/* 208 */     if (value < -max) {
/* 209 */       value = -max;
/*     */     }
/* 211 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\QuickShop.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */