/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Reach
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel reachOptionsLabel;
/*     */   MenuComboBox reachOptions;
/*     */   MenuLabel reachChanceLabel;
/*     */   MenuSlider reachChance;
/*     */   MenuLabel reachMinLabel;
/*     */   MenuSlider reachMin;
/*     */   MenuLabel reachMaxLabel;
/*     */   MenuSlider reachMax;
/*     */   Random random;
/*     */   float reach;
/*     */   int ticksRemaining;
/*     */   boolean ready;
/*     */   boolean on;
/*     */   
/*     */   public Reach(Minecraft minecraft) {
/*  45 */     super(minecraft, "reach", 0, Category.PAGE_1);
/*  46 */     this.random = new Random();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  51 */     this.reachOptionsLabel = new MenuLabel("Reach options", 0, 0);
/*  52 */     this.reachOptions = new MenuComboBox(Settings.class, 0, 0);
/*  53 */     this.reachChanceLabel = new MenuLabel("Reach chance", 0, 0);
/*  54 */     this.reachChance = new MenuSlider(100, 0, 100, 0, 0, 100, 6);
/*  55 */     this.reachMinLabel = new MenuLabel("Reach min", 0, 0);
/*  56 */     this.reachMin = new MenuSlider(3.0F, 3.0F, 6.0F, 1, 0, 0, 100, 6);
/*  57 */     this.reachMaxLabel = new MenuLabel("Reach max", 0, 0);
/*  58 */     this.reachMax = new MenuSlider(3.0F, 3.0F, 6.0F, 1, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  63 */     components.add(this.reachOptionsLabel);
/*  64 */     components.add(this.reachOptions);
/*  65 */     components.add(this.reachChanceLabel);
/*  66 */     components.add(this.reachChance);
/*  67 */     components.add(this.reachMinLabel);
/*  68 */     components.add(this.reachMin);
/*  69 */     components.add(this.reachMaxLabel);
/*  70 */     components.add(this.reachMax);
/*     */   }
/*     */   
/*     */   enum Settings {
/*  74 */     CHANCE, ONLY_ON_SPRINT, ONLY_ON_GROUND, VERTICAL_CHECK, DISABLE_IN_WATER;
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Settings cond) {
/*  78 */     for (String condition : this.reachOptions.getValues()) {
/*  79 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  80 */         return true; 
/*     */     } 
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  87 */     this.ticksRemaining = 0;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  92 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     if (hasCondition(Settings.ONLY_ON_SPRINT) && (
/*  97 */       !this.mc.player.isSprinting() || this.mc.player.isInLava() || this.mc.player.isInWater())) {
/*  98 */       this.reach = -1.0F;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 103 */     if (hasCondition(Settings.ONLY_ON_GROUND) && 
/* 104 */       !this.mc.player.onGround) {
/* 105 */       this.reach = -1.0F;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 110 */     if (hasCondition(Settings.DISABLE_IN_WATER) && 
/* 111 */       this.mc.player.isInWater()) {
/* 112 */       this.reach = -1.0F;
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 117 */     if (this.reachChance.getIntValue() < 100 && hasCondition(Settings.CHANCE)) {
/* 118 */       if (Mouse.isButtonDown(0)) {
/* 119 */         if (this.ready) {
/* 120 */           this.on = (1 + this.random.nextInt(99) <= this.reachChance.getIntValue());
/* 121 */           this.ready = false;
/*     */         } 
/*     */       } else {
/* 124 */         this.ready = true;
/*     */       } 
/*     */     } else {
/* 127 */       this.on = true;
/*     */     } 
/*     */     
/* 130 */     if (this.ticksRemaining > 0) {
/* 131 */       this.ticksRemaining--;
/*     */       
/*     */       return;
/*     */     } 
/* 135 */     int minReach = Math.round(this.reachMin.getValue() * 10.0F);
/* 136 */     int maxReach = Math.round(this.reachMax.getValue() * 10.0F);
/*     */     
/* 138 */     this.reach = minReach;
/*     */     
/* 140 */     if (maxReach > minReach) {
/* 141 */       this.reach += this.random.nextInt(maxReach - minReach);
/*     */     }
/*     */     
/* 144 */     this.reach /= 10.0F;
/* 145 */     this.ticksRemaining = 60;
/*     */   }
/*     */   
/*     */   public MovingObjectPosition getRayTracedContent(float reach) {
/* 149 */     if (this.mc.player == null) {
/* 150 */       return null;
/*     */     }
/* 152 */     if (this.mc.world == null) {
/* 153 */       return null;
/*     */     }
/* 155 */     Vec3 vec3 = this.mc.player.getPositionEyes(0.0F);
/* 156 */     Vec3 vec31 = this.mc.player.getLook(0.0F);
/* 157 */     Vec3 vec32 = vec3.add(vec31.x * reach, vec31.y * reach, vec31.z * reach);
/* 158 */     Entity pointedEntity = null;
/* 159 */     Vec3 vec33 = null;
/* 160 */     double d2 = 0.0D;
/* 161 */     float f = 1.0F;
/*     */     
/* 163 */     List<Entity> list = this.mc.world.getEntitiesInAABBexcluding((Entity)this.mc.player, this.mc.player
/* 164 */         .getEntityBoundingBox()
/* 165 */         .expand(vec31.x * reach, vec31.y * reach, vec31.z * reach)
/*     */         
/* 167 */         .grow(f, f, f), 
/* 168 */         Predicates.and(EntitySelectors.NOT_SPECTATING, new Predicate<Entity>() {
/*     */             public boolean apply(Entity p_apply_1_) {
/* 170 */               return p_apply_1_.canBeCollidedWith();
/*     */             }
/*     */           }));
/*     */     
/* 174 */     for (int j = 0; j < list.size(); j++) {
/* 175 */       Entity entity1 = list.get(j);
/* 176 */       float f1 = entity1.getCollisionBorderSize();
/* 177 */       AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().grow(f1, f1, f1);
/* 178 */       MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
/*     */       
/* 180 */       if (axisalignedbb.contains(vec3)) {
/* 181 */         if (d2 >= 0.0D) {
/* 182 */           pointedEntity = entity1;
/* 183 */           vec33 = (movingobjectposition == null) ? vec3 : movingobjectposition.hitVec;
/* 184 */           d2 = 0.0D;
/*     */         } 
/* 186 */       } else if (movingobjectposition != null) {
/* 187 */         double d3 = vec3.distanceTo(movingobjectposition.hitVec);
/*     */         
/* 189 */         if (d3 < d2 || d2 == 0.0D) {
/* 190 */           if (entity1 == this.mc.player.field_70154_o && !this.mc.player.canRiderInteract()) {
/* 191 */             if (d2 == 0.0D) {
/* 192 */               pointedEntity = entity1;
/* 193 */               vec33 = movingobjectposition.hitVec;
/*     */             } 
/*     */           } else {
/* 196 */             pointedEntity = entity1;
/* 197 */             vec33 = movingobjectposition.hitVec;
/* 198 */             d2 = d3;
/*     */           } 
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 204 */     if (pointedEntity == null) {
/* 205 */       return null;
/*     */     }
/* 207 */     if (!this.mc.player.canEntityBeSeen(pointedEntity)) {
/* 208 */       return null;
/*     */     }
/*     */     
/* 211 */     if (hasCondition(Settings.VERTICAL_CHECK) && 
/* 212 */       pointedEntity.getPosition().getY() <= this.mc.player.getPosition().getY()) {
/* 213 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 217 */     return new MovingObjectPosition(pointedEntity, vec33);
/*     */   }
/*     */   
/*     */   public float getReach(boolean override) {
/* 221 */     if (override) {
/* 222 */       return this.reach;
/*     */     }
/* 224 */     return getReach();
/*     */   }
/*     */   
/*     */   public float getReach() {
/* 228 */     if (this.on) {
/* 229 */       return this.reach;
/*     */     }
/*     */     
/* 232 */     return 3.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Reach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */