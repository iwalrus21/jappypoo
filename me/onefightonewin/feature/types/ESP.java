/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.FloatBuffer;
/*     */ import java.util.List;
/*     */ import javax.vecmath.Vector2f;
/*     */ import javax.vecmath.Vector3f;
/*     */ import javax.vecmath.Vector4f;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuCheckbox;
/*     */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*     */ import me.onefightonewin.gui.framework.components.MenuDropdown;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.util.Matrix4f;
/*     */ import net.minecraft.util.Timer;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.lwjgl.BufferUtils;
/*     */ import org.lwjgl.opengl.GL11;
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
/*     */ 
/*     */ 
/*     */ public class ESP
/*     */   extends Feature
/*     */ {
/*     */   MenuCheckbox espInvis;
/*     */   MenuCheckbox espPlayers;
/*     */   MenuCheckbox espChest;
/*     */   MenuCheckbox espFriend;
/*     */   MenuCheckbox espMob;
/*     */   MenuCheckbox espAnimal;
/*     */   MenuCheckbox espProp;
/*     */   MenuCheckbox espArrows;
/*     */   MenuColorPicker espColor;
/*     */   MenuColorPicker espChestColor;
/*     */   MenuColorPicker espFriendColor;
/*     */   MenuColorPicker espMobColor;
/*     */   MenuColorPicker espAnimalColor;
/*     */   MenuColorPicker espPropColor;
/*     */   MenuColorPicker espArrowsColor;
/*     */   MenuLabel secondLabel;
/*     */   MenuDropdown secondType;
/*     */   public static final boolean debug = false;
/*     */   Field timer;
/*     */   Field renderPosX;
/*     */   Field renderPosY;
/*     */   Field renderPosZ;
/*     */   
/*     */   public ESP(Minecraft minecraft) {
/*  75 */     super(minecraft, "esp", 0, Category.PAGE_4);
/*  76 */     this.timer = ReflectionHelper.findField(Minecraft.class, new String[] { "timer", "field_71428_T" });
/*  77 */     this.renderPosX = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosX", "field_78725_b" });
/*  78 */     this.renderPosY = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosY", "field_78726_c" });
/*  79 */     this.renderPosZ = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosZ", "field_78728_n" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  84 */     this.espInvis = new MenuCheckbox("ESP - Ignore invisible", 0, 0, 12, 12);
/*  85 */     this.espPlayers = new MenuCheckbox("ESP - Players", 0, 0, 12, 12);
/*  86 */     this.espChest = new MenuCheckbox("ESP - Chests", 0, 0, 12, 12);
/*  87 */     this.espFriend = new MenuCheckbox("ESP - Friends", 0, 0, 12, 12);
/*  88 */     this.espMob = new MenuCheckbox("ESP - Mob", 0, 0, 12, 12);
/*  89 */     this.espAnimal = new MenuCheckbox("ESP - Animals", 0, 0, 12, 12);
/*  90 */     this.espProp = new MenuCheckbox("ESP - Props", 0, 0, 12, 12);
/*  91 */     this.espArrows = new MenuCheckbox("ESP - Arrows", 0, 0, 12, 12);
/*     */     
/*  93 */     this.espColor = new MenuColorPicker(0, 0, 20, 10, Color.GREEN.getRGB());
/*  94 */     this.espChestColor = new MenuColorPicker(0, 0, 20, 10, Color.GREEN.getRGB());
/*  95 */     this.espFriendColor = new MenuColorPicker(0, 0, 20, 10, Color.BLUE.getRGB());
/*  96 */     this.espMobColor = new MenuColorPicker(0, 0, 20, 10, Color.RED.getRGB());
/*  97 */     this.espAnimalColor = new MenuColorPicker(0, 0, 20, 10, Color.YELLOW.getRGB());
/*  98 */     this.espPropColor = new MenuColorPicker(0, 0, 20, 10, Color.YELLOW.getRGB());
/*  99 */     this.espArrowsColor = new MenuColorPicker(0, 0, 20, 10, Color.YELLOW.getRGB());
/*     */     
/* 101 */     this.secondLabel = new MenuLabel("ESP options", 0, 0);
/* 102 */     this.secondType = new MenuDropdown(Modes.class, 0, 0);
/*     */   }
/*     */   
/*     */   enum Modes {
/* 106 */     NORMAL, CORNERS, BOX;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/* 111 */     components.add(this.espInvis);
/* 112 */     components.add(this.espPlayers);
/* 113 */     components.add(this.espColor);
/* 114 */     components.add(this.espChest);
/* 115 */     components.add(this.espChestColor);
/* 116 */     components.add(this.espFriend);
/* 117 */     components.add(this.espFriendColor);
/* 118 */     components.add(this.espMob);
/* 119 */     components.add(this.espMobColor);
/* 120 */     components.add(this.espAnimal);
/* 121 */     components.add(this.espAnimalColor);
/* 122 */     components.add(this.espProp);
/* 123 */     components.add(this.espPropColor);
/* 124 */     components.add(this.espArrows);
/* 125 */     components.add(this.espArrowsColor);
/*     */     
/* 127 */     components.add(this.secondLabel);
/* 128 */     components.add(this.secondType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onRenderWorldPass() {
/*     */     RenderManager renderManager;
/*     */     Timer timer;
/*     */     try {
/* 137 */       renderManager = this.mc.getRenderManager();
/* 138 */       timer = (Timer)this.timer.get(this.mc);
/* 139 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 140 */       e.printStackTrace();
/*     */       
/*     */       return;
/*     */     } 
/* 144 */     for (Entity entity : this.mc.world.loadedEntityList) {
/* 145 */       double posX, posY, posZ; Color color = null;
/*     */       
/* 147 */       if (entity instanceof EntityPlayer) {
/* 148 */         if (!this.espPlayers.getRValue() || 
/* 149 */           entity == this.mc.player) {
/*     */           continue;
/*     */         }
/* 152 */         if (this.espInvis.getRValue() && entity.isInvisible()) {
/*     */           continue;
/*     */         }
/*     */         
/* 156 */         color = new Color(this.espColor.getColor().getRGB(), true);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 161 */         if ((AntiAFK.getInstance()).friends.contains(((EntityPlayer)entity).getName()) && 
/* 162 */           this.espFriend.getRValue()) {
/* 163 */           color = new Color(this.espFriendColor.getColor().getRGB(), true);
/*     */         }
/*     */ 
/*     */         
/* 167 */         if ((AntiAFK.getInstance()).bots.contains(entity));
/*     */ 
/*     */ 
/*     */       
/*     */       }
/* 172 */       else if (entity instanceof net.minecraft.entity.passive.EntityBat || entity instanceof net.minecraft.entity.monster.EntitySlime || entity instanceof net.minecraft.entity.monster.EntityMob || entity instanceof net.minecraft.entity.boss.EntityDragon || entity instanceof net.minecraft.entity.monster.EntityGhast || entity instanceof net.minecraft.entity.monster.EntityIronGolem) {
/*     */         
/* 174 */         if (this.espMob.getRValue()) {
/* 175 */           color = new Color(this.espMobColor.getColor().getRGB(), true);
/*     */         } else {
/*     */           continue;
/*     */         } 
/* 179 */       } else if (entity instanceof net.minecraft.entity.passive.EntityVillager || entity instanceof net.minecraft.entity.passive.EntitySquid || entity instanceof net.minecraft.entity.passive.EntityAnimal) {
/* 180 */         if (this.espAnimal.getRValue()) {
/* 181 */           color = new Color(this.espAnimalColor.getColor().getRGB(), true);
/*     */         } else {
/*     */           continue;
/*     */         } 
/* 185 */       } else if (entity instanceof net.minecraft.entity.item.EntityFallingBlock) {
/* 186 */         if (this.espProp.getRValue()) {
/* 187 */           color = new Color(this.espPropColor.getColor().getRGB(), true);
/*     */         } else {
/*     */           continue;
/*     */         } 
/* 191 */       } else if (entity instanceof net.minecraft.entity.projectile.EntityArrow) {
/* 192 */         if (this.espArrows.getRValue()) {
/* 193 */           color = new Color(this.espArrowsColor.getColor().getRGB(), true);
/*     */         } else {
/*     */           continue;
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 204 */       if (color == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 212 */         posX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * timer.field_74281_c - this.renderPosX.getDouble(renderManager);
/* 213 */         posY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * timer.field_74281_c - this.renderPosY.getDouble(renderManager);
/* 214 */         posZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * timer.field_74281_c - this.renderPosZ.getDouble(renderManager);
/* 215 */       } catch (IllegalArgumentException|IllegalAccessException e1) {
/* 216 */         e1.printStackTrace();
/*     */         
/*     */         continue;
/*     */       } 
/* 220 */       if (Modes.CORNERS.toString().equalsIgnoreCase(this.secondType.getValue().toString()) || Modes.NORMAL.toString().equalsIgnoreCase(this.secondType.getValue().toString())) {
/* 221 */         GlStateManager.pushMatrix();
/*     */         
/* 223 */         GL11.glDisable(2929);
/* 224 */         GL11.glEnable(3042);
/* 225 */         GL11.glDisable(3553);
/* 226 */         GL11.glBlendFunc(770, 771);
/*     */         
/* 228 */         GlStateManager.depthMask(true);
/*     */         
/* 230 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*     */         
/* 232 */         GlStateManager.translate(posX, posY, posZ);
/*     */         
/* 234 */         GlStateManager.rotate(-(this.mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/* 235 */         GlStateManager.rotate((this.mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
/*     */         
/* 237 */         float heightOffset = -1.0F;
/* 238 */         float width = entity.width;
/* 239 */         float height = entity.height / 2.0F;
/*     */         
/* 241 */         if (entity.height < 1.3D) {
/* 242 */           height = (float)(height * 1.3D);
/* 243 */           heightOffset = (float)(heightOffset - -0.2D);
/*     */         } 
/*     */         
/* 246 */         if (entity.height < 1.0F) {
/* 247 */           heightOffset = (float)(heightOffset + 0.3D);
/*     */         }
/*     */         
/* 250 */         if (entity.height > 2.0F) {
/* 251 */           heightOffset = (float)(heightOffset - 0.4D);
/*     */         }
/*     */         
/* 254 */         float size = 0.4F;
/*     */         
/* 256 */         if (Modes.CORNERS.toString().equalsIgnoreCase(this.secondType.getValue().toString())) {
/* 257 */           GL11.glBegin(1);
/*     */           
/* 259 */           GL11.glVertex2d(-width, (-height - heightOffset));
/* 260 */           GL11.glVertex2d((width * -size), (-height - heightOffset));
/*     */           
/* 262 */           GL11.glVertex2d(width, (-height - heightOffset));
/* 263 */           GL11.glVertex2d((-width * -size), (-height - heightOffset));
/*     */           
/* 265 */           GL11.glVertex2d(width, (-height - heightOffset));
/* 266 */           GL11.glVertex2d(width, (-height * size - heightOffset));
/*     */           
/* 268 */           GL11.glVertex2d(-width, -(height + heightOffset));
/* 269 */           GL11.glVertex2d(-width, (-height * size - heightOffset));
/*     */ 
/*     */           
/* 272 */           GL11.glVertex2d(-width, (height - heightOffset));
/* 273 */           GL11.glVertex2d((width * -size), (height - heightOffset));
/*     */           
/* 275 */           GL11.glVertex2d(width, (height - heightOffset));
/* 276 */           GL11.glVertex2d((-width * -size), (height - heightOffset));
/*     */           
/* 278 */           GL11.glVertex2d(width, (height - heightOffset));
/* 279 */           GL11.glVertex2d(width, (height * size - heightOffset));
/*     */           
/* 281 */           GL11.glVertex2d(-width, (height - heightOffset));
/* 282 */           GL11.glVertex2d(-width, (height * size - heightOffset));
/* 283 */           GL11.glEnd();
/* 284 */         } else if (Modes.NORMAL.toString().equalsIgnoreCase(this.secondType.getValue().toString())) {
/* 285 */           GL11.glBegin(2);
/* 286 */           GL11.glVertex2d(-width, (-height - heightOffset));
/* 287 */           GL11.glVertex2d(width, (-height - heightOffset));
/* 288 */           GL11.glVertex2d(-width, (-height - heightOffset));
/* 289 */           GL11.glVertex2d(-width, (height - heightOffset));
/* 290 */           GL11.glVertex2d(width, (height - heightOffset));
/* 291 */           GL11.glVertex2d(-width, (height - heightOffset));
/* 292 */           GL11.glVertex2d(width, (height - heightOffset));
/* 293 */           GL11.glVertex2d(width, (-height - heightOffset));
/* 294 */           GL11.glEnd();
/*     */         } 
/*     */         
/* 297 */         GlStateManager.translate(0.0D, 21.0D + -((entity.getEntityBoundingBox()).maxY - (entity.getEntityBoundingBox()).minY) * 12.0D, 0.0D);
/* 298 */         GL11.glEnable(2929);
/* 299 */         GL11.glEnable(3553);
/* 300 */         GL11.glDisable(3042);
/* 301 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */         
/* 303 */         GlStateManager.popMatrix(); continue;
/* 304 */       }  if (Modes.BOX.toString().equalsIgnoreCase(this.secondType.getValue().toString())) {
/* 305 */         double x, y, z; GlStateManager.pushMatrix();
/*     */         
/* 307 */         GL11.glDisable(2929);
/* 308 */         GL11.glEnable(3042);
/* 309 */         GL11.glDisable(3553);
/* 310 */         GL11.glBlendFunc(770, 771);
/*     */         
/* 312 */         GlStateManager.depthMask(true);
/*     */         
/* 314 */         GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/* 315 */         GL11.glBegin(2);
/*     */         
/* 317 */         float height = entity.height;
/* 318 */         float width = entity.width;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         try {
/* 325 */           x = entity.posX - this.renderPosX.getDouble(renderManager);
/* 326 */           y = entity.posY - this.renderPosY.getDouble(renderManager);
/* 327 */           z = entity.posZ - this.renderPosZ.getDouble(renderManager);
/* 328 */         } catch (IllegalArgumentException|IllegalAccessException e1) {
/* 329 */           e1.printStackTrace();
/*     */           
/*     */           continue;
/*     */         } 
/* 333 */         x -= 0.5D;
/* 334 */         z -= 0.5D;
/*     */         
/* 336 */         GL11.glVertex3d(x, y, z);
/* 337 */         GL11.glVertex3d(x + width, y, z);
/* 338 */         GL11.glVertex3d(x + width, y, z + width);
/* 339 */         GL11.glVertex3d(x, y, z + width);
/* 340 */         GL11.glVertex3d(x, y + height, z + width);
/* 341 */         GL11.glVertex3d(x + width, y + height, z + width);
/* 342 */         GL11.glVertex3d(x + width, y + height, z);
/* 343 */         GL11.glVertex3d(x, y + height, z);
/* 344 */         GL11.glVertex3d(x, y, z);
/* 345 */         GL11.glVertex3d(x, y, z + width);
/* 346 */         GL11.glVertex3d(x, y + height, z + width);
/* 347 */         GL11.glVertex3d(x, y + height, z);
/* 348 */         GL11.glVertex3d(x + width, y + height, z);
/* 349 */         GL11.glVertex3d(x + width, y, z);
/* 350 */         GL11.glVertex3d(x + width, y, z + width);
/* 351 */         GL11.glVertex3d(x + width, y + height, z + width);
/* 352 */         GL11.glVertex3d(x + width, y + height, z);
/* 353 */         GL11.glVertex3d(x + width, y, z);
/*     */         
/* 355 */         GL11.glEnd();
/*     */         
/* 357 */         GL11.glEnable(2929);
/* 358 */         GL11.glEnable(3553);
/* 359 */         GL11.glDisable(3042);
/* 360 */         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */         
/* 362 */         GlStateManager.popMatrix();
/*     */       } 
/*     */     } 
/*     */     
/* 366 */     for (TileEntity entity : this.mc.world.loadedTileEntityList) {
/* 367 */       double posX, posY, posZ; Color color = null;
/*     */       
/* 369 */       if (entity instanceof net.minecraft.tileentity.TileEntityChest) {
/* 370 */         if (this.espChest.getRValue()) {
/* 371 */           color = new Color(this.espChestColor.getColor().getRGB(), true);
/*     */         } else {
/*     */           continue;
/*     */         } 
/*     */       }
/*     */       
/* 377 */       if (color == null) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       try {
/* 385 */         posX = entity.getPos().getX() - this.renderPosX.getDouble(renderManager);
/* 386 */         posY = entity.getPos().getY() - this.renderPosY.getDouble(renderManager);
/* 387 */         posZ = entity.getPos().getZ() - this.renderPosZ.getDouble(renderManager);
/* 388 */       } catch (IllegalArgumentException|IllegalAccessException e1) {
/* 389 */         e1.printStackTrace();
/*     */         
/*     */         continue;
/*     */       } 
/* 393 */       GlStateManager.pushMatrix();
/* 394 */       GlStateManager.translate(posX, posY, posZ);
/*     */       
/* 396 */       GL11.glDisable(2929);
/* 397 */       GL11.glEnable(3042);
/* 398 */       GL11.glDisable(3553);
/* 399 */       GL11.glBlendFunc(770, 771);
/*     */       
/* 401 */       GlStateManager.depthMask(true);
/*     */       
/* 403 */       GL11.glColor4f(color.getRed() / 255.0F, color.getGreen() / 255.0F, color.getBlue() / 255.0F, color.getAlpha() / 255.0F);
/*     */       
/* 405 */       GL11.glBegin(2);
/*     */       
/* 407 */       double x = 0.0D;
/* 408 */       double y = 0.0D;
/* 409 */       double z = 0.0D;
/* 410 */       GL11.glVertex3d(x, y, z);
/* 411 */       GL11.glVertex3d(x + 1.0D, y, z);
/* 412 */       GL11.glVertex3d(x + 1.0D, y, z + 1.0D);
/* 413 */       GL11.glVertex3d(x, y, z + 1.0D);
/* 414 */       GL11.glVertex3d(x, y + 1.0D, z + 1.0D);
/* 415 */       GL11.glVertex3d(x + 1.0D, y + 1.0D, z + 1.0D);
/* 416 */       GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
/* 417 */       GL11.glVertex3d(x, y + 1.0D, z);
/* 418 */       GL11.glVertex3d(x, y, z);
/* 419 */       GL11.glVertex3d(x, y, z + 1.0D);
/* 420 */       GL11.glVertex3d(x, y + 1.0D, z + 1.0D);
/* 421 */       GL11.glVertex3d(x, y + 1.0D, z);
/* 422 */       GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
/* 423 */       GL11.glVertex3d(x + 1.0D, y, z);
/* 424 */       GL11.glVertex3d(x + 1.0D, y, z + 1.0D);
/* 425 */       GL11.glVertex3d(x + 1.0D, y + 1.0D, z + 1.0D);
/* 426 */       GL11.glVertex3d(x + 1.0D, y + 1.0D, z);
/* 427 */       GL11.glVertex3d(x + 1.0D, y, z);
/*     */       
/* 429 */       GL11.glEnd();
/*     */       
/* 431 */       GlStateManager.translate(0.0D, 21.0D + -((entity.getRenderBoundingBox()).maxY - (entity.getRenderBoundingBox()).minY) * 12.0D, 0.0D);
/* 432 */       GL11.glEnable(2929);
/* 433 */       GL11.glEnable(3553);
/* 434 */       GL11.glDisable(3042);
/* 435 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*     */       
/* 437 */       GlStateManager.popMatrix();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Matrix4f getMatrix(int matrix) {
/* 443 */     FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(16);
/*     */     
/* 445 */     GL11.glGetFloat(matrix, floatBuffer);
/*     */     
/* 447 */     return (Matrix4f)(new Matrix4f()).load(floatBuffer);
/*     */   }
/*     */   
/*     */   public Vector2f worldToScreen(Vector3f pointInWorld) {
/* 451 */     Vector4f clipSpacePos = multiply(
/* 452 */         multiply(new Vector4f(pointInWorld.x, pointInWorld.y, pointInWorld.z, 1.0F), getMatrix(2982)), getMatrix(2983));
/*     */     
/* 454 */     Vector3f ndcSpacePos = new Vector3f(clipSpacePos.x / clipSpacePos.w, clipSpacePos.y / clipSpacePos.w, clipSpacePos.z / clipSpacePos.w);
/*     */ 
/*     */     
/* 457 */     float screenX = (ndcSpacePos.x + 1.0F) / 2.0F * this.mc.displayWidth;
/* 458 */     float screenY = (1.0F - ndcSpacePos.y) / 2.0F * this.mc.displayHeight;
/*     */     
/* 460 */     if (ndcSpacePos.z < -1.0D || ndcSpacePos.z > 1.0D) {
/* 461 */       return null;
/*     */     }
/*     */     
/* 464 */     return new Vector2f(screenX, screenY);
/*     */   }
/*     */   
/*     */   public Vector4f multiply(Vector4f vec, Matrix4f mat) {
/* 468 */     return new Vector4f(vec.x * mat.m00 + vec.y * mat.m10 + vec.z * mat.m20 + vec.w * mat.m30, vec.x * mat.m01 + vec.y * mat.m11 + vec.z * mat.m21 + vec.w * mat.m31, vec.x * mat.m02 + vec.y * mat.m12 + vec.z * mat.m22 + vec.w * mat.m32, vec.x * mat.m03 + vec.y * mat.m13 + vec.z * mat.m23 + vec.w * mat.m33);
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\ESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */