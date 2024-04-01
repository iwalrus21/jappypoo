/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import javax.vecmath.Vector2f;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.AimTarget;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
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
/*     */ public class BowAimbot
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel targetsLabel;
/*     */   MenuComboBox targets;
/*     */   MenuLabel horizontalLabel;
/*     */   MenuSlider horizontal;
/*     */   MenuLabel verticalLabel;
/*     */   MenuSlider vertical;
/*     */   MenuLabel aimDistanceLabel;
/*     */   MenuSlider aimDistance;
/*     */   MenuLabel fovLabel;
/*     */   MenuSlider fov;
/*     */   MenuCheckbox bowaimbotPrediction;
/*  52 */   long tickcount = 0L;
/*  53 */   Entity locked = null;
/*     */   
/*     */   public BowAimbot(Minecraft minecraft) {
/*  56 */     super(minecraft, "bowaimbot", 0, Category.PAGE_2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  61 */     this.horizontalLabel = new MenuLabel("Horizontal bow smoothing", 0, 0);
/*  62 */     this.horizontal = new MenuSlider(50, 1, 50, 0, 0, 100, 6);
/*  63 */     this.verticalLabel = new MenuLabel("Vertical bow smoothing", 0, 0);
/*  64 */     this.vertical = new MenuSlider(50, 1, 50, 0, 0, 100, 6);
/*  65 */     this.aimDistanceLabel = new MenuLabel("Bow aim distance", 0, 0);
/*  66 */     this.aimDistance = new MenuSlider(200, 1, 300, 0, 0, 100, 6);
/*  67 */     this.fovLabel = new MenuLabel("Bow aimbot FOV", 0, 0);
/*  68 */     this.fov = new MenuSlider(90, 1, 180, 0, 0, 100, 6);
/*  69 */     this.targetsLabel = new MenuLabel("Bow aimbot targets", 0, 0);
/*  70 */     this.targets = new MenuComboBox(AimTarget.class, 0, 0);
/*  71 */     this.bowaimbotPrediction = new MenuCheckbox("Bow prediction", 0, 0, 12, 12);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  76 */     components.add(this.horizontalLabel);
/*  77 */     components.add(this.horizontal);
/*  78 */     components.add(this.verticalLabel);
/*  79 */     components.add(this.vertical);
/*  80 */     components.add(this.aimDistanceLabel);
/*  81 */     components.add(this.aimDistance);
/*  82 */     components.add(this.fovLabel);
/*  83 */     components.add(this.fov);
/*  84 */     components.add(this.bowaimbotPrediction);
/*  85 */     components.add(this.targetsLabel);
/*  86 */     components.add(this.targets);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  91 */     this.locked = null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
/*  96 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 103 */     if (this.mc.player.func_70694_bm() == null) {
/*     */       return;
/*     */     }
/* 106 */     if (!(this.mc.player.func_70694_bm().getItem() instanceof net.minecraft.item.ItemBow)) {
/*     */       return;
/*     */     }
/* 109 */     if (!this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/* 112 */     if (this.locked == null) {
/*     */       return;
/*     */     }
/* 115 */     Vector2f aimpoint = getAimPoint(this.locked, true, this.bowaimbotPrediction.getRValue());
/*     */     
/* 117 */     if (this.tickcount >= 1L) {
/* 118 */       this.tickcount = 0L;
/* 119 */       Vector2f localAngles = getLocalAngles();
/*     */       
/* 121 */       if (this.horizontal.getIntValue() < 50) {
/* 122 */         float negCheck1 = aimpoint.x - localAngles.x;
/* 123 */         float negCheck2 = localAngles.x - aimpoint.x;
/*     */         
/* 125 */         if (negCheck1 < 0.0F)
/* 126 */           negCheck1 += 360.0F; 
/* 127 */         if (negCheck2 < 0.0F) {
/* 128 */           negCheck2 += 360.0F;
/*     */         }
/* 130 */         float xLeft = (negCheck1 < negCheck2) ? negCheck1 : negCheck2;
/*     */         
/* 132 */         xLeft /= (50 - this.horizontal.getIntValue());
/*     */         
/* 134 */         Vector2f safeValues = clampAngles((negCheck1 > negCheck2) ? (localAngles.x - xLeft) : (localAngles.x + xLeft), localAngles.y);
/*     */         
/* 136 */         if (xLeft != 0.0F && !Float.isNaN(safeValues.x))
/* 137 */           this.mc.player.rotationYaw = safeValues.x; 
/*     */       } else {
/* 139 */         this.mc.player.rotationYaw = aimpoint.x;
/*     */       } 
/*     */       
/* 142 */       if (this.vertical.getIntValue() < 50) {
/* 143 */         float yLeft = localAngles.y - aimpoint.y;
/*     */         
/* 145 */         yLeft /= (50 - this.vertical.getIntValue());
/*     */         
/* 147 */         Vector2f safeValues = clampAngles(localAngles.x, localAngles.y - yLeft);
/*     */         
/* 149 */         if (yLeft != 0.0F && !Float.isNaN(safeValues.y))
/* 150 */           this.mc.player.rotationPitch = safeValues.y; 
/*     */       } else {
/* 152 */         this.mc.player.rotationPitch = aimpoint.y;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 159 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 163 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 166 */     if (this.mc.player.func_70694_bm() == null) {
/*     */       return;
/*     */     }
/* 169 */     if (!(this.mc.player.func_70694_bm().getItem() instanceof net.minecraft.item.ItemBow)) {
/*     */       return;
/*     */     }
/* 172 */     if (!this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/* 173 */       this.locked = null;
/*     */       
/*     */       return;
/*     */     } 
/* 177 */     if (this.locked == null || !isValidTarget(this.locked)) {
/* 178 */       Entity target = getBestTarget();
/*     */       
/* 180 */       if (target == null) {
/* 181 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 185 */       if (this.fov.getIntValue() < getFovToEntity(target, true, this.bowaimbotPrediction.getRValue())) {
/* 186 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 190 */       this.locked = target;
/*     */     } 
/* 192 */     this.tickcount++;
/*     */   }
/*     */   
/*     */   public Entity getBestTarget() {
/* 196 */     if (this.mc.world.loadedEntityList == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     Entity finalTarget = null;
/* 200 */     float best = Float.MAX_VALUE;
/*     */     
/* 202 */     for (Entity target : this.mc.world.loadedEntityList) {
/* 203 */       if (isValidTarget(target)) {
/* 204 */         float user = getFovToEntity(target, true, this.bowaimbotPrediction.getRValue());
/*     */         
/* 206 */         if (best > user) {
/* 207 */           best = user;
/* 208 */           finalTarget = target;
/*     */         } 
/*     */       } 
/*     */     } 
/* 212 */     return finalTarget;
/*     */   }
/*     */   
/*     */   public boolean isValidTarget(Entity target) {
/* 216 */     if (((target instanceof EntityPlayer && hasCondition(AimTarget.ENEMIES)) || (target instanceof net.minecraft.entity.passive.EntityVillager && 
/* 217 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityBat && 
/* 218 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntitySlime && 
/* 219 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityMob && 
/* 220 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityIronGolem && 
/* 221 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.passive.EntitySquid && 
/* 222 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityAnimal && 
/* 223 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.boss.EntityDragon && 
/* 224 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityGhast && 
/* 225 */       hasCondition(AimTarget.MOBS))) && target != this.mc.player) {
/*     */ 
/*     */       
/* 228 */       if (((Antibot)AntiAFK.getInstance().getFeature(Antibot.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/* 229 */         return false;
/*     */       }
/*     */       
/* 232 */       if (!target.isInvisible() && this.aimDistance.getValue() > this.mc.player.getDistance(target) && this.mc.player.canEntityBeSeen(target) && !target.isDead) {
/* 233 */         if (target instanceof EntityPlayer) {
/* 234 */           EntityPlayer player = (EntityPlayer)target;
/*     */           
/* 236 */           if (!hasCondition(AimTarget.TEAM_BY_COLOR) && 
/* 237 */             this.mc.player.func_142014_c((EntityLivingBase)player)) {
/* 238 */             return false;
/*     */           }
/*     */           
/* 241 */           if (!hasCondition(AimTarget.RANKED_USERS)) {
/* 242 */             String name = ChatColor.stripColors(player.getDisplayName().getUnformattedText().toUpperCase()).split(" ")[0];
/*     */             
/* 244 */             if (player.getDisplayName().getUnformattedText().contains(" ") && (
/* 245 */               name.contains("ULTRA") || name.contains("HERO") || name.contains("LEGEND") || name.contains("TITAN") || name.contains("ETERNAL") || name.contains("IMMORTAL") || name
/* 246 */               .contains("YT") || name.contains("STREAM") || name.contains("YOUTUBE") || name.contains("KNIGHT") || name.contains("LORD") || name
/* 247 */               .contains("LADY") || name.contains("DUKE") || name.contains("DUCHNESS") || name.contains("TRAINEE") || name.contains("MOD") || name
/* 248 */               .contains("SR.MOD") || name.contains("ADMIN") || name.contains("LEADER") || name.contains("OWNER") || name.contains("BUILDER") || name
/* 249 */               .contains("BUILDLEAD") || name.contains("DEVELOPER") || name.contains("SUPPORT") || name.contains("ARTIST"))) {
/* 250 */               return false;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 255 */           if (!hasCondition(AimTarget.FRIENDS) && 
/* 256 */             (AntiAFK.getInstance()).friends.contains(player.getName())) {
/* 257 */             return false;
/*     */           }
/*     */           
/* 260 */           if (player.isSpectator())
/* 261 */             return false; 
/*     */         } 
/* 263 */         return true;
/*     */       } 
/*     */     } 
/* 266 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/* 270 */     for (String condition : this.targets.getValues()) {
/* 271 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 272 */         return true; 
/*     */     } 
/* 274 */     return false;
/*     */   }
/*     */   
/*     */   public Vector2f getLocalAngles() {
/* 278 */     return new Vector2f(this.mc.player.rotationYaw, this.mc.player.rotationPitch);
/*     */   }
/*     */   
/*     */   public float getFovToEntity(Entity entity, boolean projectile, boolean predictPosition) {
/* 282 */     Vector2f aimpoint = getAimPoint(entity, projectile, predictPosition);
/* 283 */     Vector2f localAngles = getLocalAngles();
/* 284 */     localAngles.x = Math.abs(localAngles.x);
/* 285 */     localAngles.y = Math.abs(localAngles.y);
/* 286 */     aimpoint.x = Math.abs(aimpoint.x);
/* 287 */     aimpoint.y = Math.abs(aimpoint.y);
/* 288 */     return Math.max(localAngles.x, aimpoint.x) - Math.min(localAngles.x, aimpoint.x) + Math.max(localAngles.y, aimpoint.y) - Math.min(localAngles.y, aimpoint.y);
/*     */   }
/*     */   
/*     */   public Vector2f getAimPoint(Entity entity, boolean projectile, boolean predictPosition) {
/* 292 */     double x = entity.posX - this.mc.player.posX;
/* 293 */     double y = entity.posY - this.mc.player.posY - (this.mc.player.getEyeHeight() - entity.getEyeHeight());
/* 294 */     double z = entity.posZ - this.mc.player.posZ;
/* 295 */     double sq = Math.sqrt(x * x + z * z);
/* 296 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 297 */     if (projectile || predictPosition) {
/* 298 */       if (predictPosition) {
/* 299 */         double eyePosDist = this.mc.player.getPositionEyes(1.0F).distanceTo(getCenter(entity.getEntityBoundingBox()));
/*     */         
/* 301 */         if (entity.prevPosX != entity.posX) {
/* 302 */           x = entity.posX + (entity.posX - entity.prevPosX) * eyePosDist - this.mc.player.posX;
/*     */         }
/* 304 */         if (entity.prevPosZ != entity.posZ) {
/* 305 */           z = entity.posZ + (entity.posZ - entity.prevPosZ) * eyePosDist - this.mc.player.posZ;
/*     */         }
/*     */       } 
/* 308 */       if (projectile) {
/* 309 */         float velocity = getArrowVelocity(this.mc.player.func_70694_bm().getMaxItemUseDuration() - this.mc.player.func_71052_bv());
/* 310 */         float g = 0.006F;
/* 311 */         double distanceSq = Math.pow(sq, 2.0D);
/* 312 */         double velocitySq = Math.pow(velocity, 2.0D);
/*     */         
/* 314 */         float newPitch = (float)-Math.toDegrees(Math.atan2(velocitySq - Math.sqrt(Math.pow(velocity, 4.0D) - g * (g * distanceSq + 2.0D * y * velocitySq)), g * sq));
/* 315 */         if (!Float.isNaN(newPitch)) {
/* 316 */           pitch = newPitch;
/*     */         }
/*     */       } 
/*     */     } 
/* 320 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/* 321 */     return clampAngles(yaw, pitch);
/*     */   }
/*     */   
/*     */   public static float getArrowVelocity(int charge) {
/* 325 */     float f = charge / 20.0F;
/* 326 */     f = (f * f + f * 2.0F) / 3.0F;
/*     */     
/* 328 */     if (f > 1.0F) {
/* 329 */       f = 1.0F;
/*     */     }
/*     */     
/* 332 */     return f;
/*     */   }
/*     */   
/*     */   public Vec3 getCenter(AxisAlignedBB bb) {
/* 336 */     return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5D, bb.minY + (bb.maxY - bb.minY) * 0.5D, bb.minZ + (bb.maxZ - bb.minZ) * 0.5D);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vector2f clampAngles(float yaw, float pitch) {
/* 341 */     float pitchDelta = clamp(pitch - this.mc.player.rotationPitch, 90.0F);
/* 342 */     float yawDelta = clamp(MathHelper.wrapDegrees(yaw - this.mc.player.rotationYaw), 180.0F);
/*     */     
/* 344 */     return new Vector2f(this.mc.player.rotationYaw + yawDelta, this.mc.player.rotationPitch + pitchDelta);
/*     */   }
/*     */   
/*     */   public float clamp(float value, float max) {
/* 348 */     if (value > max) {
/* 349 */       value = max;
/*     */     }
/* 351 */     if (value < -max) {
/* 352 */       value = -max;
/*     */     }
/* 354 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\BowAimbot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */