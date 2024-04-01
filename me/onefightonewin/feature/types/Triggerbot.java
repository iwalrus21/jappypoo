/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Random;
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
/*     */ import net.minecraft.util.MovingObjectPosition;
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
/*     */ public class Triggerbot
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel swingDistLabel;
/*     */   MenuSlider swingDist;
/*     */   MenuLabel hitDistLabel;
/*     */   MenuSlider hitDist;
/*     */   MenuLabel cpsMinLabel;
/*     */   MenuSlider cpsMin;
/*     */   MenuLabel cpsMaxLabel;
/*     */   MenuSlider cpsMax;
/*     */   MenuLabel targetsLabel;
/*     */   MenuComboBox targets;
/*     */   MenuLabel settingsLabel;
/*     */   MenuComboBox settings;
/*     */   final Method method;
/*     */   final Field field;
/*     */   Random random;
/*     */   int interval;
/*     */   int ticksRemaining;
/*     */   int cps;
/*     */   long lastTime;
/*     */   
/*     */   public Triggerbot(Minecraft minecraft) {
/*  61 */     super(minecraft, "triggerbot", 0, Category.PAGE_1);
/*  62 */     this.random = new Random();
/*  63 */     this.method = ReflectionHelper.findMethod(Minecraft.class, minecraft, new String[] { "clickMouse", "func_147116_af" }, new Class[0]);
/*  64 */     this.field = ReflectionHelper.findField(Minecraft.class, new String[] { "leftClickCounter", "field_71429_W" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  69 */     this.swingDistLabel = new MenuLabel("Triggerbot swing distance", 0, 0);
/*  70 */     this.swingDist = new MenuSlider(1.0F, 1.0F, 6.0F, 1, 0, 0, 100, 6);
/*  71 */     this.hitDistLabel = new MenuLabel("Triggerbot hit distance", 0, 0);
/*  72 */     this.hitDist = new MenuSlider(1.0F, 1.0F, 6.0F, 1, 0, 0, 100, 6);
/*  73 */     this.cpsMinLabel = new MenuLabel("Triggerbot CPS Min", 0, 0);
/*  74 */     this.cpsMin = new MenuSlider(1, 1, 30, 0, 0, 100, 6);
/*  75 */     this.cpsMaxLabel = new MenuLabel("Triggerbot CPS Max", 0, 0);
/*  76 */     this.cpsMax = new MenuSlider(1, 1, 30, 0, 0, 100, 6);
/*  77 */     this.targetsLabel = new MenuLabel("Triggerbot targets", 0, 0);
/*  78 */     this.targets = new MenuComboBox(AimTarget.class, 0, 0);
/*  79 */     this.settingsLabel = new MenuLabel("Triggerbot settings", 0, 0);
/*  80 */     this.settings = new MenuComboBox(Settings.class, 0, 0);
/*     */   }
/*     */   
/*     */   enum Settings {
/*  84 */     ONLY_ON_GROUND, DISABLE_IN_WATER, ONLY_WHEN_SPRINTING;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Settings cond) {
/*  88 */     for (String condition : this.settings.getValues()) {
/*  89 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  90 */         return true; 
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  98 */     components.add(this.swingDistLabel);
/*  99 */     components.add(this.swingDist);
/* 100 */     components.add(this.hitDistLabel);
/* 101 */     components.add(this.hitDist);
/* 102 */     components.add(this.cpsMinLabel);
/* 103 */     components.add(this.cpsMin);
/* 104 */     components.add(this.cpsMaxLabel);
/* 105 */     components.add(this.cpsMax);
/* 106 */     components.add(this.targetsLabel);
/* 107 */     components.add(this.targets);
/* 108 */     components.add(this.settingsLabel);
/* 109 */     components.add(this.settings);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 114 */     this.lastTime = 0L;
/* 115 */     this.cps = 0;
/* 116 */     this.ticksRemaining = 0;
/* 117 */     this.interval = 0;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/* 122 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/* 126 */     Reach reach = (Reach)AntiAFK.getInstance().getFeature(Reach.class);
/*     */     
/* 128 */     MovingObjectPosition obj = this.mc.objectMouseOver;
/*     */     
/* 130 */     boolean valid = false;
/*     */     
/* 132 */     if (obj != null) {
/* 133 */       if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
/* 134 */         float distance = 6.0F;
/*     */         
/* 136 */         if (hasCondition(Settings.ONLY_ON_GROUND) && 
/* 137 */           !this.mc.player.onGround) {
/* 138 */           distance = 3.0F;
/*     */         }
/*     */ 
/*     */         
/* 142 */         if (hasCondition(Settings.DISABLE_IN_WATER) && 
/* 143 */           this.mc.player.isInWater()) {
/* 144 */           distance = 3.0F;
/*     */         }
/*     */ 
/*     */         
/* 148 */         if (hasCondition(Settings.ONLY_WHEN_SPRINTING) && 
/* 149 */           !this.mc.player.isSprinting()) {
/* 150 */           distance = 3.0F;
/*     */         }
/*     */ 
/*     */         
/* 154 */         obj = reach.getRayTracedContent(distance);
/*     */         
/* 156 */         if (obj != null && 
/* 157 */           obj.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
/* 158 */           valid = true;
/*     */         }
/*     */       }
/* 161 */       else if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
/* 162 */         valid = true;
/*     */       } 
/*     */     }
/*     */     
/* 166 */     if (!valid) {
/*     */       return;
/*     */     }
/*     */     
/* 170 */     if (this.mc.gameSettings.keyBindUseItem.isKeyDown()) {
/*     */       return;
/*     */     }
/*     */     
/* 174 */     Entity target = obj.entityHit;
/*     */     
/* 176 */     if (((target instanceof EntityPlayer && hasCondition(AimTarget.ENEMIES)) || (target instanceof net.minecraft.entity.passive.EntityVillager && 
/* 177 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof Entity && 
/* 178 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntitySlime && 
/* 179 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityMob && 
/* 180 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityIronGolem && 
/* 181 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.passive.EntitySquid && 
/* 182 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.passive.EntityAnimal && 
/* 183 */       hasCondition(AimTarget.ANIMALS)) || (target instanceof net.minecraft.entity.boss.EntityDragon && 
/* 184 */       hasCondition(AimTarget.MOBS)) || (target instanceof net.minecraft.entity.monster.EntityGhast && 
/* 185 */       hasCondition(AimTarget.MOBS))) && target != this.mc.player) {
/*     */ 
/*     */       
/* 188 */       if (((Reach)AntiAFK.getInstance().getFeature(Reach.class)).isEnabled() && (AntiAFK.getInstance()).bots.contains(target)) {
/*     */         return;
/*     */       }
/*     */       
/* 192 */       if (((AntiEvade)AntiAFK.getInstance().getFeature(AntiEvade.class)).isEntityEvading(target)) {
/*     */         return;
/*     */       }
/*     */       
/* 196 */       if (((AntiDisengage)AntiAFK.getInstance().getFeature(AntiDisengage.class)).isEntityEvading(target)) {
/*     */         return;
/*     */       }
/*     */       
/* 200 */       if (target instanceof EntityPlayer) {
/* 201 */         EntityPlayer player = (EntityPlayer)target;
/*     */         
/* 203 */         if (!hasCondition(AimTarget.TEAM_BY_COLOR) && 
/* 204 */           this.mc.player.func_142014_c((EntityLivingBase)player)) {
/*     */           return;
/*     */         }
/*     */         
/* 208 */         if (!hasCondition(AimTarget.RANKED_USERS)) {
/* 209 */           String name = ChatColor.stripColors(player.getDisplayName().getUnformattedText().toUpperCase()).split(" ")[0];
/* 210 */           if (player.getDisplayName().getUnformattedText().contains(" ") && (
/* 211 */             name.contains("ULTRA") || name.contains("HERO") || name.contains("LEGEND") || name.contains("TITAN") || name.contains("ETERNAL") || name.contains("IMMORTAL") || name
/* 212 */             .contains("YT") || name.contains("STREAM") || name.contains("YOUTUBE") || name.contains("KNIGHT") || name.contains("LORD") || name
/* 213 */             .contains("LADY") || name.contains("DUKE") || name.contains("DUCHNESS") || name.contains("TRAINEE") || name.contains("MOD") || name
/* 214 */             .contains("SR.MOD") || name.contains("ADMIN") || name.contains("LEADER") || name.contains("OWNER") || name.contains("BUILDER") || name
/* 215 */             .contains("BUILDLEAD") || name.contains("DEVELOPER") || name.contains("SUPPORT") || name.contains("ARTIST"))) {
/*     */             return;
/*     */           }
/*     */         } 
/*     */ 
/*     */         
/* 221 */         if (!hasCondition(AimTarget.FRIENDS) && 
/* 222 */           (AntiAFK.getInstance()).friends.contains(player.getName())) {
/*     */           return;
/*     */         }
/*     */         
/* 226 */         if (player.isSpectator()) {
/*     */           return;
/*     */         }
/*     */       } 
/*     */     } else {
/*     */       return;
/*     */     } 
/* 233 */     if (obj.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
/* 234 */       boolean canSwing = (this.mc.player.getDistance(target) < this.swingDist.getIntValue());
/* 235 */       if (reach.isEnabled()) {
/* 236 */         if (reach.getReach(true) > this.mc.player.getDistance(target)) {
/* 237 */           this.mc.objectMouseOver = obj;
/*     */         }
/* 239 */         else if (this.mc.player.getDistance(target) > this.swingDist.getIntValue()) {
/*     */           
/*     */           return;
/*     */         }
/*     */       
/* 244 */       } else if (this.mc.player.getDistance(target) > this.hitDist.getIntValue()) {
/* 245 */         if (!canSwing) {
/*     */           return;
/*     */         }
/*     */       } else {
/* 249 */         this.mc.objectMouseOver = obj;
/*     */       }
/*     */     
/*     */     }
/* 253 */     else if (this.mc.player.getDistance(target) > this.swingDist.getIntValue()) {
/*     */       return;
/*     */     } 
/*     */ 
/*     */     
/* 258 */     if (this.ticksRemaining > 0) {
/* 259 */       this.ticksRemaining--;
/*     */     } else {
/* 261 */       int min = this.cpsMin.getIntValue();
/* 262 */       int max = this.cpsMax.getIntValue();
/*     */       
/* 264 */       int cps = min;
/*     */       
/* 266 */       if (max > min) {
/* 267 */         cps += this.random.nextInt(max - min);
/*     */       }
/*     */       
/* 270 */       this.cps = cps;
/* 271 */       this.interval = 1000 / cps;
/* 272 */       this.ticksRemaining = 20;
/*     */     } 
/*     */     
/* 275 */     Keystrokes strokes = (Keystrokes)AntiAFK.getInstance().getFeature(Keystrokes.class);
/*     */     
/* 277 */     if (this.cps > 20) {
/* 278 */       for (int i = 0; i < this.cps / 20; i++) {
/*     */         try {
/* 280 */           if (strokes.isEnabled()) {
/* 281 */             if (strokes.leftCPS > this.cps) {
/*     */               break;
/*     */             }
/*     */             
/* 285 */             strokes.leftClick();
/*     */           } 
/*     */           
/* 288 */           this.field.setInt(this.mc, 0);
/* 289 */           this.method.invoke(this.mc, new Object[0]);
/* 290 */         } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 291 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } else {
/* 295 */       long time = System.currentTimeMillis();
/*     */       
/* 297 */       if (time - this.lastTime > this.interval) {
/* 298 */         this.lastTime = time;
/*     */         try {
/* 300 */           this.field.setInt(this.mc, 0);
/* 301 */           this.method.invoke(this.mc, new Object[0]);
/*     */           
/* 303 */           if (strokes.isEnabled()) {
/* 304 */             strokes.leftClick();
/*     */           }
/* 306 */         } catch (IllegalAccessException|IllegalArgumentException|java.lang.reflect.InvocationTargetException e) {
/* 307 */           e.printStackTrace();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean hasCondition(AimTarget cond) {
/* 314 */     for (String condition : this.targets.getValues()) {
/* 315 */       if (condition.equalsIgnoreCase(cond.toString()))
/* 316 */         return true; 
/*     */     } 
/* 318 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Triggerbot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */