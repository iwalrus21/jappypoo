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
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
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
/*     */ public class Aimassist
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
/*     */   MenuLabel optionsLabel;
/*     */   MenuComboBox options;
/*     */   MenuLabel hitboxLabel;
/*     */   MenuDropdown hitbox;
/*  54 */   long tickcount = 0L;
/*  55 */   Entity locked = null;
/*     */   
/*     */   public Aimassist(Minecraft minecraft) {
/*  58 */     super(minecraft, "aimassist", 0, Category.PAGE_2);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  63 */     this.horizontalLabel = new MenuLabel("Horizontal smoothing", 0, 0);
/*  64 */     this.horizontal = new MenuSlider(50, 1, 180, 0, 0, 100, 6);
/*  65 */     this.verticalLabel = new MenuLabel("Vertical smoothing", 0, 0);
/*  66 */     this.vertical = new MenuSlider(50, 1, 90, 0, 0, 100, 6);
/*  67 */     this.aimDistanceLabel = new MenuLabel("Aim distance", 0, 0);
/*  68 */     this.aimDistance = new MenuSlider(3.0F, 3.0F, 6.0F, 1, 0, 0, 100, 6);
/*  69 */     this.fovLabel = new MenuLabel("Aimassist FOV", 0, 0);
/*  70 */     this.fov = new MenuSlider(90, 1, 180, 0, 0, 100, 6);
/*  71 */     this.targetsLabel = new MenuLabel("Aimassist targets", 0, 0);
/*  72 */     this.targets = new MenuComboBox(AimTarget.class, 0, 0);
/*  73 */     this.optionsLabel = new MenuLabel("Aim assist options", 0, 0);
/*  74 */     this.options = new MenuComboBox(Options.class, 0, 0);
/*  75 */     this.hitboxLabel = new MenuLabel("Aim assist hitbox", 0, 0);
/*  76 */     this.hitbox = new MenuDropdown(Hitbox.class, 0, 0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  82 */     components.add(this.horizontalLabel);
/*  83 */     components.add(this.horizontal);
/*  84 */     components.add(this.verticalLabel);
/*  85 */     components.add(this.vertical);
/*  86 */     components.add(this.aimDistanceLabel);
/*  87 */     components.add(this.aimDistance);
/*  88 */     components.add(this.fovLabel);
/*  89 */     components.add(this.fov);
/*  90 */     components.add(this.targetsLabel);
/*  91 */     components.add(this.targets);
/*  92 */     components.add(this.optionsLabel);
/*  93 */     components.add(this.options);
/*  94 */     components.add(this.hitboxLabel);
/*  95 */     components.add(this.hitbox);
/*     */   }
/*     */   
/*     */   enum Options {
/*  99 */     LEFT_CLICK_REQUIRED, IGNORE_FOV_AIMLOCK, DISABLE_ON_BREAK_BLOCKS;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Options cond) {
/* 103 */     for (String condition : this.options.getValues()) {
/* 104 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 105 */         return true; 
/*     */     } 
/* 107 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 112 */     this.locked = null;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
/* 117 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 121 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 124 */     if (hasCondition(Options.LEFT_CLICK_REQUIRED) && 
/* 125 */       !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 130 */     if (hasCondition(Options.DISABLE_ON_BREAK_BLOCKS) && 
/* 131 */       this.mc.objectMouseOver != null && 
/* 132 */       this.mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && 
/* 133 */       this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 140 */     if (this.locked == null) {
/*     */       return;
/*     */     }
/* 143 */     Vector2f aimpoint = getAimPoint(this.locked);
/*     */     
/* 145 */     if (this.tickcount >= 1L) {
/* 146 */       this.tickcount = 0L;
/* 147 */       Vector2f localAngles = getLocalAngles();
/*     */       
/* 149 */       if (this.horizontal.getIntValue() < this.horizontal.getMaxValue()) {
/* 150 */         float negCheck1 = aimpoint.x - localAngles.x;
/* 151 */         float negCheck2 = localAngles.x - aimpoint.x;
/*     */         
/* 153 */         if (negCheck1 < 0.0F)
/* 154 */           negCheck1 += 360.0F; 
/* 155 */         if (negCheck2 < 0.0F) {
/* 156 */           negCheck2 += 360.0F;
/*     */         }
/* 158 */         float xLeft = (negCheck1 < negCheck2) ? negCheck1 : negCheck2;
/*     */         
/* 160 */         if (Math.abs(xLeft) > 0.0F) {
/* 161 */           xLeft = Math.min(this.horizontal.getValue(), xLeft);
/*     */         }
/*     */         
/* 164 */         Vector2f safeValues = clampAngles((negCheck1 > negCheck2) ? (localAngles.x - xLeft) : (localAngles.x + xLeft), localAngles.y);
/*     */         
/* 166 */         if (xLeft != 0.0F && !Float.isNaN(safeValues.x))
/* 167 */           this.mc.player.rotationYaw = safeValues.x; 
/*     */       } else {
/* 169 */         this.mc.player.rotationYaw = aimpoint.x;
/*     */       } 
/*     */       
/* 172 */       if (this.vertical.getIntValue() < this.vertical.getMaxValue()) {
/* 173 */         float yLeft = localAngles.y - aimpoint.y;
/*     */         
/* 175 */         if (Math.abs(yLeft) > 0.0F) {
/* 176 */           boolean orgMinus = (yLeft < 0.0F);
/*     */           
/* 178 */           yLeft = Math.min(this.vertical.getValue(), Math.abs(yLeft));
/*     */           
/* 180 */           if (orgMinus) {
/* 181 */             yLeft = -yLeft;
/*     */           }
/*     */         } 
/* 184 */         Vector2f safeValues = clampAngles(localAngles.x, localAngles.y - yLeft);
/*     */         
/* 186 */         if (yLeft != 0.0F && !Float.isNaN(safeValues.y))
/* 187 */           this.mc.player.rotationPitch = safeValues.y; 
/*     */       } else {
/* 189 */         this.mc.player.rotationPitch = aimpoint.y;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 196 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 200 */     if (this.mc.playerController.isSpectator()) {
/*     */       return;
/*     */     }
/* 203 */     if (hasCondition(Options.LEFT_CLICK_REQUIRED) && 
/* 204 */       !this.mc.gameSettings.keyBindAttack.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 209 */     if (this.locked == null || !isValidTarget(this.locked)) {
/* 210 */       Entity target = getBestTarget();
/*     */       
/* 212 */       if (target == null) {
/* 213 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 217 */       if (this.fov.getIntValue() < getFovToEntity(target)) {
/* 218 */         this.locked = null;
/*     */         
/*     */         return;
/*     */       } 
/* 222 */       this.locked = target;
/*     */     }
/* 224 */     else if (this.fov.getIntValue() < getFovToEntity(this.locked) && !hasCondition(Options.IGNORE_FOV_AIMLOCK)) {
/* 225 */       this.locked = null;
/*     */       
/*     */       return;
/*     */     } 
/* 229 */     this.tickcount++;
/*     */   }
/*     */   
/*     */   public Entity getBestTarget() {
/* 233 */     if (this.mc.world.loadedEntityList == null) {
/* 234 */       return null;
/*     */     }
/* 236 */     Entity finalTarget = null;
/* 237 */     float best = Float.MAX_VALUE;
/*     */     
/* 239 */     for (Entity target : this.mc.world.loadedEntityList) {
/* 240 */       if (isValidTarget(target)) {
/* 241 */         float user = getFovToEntity(target);
/*     */         
/* 243 */         if (best > user) {
/* 244 */           best = user;
/* 245 */           finalTarget = target;
/*     */         } 
/*     */       } 
/*     */     } 
/* 249 */     return finalTarget;
/*     */   }
/*     */   
/*     */   public boolean isValidTarget(Entity target) {
/* 253 */     if (((target instanceof EntityPlayer && hasCondition(AimTarget.ENEMIES)) || (target instanceof net.minecraft.entity.passive.EntityVillager && 
/* 254 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityBat && 
/* 255 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntitySlime && 
/* 256 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityMob && 
/* 257 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityIronGolem && 
/* 258 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.passive.EntitySquid && 
/* 259 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityAnimal && 
/* 260 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.boss.EntityDragon && 
/* 261 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityGhast && 
/* 262 */       hasCondition(AimTarget.MOBS))) && target != this.mc.player) {
/*     */ 
/*     */       
/* 265 */       if (target instanceof me.onefightonewin.entities.EntityFakePlayer) {
/* 266 */         return false;
/*     */       }
/*     */       
/* 269 */       if (((Antibot)AntiAFK.getInstance().getFeature(Antibot.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/* 270 */         return false;
/*     */       }
/*     */       
/* 273 */       if (!target.isInvisible() && this.aimDistance.getValue() > this.mc.player.getDistance(target) && this.mc.player.canEntityBeSeen(target) && !target.isDead) {
/* 274 */         if (target instanceof EntityPlayer) {
/* 275 */           EntityPlayer player = (EntityPlayer)target;
/*     */           
/* 277 */           if (!hasCondition(AimTarget.TEAM_BY_COLOR) && 
/* 278 */             this.mc.player.func_142014_c((EntityLivingBase)player)) {
/* 279 */             return false;
/*     */           }
/*     */           
/* 282 */           if (!hasCondition(AimTarget.RANKED_USERS)) {
/* 283 */             String name = ChatColor.stripColors(player.getDisplayName().getUnformattedText().toUpperCase()).split(" ")[0];
/*     */             
/* 285 */             if (player.getDisplayName().getUnformattedText().contains(" ") && (
/* 286 */               name.contains("ULTRA") || name.contains("HERO") || name.contains("LEGEND") || name.contains("TITAN") || name.contains("ETERNAL") || name.contains("IMMORTAL") || name
/* 287 */               .contains("YT") || name.contains("STREAM") || name.contains("YOUTUBE") || name.contains("KNIGHT") || name.contains("LORD") || name
/* 288 */               .contains("LADY") || name.contains("DUKE") || name.contains("DUCHNESS") || name.contains("TRAINEE") || name.contains("MOD") || name
/* 289 */               .contains("SR.MOD") || name.contains("ADMIN") || name.contains("LEADER") || name.contains("OWNER") || name.contains("BUILDER") || name
/* 290 */               .contains("BUILDLEAD") || name.contains("DEVELOPER") || name.contains("SUPPORT") || name.contains("ARTIST"))) {
/* 291 */               return false;
/*     */             }
/*     */           } 
/*     */ 
/*     */           
/* 296 */           if (!hasCondition(AimTarget.FRIENDS) && 
/* 297 */             (AntiAFK.getInstance()).friends.contains(player.getName())) {
/* 298 */             return false;
/*     */           }
/*     */           
/* 301 */           if (player.isSpectator())
/* 302 */             return false; 
/*     */         } 
/* 304 */         return true;
/*     */       } 
/*     */     } 
/* 307 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/* 311 */     for (String condition : this.targets.getValues()) {
/* 312 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 313 */         return true; 
/*     */     } 
/* 315 */     return false;
/*     */   }
/*     */   
/*     */   public Vector2f getLocalAngles() {
/* 319 */     return new Vector2f(this.mc.player.rotationYaw, this.mc.player.rotationPitch);
/*     */   }
/*     */   
/*     */   public float getFovToEntity(Entity entity) {
/* 323 */     Vector2f aimpoint = getAimPoint(entity);
/* 324 */     Vector2f localAngles = getLocalAngles();
/* 325 */     localAngles.x = Math.abs(localAngles.x);
/* 326 */     localAngles.y = Math.abs(localAngles.y);
/* 327 */     aimpoint.x = Math.abs(aimpoint.x);
/* 328 */     aimpoint.y = Math.abs(aimpoint.y);
/* 329 */     return Math.max(localAngles.x, aimpoint.x) - Math.min(localAngles.x, aimpoint.x) + Math.max(localAngles.y, aimpoint.y) - Math.min(localAngles.y, aimpoint.y);
/*     */   }
/*     */   
/*     */   public Vector2f getAimPoint(Entity entity) {
/* 333 */     double x = entity.posX - this.mc.player.posX;
/* 334 */     double y = entity.posY - this.mc.player.posY - (this.mc.player.getEyeHeight() - entity.getEyeHeight() / 3.0F * 2.0F);
/*     */     
/* 336 */     if (this.hitbox.getValue().equalsIgnoreCase(Hitbox.ADAPTIVE_HITBOX.toString())) {
/* 337 */       if (!entity.onGround) {
/* 338 */         if (entity.getDistance((Entity)this.mc.player) <= 2.0F) {
/* 339 */           y = getYModifier(entity, Hitbox.LEGS);
/*     */         } else {
/* 341 */           y = getYModifier(entity, Hitbox.LEGS);
/*     */         } 
/* 343 */       } else if (entity.isSneaking()) {
/* 344 */         y = getYModifier(entity, Hitbox.HEAD);
/*     */       }
/*     */     
/* 347 */     } else if (this.hitbox.getValue().trim().length() == 0) {
/* 348 */       y = getYModifier(entity, Hitbox.HEAD);
/*     */     } else {
/*     */       try {
/* 351 */         y = getYModifier(entity, Hitbox.valueOf(this.hitbox.getValue().toUpperCase()));
/* 352 */       } catch (IllegalArgumentException illegalArgumentException) {}
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 358 */     double z = entity.posZ - this.mc.player.posZ;
/* 359 */     double sq = Math.sqrt(x * x + z * z);
/* 360 */     float pitch = (float)-Math.toDegrees(Math.atan2(y, sq));
/* 361 */     float yaw = (float)Math.toDegrees(Math.atan2(z, x)) - 90.0F;
/* 362 */     return clampAngles(yaw, pitch);
/*     */   }
/*     */   
/*     */   enum Hitbox {
/* 366 */     ADAPTIVE_HITBOX, HEAD, THORAX, LEGS, FEET;
/*     */   }
/*     */   
/*     */   private float getYModifier(Entity entity, Hitbox hitbox) {
/* 370 */     switch (hitbox) {
/*     */       case HEAD:
/* 372 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight());
/*     */       
/*     */       case THORAX:
/* 375 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 1.25F);
/*     */       
/*     */       case FEET:
/* 378 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 5.0F);
/*     */       
/*     */       case LEGS:
/* 381 */         return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight() / 3.0F);
/*     */     } 
/*     */     
/* 384 */     return getAimPos(entity, this.mc.player.getEyeHeight() - entity.getEyeHeight());
/*     */   }
/*     */ 
/*     */   
/*     */   public float getAimPos(Entity entity, float offset) {
/* 389 */     return (float)(entity.posY - this.mc.player.posY - offset);
/*     */   }
/*     */   
/*     */   public Vector2f clampAngles(float yaw, float pitch) {
/* 393 */     float pitchDelta = clamp(pitch - this.mc.player.rotationPitch, 90.0F);
/* 394 */     float yawDelta = clamp(MathHelper.wrapDegrees(yaw - this.mc.player.rotationYaw), 180.0F);
/*     */     
/* 396 */     return new Vector2f(this.mc.player.rotationYaw + yawDelta, this.mc.player.rotationPitch + pitchDelta);
/*     */   }
/*     */   
/*     */   public float clamp(float value, float max) {
/* 400 */     if (value > max) {
/* 401 */       value = max;
/*     */     }
/* 403 */     if (value < -max) {
/* 404 */       value = -max;
/*     */     }
/* 406 */     return value;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Aimassist.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */