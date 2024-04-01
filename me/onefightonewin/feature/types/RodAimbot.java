/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import javax.vecmath.Vector2f;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.AimTarget;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
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
/*     */ public class RodAimbot
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel targetsLabel;
/*     */   MenuComboBox targets;
/*  45 */   int maxDistance = 6;
/*     */   
/*     */   Method method;
/*     */   
/*     */   boolean nextOk;
/*     */   
/*     */   int ticksToClick;
/*     */   
/*     */   public RodAimbot(Minecraft minecraft) {
/*  54 */     super(minecraft, "rodaimbot", 0, Category.PAGE_2);
/*  55 */     this.method = ReflectionHelper.findMethod(Minecraft.class, minecraft, new String[] { "rightClickMouse", "func_147121_ag" }, new Class[0]);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  60 */     this.targetsLabel = new MenuLabel("Targets", 0, 0);
/*  61 */     this.targets = new MenuComboBox(AimTarget.class, 0, 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  66 */     components.add(this.targetsLabel);
/*  67 */     components.add(this.targets);
/*     */   }
/*     */   
/*     */   public void onEnable() {
/*  71 */     this.nextOk = false;
/*  72 */     this.ticksToClick = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onRightClick() {
/*  77 */     if (this.mc.world == null || this.mc.player == null) {
/*  78 */       return true;
/*     */     }
/*     */     
/*  81 */     if (this.mc.playerController.isSpectator()) {
/*  82 */       return true;
/*     */     }
/*  84 */     ItemStack item = this.mc.player.func_70694_bm();
/*     */     
/*  86 */     if (item == null || item.getItem() == null || item.getItem() != Items.FISHING_ROD) {
/*  87 */       this.ticksToClick = -1;
/*  88 */       this.nextOk = false;
/*  89 */       return true;
/*     */     } 
/*     */     
/*  92 */     if (this.nextOk) {
/*  93 */       this.nextOk = false;
/*  94 */       this.ticksToClick = -1;
/*  95 */       return true;
/*     */     } 
/*     */     
/*  98 */     Entity target = getBestTarget();
/*     */     
/* 100 */     if (target == null) {
/* 101 */       return true;
/*     */     }
/*     */     
/* 104 */     Vector2f aimpoint = getAimPoint(target, false, true);
/*     */     
/* 106 */     this.mc.player.rotationYaw = aimpoint.x;
/* 107 */     this.mc.player.rotationPitch = aimpoint.y;
/* 108 */     this.ticksToClick = 3;
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 115 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 119 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 122 */     if (this.ticksToClick == -1) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     ItemStack item = this.mc.player.func_70694_bm();
/*     */     
/* 128 */     if (item == null || item.getItem() == null || item.getItem() != Items.FISHING_ROD) {
/*     */       return;
/*     */     }
/*     */     
/* 132 */     if (this.ticksToClick > 0) {
/* 133 */       this.ticksToClick--;
/*     */       
/* 135 */       Entity target = getBestTarget();
/*     */       
/* 137 */       if (target == null) {
/*     */         return;
/*     */       }
/*     */       
/* 141 */       Vector2f aimpoint = getAimPoint(target, false, true);
/*     */       
/* 143 */       this.mc.player.rotationYaw = aimpoint.x;
/* 144 */       this.mc.player.rotationPitch = aimpoint.y;
/*     */     } else {
/*     */       try {
/* 147 */         this.nextOk = true;
/* 148 */         this.method.invoke(this.mc, new Object[0]);
/* 149 */       } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 150 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Vector2f getAimPoint(Entity entity, boolean projectile, boolean predictPosition) {
/* 156 */     double x = entity.posX - this.mc.player.posX;
/* 157 */     double y = entity.posY - this.mc.player.posY - (this.mc.player.getEyeHeight() - entity.getEyeHeight());
/* 158 */     double z = entity.posZ - this.mc.player.posZ;
/* 159 */     double sq = Math.sqrt(x * x + z * z);
/* 160 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 161 */     if ((projectile || predictPosition) && 
/* 162 */       predictPosition) {
/* 163 */       double eyePosDist = this.mc.player.getPositionEyes(1.0F).distanceTo(getCenter(entity.getEntityBoundingBox()));
/*     */       
/* 165 */       if (entity.prevPosX != entity.posX) {
/* 166 */         x = entity.posX + (entity.posX - entity.prevPosX) * eyePosDist - this.mc.player.posX;
/*     */       }
/* 168 */       if (entity.prevPosZ != entity.posZ) {
/* 169 */         z = entity.posZ + (entity.posZ - entity.prevPosZ) * eyePosDist - this.mc.player.posZ;
/*     */       }
/*     */     } 
/*     */     
/* 173 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/* 174 */     return clampAngles(yaw, pitch);
/*     */   }
/*     */   
/*     */   public Vec3 getCenter(AxisAlignedBB bb) {
/* 178 */     return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5D, bb.minY + (bb.maxY - bb.minY) * 0.5D, bb.minZ + (bb.maxZ - bb.minZ) * 0.5D);
/*     */   }
/*     */   
/*     */   public Vector2f clampAngles(float yaw, float pitch) {
/* 182 */     float pitchDelta = clamp(pitch - this.mc.player.rotationPitch, 90.0F);
/* 183 */     float yawDelta = clamp(MathHelper.wrapDegrees(yaw - this.mc.player.rotationYaw), 180.0F);
/*     */     
/* 185 */     return new Vector2f(this.mc.player.rotationYaw + yawDelta, this.mc.player.rotationPitch + pitchDelta);
/*     */   }
/*     */   
/*     */   public float clamp(float value, float max) {
/* 189 */     if (value > max) {
/* 190 */       value = max;
/*     */     }
/* 192 */     if (value < -max) {
/* 193 */       value = -max;
/*     */     }
/* 195 */     return value;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/* 199 */     for (String condition : this.targets.getValues()) {
/* 200 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 201 */         return true; 
/*     */     } 
/* 203 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getBestTarget() {
/* 208 */     if (this.mc.world.loadedEntityList == null) {
/* 209 */       return null;
/*     */     }
/* 211 */     Entity finalTarget = null;
/* 212 */     float best = Float.MAX_VALUE;
/*     */     
/* 214 */     for (Entity target : this.mc.world.loadedEntityList) {
/* 215 */       if (isValidTarget(target)) {
/* 216 */         float user = this.mc.player.getDistance(target);
/*     */         
/* 218 */         if (best > user) {
/* 219 */           best = user;
/* 220 */           finalTarget = target;
/*     */         } 
/*     */       } 
/*     */     } 
/* 224 */     return finalTarget;
/*     */   }
/*     */   
/*     */   public boolean isValidTarget(Entity target) {
/* 228 */     if (((target instanceof EntityPlayer && hasCondition(AimTarget.ENEMIES)) || (target instanceof net.minecraft.entity.passive.EntityVillager && 
/* 229 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityBat && 
/* 230 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntitySlime && 
/* 231 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityMob && 
/* 232 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityIronGolem && 
/* 233 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.passive.EntitySquid && 
/* 234 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityAnimal && 
/* 235 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.boss.EntityDragon && 
/* 236 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityGhast && 
/* 237 */       hasCondition(AimTarget.MOBS))) && target != this.mc.player) {
/*     */ 
/*     */       
/* 240 */       if (((Antibot)AntiAFK.getInstance().getFeature(Antibot.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/* 241 */         return false;
/*     */       }
/*     */       
/* 244 */       if (!target.isInvisible() && this.maxDistance > this.mc.player.getDistance(target) && this.mc.player.canEntityBeSeen(target) && !target.isDead) {
/* 245 */         if (target instanceof EntityPlayer) {
/* 246 */           EntityPlayer player = (EntityPlayer)target;
/*     */           
/* 248 */           if (!hasCondition(AimTarget.TEAM_BY_COLOR) && 
/* 249 */             this.mc.player.func_142014_c((EntityLivingBase)player)) {
/* 250 */             return false;
/*     */           }
/*     */           
/* 253 */           if (target instanceof me.onefightonewin.entities.EntityFakePlayer) {
/* 254 */             return false;
/*     */           }
/*     */           
/* 257 */           if (!hasCondition(AimTarget.RANKED_USERS)) {
/* 258 */             String name = ChatColor.stripColors(player.getDisplayName().getUnformattedText().toUpperCase()).split(" ")[0];
/*     */             
/* 260 */             if (player.getDisplayName().getUnformattedText().contains(" ") && (
/* 261 */               name.contains("ULTRA") || name.contains("HERO") || name.contains("LEGEND") || name.contains("TITAN") || name.contains("ETERNAL") || name.contains("IMMORTAL") || name
/* 262 */               .contains("YT") || name.contains("STREAM") || name.contains("YOUTUBE") || name.contains("KNIGHT") || name.contains("LORD") || name
/* 263 */               .contains("LADY") || name.contains("DUKE") || name.contains("DUCHNESS") || name.contains("TRAINEE") || name.contains("MOD") || name
/* 264 */               .contains("SR.MOD") || name.contains("ADMIN") || name.contains("LEADER") || name.contains("OWNER") || name.contains("BUILDER") || name
/* 265 */               .contains("BUILDLEAD") || name.contains("DEVELOPER") || name.contains("SUPPORT") || name.contains("ARTIST"))) {
/* 266 */               return false;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 271 */           if (!hasCondition(AimTarget.FRIENDS) && 
/* 272 */             (AntiAFK.getInstance()).friends.contains(player.getName())) {
/* 273 */             return false;
/*     */           }
/*     */           
/* 276 */           if (player.isSpectator())
/* 277 */             return false; 
/*     */         } 
/* 279 */         return true;
/*     */       } 
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\RodAimbot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */