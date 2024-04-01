/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import javax.vecmath.Vector2f;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.AimTarget;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Killaura
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel cpsMinLabel;
/*     */   MenuSlider cpsMin;
/*     */   MenuLabel cpsMaxLabel;
/*     */   MenuSlider cpsMax;
/*     */   MenuLabel maxRangeLabel;
/*     */   MenuSlider maxRange;
/*     */   MenuLabel fovLabel;
/*     */   MenuSlider fov;
/*     */   MenuLabel targetsLabel;
/*     */   MenuComboBox targets;
/*     */   MenuLabel maxConcurrentLabel;
/*     */   MenuSlider concurrent;
/*     */   MenuLabel settingsLabel;
/*     */   MenuComboBox settings;
/*     */   int cps;
/*     */   Entity locked;
/*     */   long lastHit;
/*     */   int ticksRemaining;
/*     */   int interval;
/*     */   Random random;
/*     */   
/*     */   public Killaura(Minecraft minecraft) {
/*  65 */     super(minecraft, "killaura", 0, Category.PAGE_1);
/*  66 */     this.random = new Random();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  71 */     this.cpsMinLabel = new MenuLabel("Killaura min cps", 0, 0);
/*  72 */     this.cpsMin = new MenuSlider(1, 1, 20, 0, 0, 100, 6);
/*  73 */     this.cpsMaxLabel = new MenuLabel("Killaura max cps", 0, 0);
/*  74 */     this.cpsMax = new MenuSlider(1, 1, 20, 0, 0, 100, 6);
/*  75 */     this.maxRangeLabel = new MenuLabel("Killaura max range", 0, 0);
/*  76 */     this.maxRange = new MenuSlider(1.0F, 1.0F, 6.0F, 1, 0, 0, 100, 6);
/*  77 */     this.fovLabel = new MenuLabel("Killaura fov", 0, 0);
/*  78 */     this.fov = new MenuSlider(180, 1, 180, 0, 0, 100, 6);
/*  79 */     this.targetsLabel = new MenuLabel("Killaura targets", 0, 0);
/*  80 */     this.targets = new MenuComboBox(AimTarget.class, 0, 0);
/*  81 */     this.maxConcurrentLabel = new MenuLabel("Multiaura max concurrent entities", 0, 0);
/*  82 */     this.concurrent = new MenuSlider(5, 1, 15, 0, 0, 100, 6);
/*  83 */     this.settingsLabel = new MenuLabel("Killaura settings", 0, 0);
/*  84 */     this.settings = new MenuComboBox(Settings.class, 0, 0);
/*     */   }
/*     */   
/*     */   enum Settings {
/*  88 */     ON_MOVE_ONLY, ONLY_ON_GROUND, DISABLE_IN_WATER, ONLY_WHEN_SPRINTING, MULTIAURA, HIT_THROUGH_BLOCKS, LEFT_CLICK_REQUIRED, DISABLE_ON_BREAK_BLOCK;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Settings cond) {
/*  92 */     for (String condition : this.settings.getValues()) {
/*  93 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  94 */         return true; 
/*     */     } 
/*  96 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 102 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     float range = this.maxRange.getValue();
/*     */     
/* 108 */     if (hasCondition(Settings.ONLY_ON_GROUND) && 
/* 109 */       !this.mc.player.onGround) {
/* 110 */       range = 3.0F;
/*     */     }
/*     */ 
/*     */     
/* 114 */     if (hasCondition(Settings.DISABLE_IN_WATER) && 
/* 115 */       this.mc.player.isInWater()) {
/* 116 */       range = 3.0F;
/*     */     }
/*     */ 
/*     */     
/* 120 */     if (hasCondition(Settings.ONLY_WHEN_SPRINTING) && 
/* 121 */       !this.mc.player.isSprinting()) {
/* 122 */       range = 3.0F;
/*     */     }
/*     */ 
/*     */     
/* 126 */     if (hasCondition(Settings.ON_MOVE_ONLY) && this.mc.player.posX == this.mc.player.prevPosX && this.mc.player.posY == this.mc.player.prevPosY && this.mc.player.posZ == this.mc.player.prevPosZ) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     if (hasCondition(Settings.LEFT_CLICK_REQUIRED) && 
/* 131 */       !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 136 */     if (hasCondition(Settings.DISABLE_ON_BREAK_BLOCK) && 
/* 137 */       this.mc.objectMouseOver != null && 
/* 138 */       this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && 
/* 139 */       this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 146 */     if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */     
/* 150 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 153 */     if (this.locked == null || !isValidTarget(this.locked, range)) {
/* 154 */       Entity target = getBestTarget(range);
/*     */       
/* 156 */       if (target == null) {
/* 157 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 161 */       if (this.fov.getIntValue() < getFovToEntity(target)) {
/* 162 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 166 */       this.locked = target;
/*     */     }
/* 168 */     else if (this.fov.getIntValue() < getFovToEntity(this.locked)) {
/* 169 */       this.locked = null;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 174 */     Keystrokes strokes = (Keystrokes)AntiAFK.getInstance().getFeature(Keystrokes.class);
/*     */     
/* 176 */     if (this.ticksRemaining > 0) {
/* 177 */       this.ticksRemaining--;
/*     */     } else {
/* 179 */       int min = this.cpsMin.getIntValue();
/* 180 */       int max = this.cpsMax.getIntValue();
/*     */       
/* 182 */       int cps = min;
/*     */       
/* 184 */       if (max > min) {
/* 185 */         cps += this.random.nextInt(max - min);
/*     */       }
/*     */       
/* 188 */       this.cps = cps;
/* 189 */       this.interval = 1000 / cps;
/* 190 */       this.ticksRemaining = 20;
/*     */     } 
/*     */     
/* 193 */     if (this.locked != null) {
/* 194 */       if (hasCondition(Settings.MULTIAURA)) {
/* 195 */         if (System.currentTimeMillis() - this.lastHit > this.interval) {
/* 196 */           int hit = 0;
/* 197 */           for (Entity target : this.mc.world.loadedEntityList) {
/* 198 */             if (isValidTarget(target, range)) {
/* 199 */               attack(target, strokes);
/* 200 */               hit++;
/*     */               
/* 202 */               if (hit > this.concurrent.getIntValue()) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           } 
/*     */           
/* 208 */           this.lastHit = System.currentTimeMillis();
/*     */         }
/*     */       
/* 211 */       } else if (System.currentTimeMillis() - this.lastHit > this.interval) {
/* 212 */         attack(this.locked, strokes);
/* 213 */         this.lastHit = System.currentTimeMillis();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void attack(Entity entity, Keystrokes strokes) {
/* 220 */     if (strokes.isEnabled()) {
/* 221 */       strokes.leftClick();
/*     */     }
/*     */     
/* 224 */     this.mc.player.func_71038_i();
/* 225 */     this.mc.playerController.attackEntity((EntityPlayer)this.mc.player, entity);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/* 230 */     components.add(this.cpsMinLabel);
/* 231 */     components.add(this.cpsMin);
/* 232 */     components.add(this.cpsMaxLabel);
/* 233 */     components.add(this.cpsMax);
/* 234 */     components.add(this.maxRangeLabel);
/* 235 */     components.add(this.maxRange);
/* 236 */     components.add(this.fovLabel);
/* 237 */     components.add(this.fov);
/* 238 */     components.add(this.targetsLabel);
/* 239 */     components.add(this.targets);
/* 240 */     components.add(this.maxConcurrentLabel);
/* 241 */     components.add(this.concurrent);
/* 242 */     components.add(this.settingsLabel);
/* 243 */     components.add(this.settings);
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/* 247 */     for (String condition : this.targets.getValues()) {
/* 248 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 249 */         return true; 
/*     */     } 
/* 251 */     return false;
/*     */   }
/*     */   
/*     */   public Entity getBestTarget(float range) {
/* 255 */     if (this.mc.world.loadedEntityList == null) {
/* 256 */       return null;
/*     */     }
/* 258 */     Entity finalTarget = null;
/* 259 */     float best = Float.MAX_VALUE;
/*     */     
/* 261 */     for (Entity target : this.mc.world.loadedEntityList) {
/* 262 */       if (isValidTarget(target, range)) {
/* 263 */         float user = getFovToEntity(target);
/*     */         
/* 265 */         if (best > user) {
/* 266 */           best = user;
/* 267 */           finalTarget = target;
/*     */         } 
/*     */       } 
/*     */     } 
/* 271 */     return finalTarget;
/*     */   }
/*     */   
/*     */   public Vector2f getLocalAngles() {
/* 275 */     return new Vector2f(this.mc.player.rotationYaw, this.mc.player.rotationPitch);
/*     */   }
/*     */   
/*     */   public float getFovToEntity(Entity entity) {
/* 279 */     Vector2f aimpoint = getAimPoint(entity);
/* 280 */     Vector2f localAngles = getLocalAngles();
/* 281 */     localAngles.x = Math.abs(localAngles.x);
/* 282 */     localAngles.y = Math.abs(localAngles.y);
/* 283 */     aimpoint.x = Math.abs(aimpoint.x);
/* 284 */     aimpoint.y = Math.abs(aimpoint.y);
/* 285 */     return Math.max(localAngles.x, aimpoint.x) - Math.min(localAngles.x, aimpoint.x) + Math.max(localAngles.y, aimpoint.y) - Math.min(localAngles.y, aimpoint.y);
/*     */   }
/*     */   
/*     */   private float getYModifier(Entity entity, Hitbox hitbox) {
/* 289 */     switch (hitbox) {
/*     */       case HEAD:
/* 291 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight());
/*     */       
/*     */       case THORAX:
/* 294 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 1.25F);
/*     */       
/*     */       case FEET:
/* 297 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 5.0F);
/*     */       
/*     */       case LEGS:
/* 300 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 3.0F);
/*     */     } 
/*     */     
/* 303 */     return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAimPos(Entity entity, float offset) {
/* 308 */     return (float)(entity.posY - this.mc.player.posY - offset);
/*     */   }
/*     */   
/*     */   public Vector2f getAimPoint(Entity entity) {
/* 312 */     double x = entity.posX - this.mc.player.posX;
/* 313 */     double y = getYModifier(entity, Hitbox.HEAD);
/* 314 */     double z = entity.posZ - this.mc.player.posZ;
/* 315 */     double sq = Math.sqrt(x * x + z * z);
/* 316 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 317 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/* 318 */     return clampAngles(yaw, pitch);
/*     */   }
/*     */   
/*     */   public Vector2f clampAngles(float yaw, float pitch) {
/* 322 */     float pitchDelta = clamp(pitch - this.mc.player.rotationPitch, 90.0F);
/* 323 */     float yawDelta = clamp(MathHelper.wrapDegrees(yaw - this.mc.player.rotationYaw), 180.0F);
/*     */     
/* 325 */     return new Vector2f(this.mc.player.rotationYaw + yawDelta, this.mc.player.rotationPitch + pitchDelta);
/*     */   }
/*     */   
/*     */   public float clamp(float value, float max) {
/* 329 */     if (value > max) {
/* 330 */       value = max;
/*     */     }
/* 332 */     if (value < -max) {
/* 333 */       value = -max;
/*     */     }
/* 335 */     return value;
/*     */   }
/*     */   
/*     */   enum Hitbox {
/* 339 */     ADAPTIVE_HITBOX, HEAD, THORAX, LEGS, FEET;
/*     */   }
/*     */   
/*     */   public boolean isValidTarget(Entity target, float range) {
/* 343 */     if (((target instanceof EntityPlayer && hasCondition(AimTarget.ENEMIES)) || (target instanceof net.minecraft.entity.passive.EntityVillager && 
/* 344 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityBat && 
/* 345 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntitySlime && 
/* 346 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityMob && 
/* 347 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityIronGolem && 
/* 348 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.passive.EntitySquid && 
/* 349 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityAnimal && 
/* 350 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.boss.EntityDragon && 
/* 351 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityGhast && 
/* 352 */       hasCondition(AimTarget.MOBS))) && target != this.mc.player) {
/*     */ 
/*     */       
/* 355 */       if (((Antibot)AntiAFK.getInstance().getFeature(Antibot.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/* 356 */         return false;
/*     */       }
/*     */       
/* 359 */       if (((AntiEvade)AntiAFK.getInstance().getFeature(AntiEvade.class)).isEntityEvading(target)) {
/* 360 */         return false;
/*     */       }
/*     */       
/* 363 */       if (((AntiDisengage)AntiAFK.getInstance().getFeature(AntiDisengage.class)).isEntityEvading(target)) {
/* 364 */         return false;
/*     */       }
/*     */       
/* 367 */       if (target instanceof me.onefightonewin.entities.EntityFakePlayer) {
/* 368 */         return false;
/*     */       }
/*     */       
/* 371 */       if (!target.isInvisible() && range > this.mc.player.getDistance(target) && !target.isDead) {
/* 372 */         if (target instanceof EntityPlayer) {
/* 373 */           EntityPlayer player = (EntityPlayer)target;
/*     */           
/* 375 */           if (!this.mc.player.canEntityBeSeen(target)) {
/* 376 */             boolean valid = false;
/*     */             
/* 378 */             if (hasCondition(Settings.HIT_THROUGH_BLOCKS) && 
/* 379 */               this.mc.player.getDistance(target) <= 3.0F) {
/* 380 */               valid = true;
/*     */             }
/*     */ 
/*     */             
/* 384 */             if (!valid) {
/* 385 */               return false;
/*     */             }
/*     */           } 
/*     */           
/* 389 */           if (!hasCondition(AimTarget.TEAM_BY_COLOR) && 
/* 390 */             this.mc.player.func_142014_c((EntityLivingBase)player)) {
/* 391 */             return false;
/*     */           }
/*     */           
/* 394 */           if (!hasCondition(AimTarget.RANKED_USERS)) {
/* 395 */             String name = ChatColor.stripColors(player.getDisplayName().getUnformattedText().toUpperCase()).split(" ")[0];
/*     */             
/* 397 */             if (player.getDisplayName().getUnformattedText().contains(" ") && (
/* 398 */               name.contains("ULTRA") || name.contains("HERO") || name.contains("LEGEND") || name.contains("TITAN") || name.contains("ETERNAL") || name.contains("IMMORTAL") || name
/* 399 */               .contains("YT") || name.contains("STREAM") || name.contains("YOUTUBE") || name.contains("KNIGHT") || name.contains("LORD") || name
/* 400 */               .contains("LADY") || name.contains("DUKE") || name.contains("DUCHNESS") || name.contains("TRAINEE") || name.contains("MOD") || name
/* 401 */               .contains("SR.MOD") || name.contains("ADMIN") || name.contains("LEADER") || name.contains("OWNER") || name.contains("BUILDER") || name
/* 402 */               .contains("BUILDLEAD") || name.contains("DEVELOPER") || name.contains("SUPPORT") || name.contains("ARTIST"))) {
/* 403 */               return false;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 408 */           if (!hasCondition(AimTarget.FRIENDS) && 
/* 409 */             (AntiAFK.getInstance()).friends.contains(player.getName())) {
/* 410 */             return false;
/*     */           }
/*     */           
/* 413 */           if (player.isSpectator())
/* 414 */             return false; 
/*     */         } 
/* 416 */         return true;
/*     */       } 
/*     */     } 
/* 419 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Killaura.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */