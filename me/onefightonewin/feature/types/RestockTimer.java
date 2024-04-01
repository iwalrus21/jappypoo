/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ public class RestockTimer
/*     */   extends Feature
/*     */ {
/*  30 */   final int maxTime = 60;
/*  31 */   final int maxTimeDeath = 1800;
/*     */   
/*  33 */   final int chest = 54;
/*  34 */   final int emerald = 388;
/*     */   
/*  36 */   final int pollTimeTotalTicks = 5;
/*     */   
/*     */   List<RestockData> timers;
/*     */   
/*     */   Field renderPosX;
/*     */   
/*     */   Field renderPosY;
/*     */   Field renderPosZ;
/*     */   int pollTime;
/*     */   
/*     */   public RestockTimer(Minecraft minecraft) {
/*  47 */     super(minecraft, "restocktimer", -1, Category.PAGE_7);
/*  48 */     this.timers = new CopyOnWriteArrayList<>();
/*  49 */     this.renderPosX = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosX", "field_78725_b" });
/*  50 */     this.renderPosY = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosY", "field_78726_c" });
/*  51 */     this.renderPosZ = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosZ", "field_78728_n" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  56 */     this.timers.clear();
/*  57 */     this.pollTime = 0;
/*     */   }
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
/*     */   @SubscribeEvent
/*     */   public void onClientTickEvent(TickEvent.ClientTickEvent event) {
/*  74 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  78 */     if (this.pollTime > 5) {
/*  79 */       this.pollTime = 0;
/*     */     } else {
/*  81 */       this.pollTime++;
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     for (Entity entity : this.mc.world.loadedEntityList) {
/*  86 */       if (entity instanceof EntityItem) {
/*  87 */         EntityItem item = (EntityItem)entity;
/*     */         
/*  89 */         if (!item.onGround) {
/*     */           continue;
/*     */         }
/*     */         
/*  93 */         int id = Item.getIdFromItem(item.getItem().getItem());
/*     */         
/*  95 */         if (id == 54 || id == 388) {
/*  96 */           boolean duplicate = false;
/*     */           
/*  98 */           for (RestockData data : this.timers) {
/*  99 */             if (data.isDuplicate(item.posX, item.posY, item.posZ)) {
/* 100 */               data.setItem(item);
/* 101 */               data.setReady();
/* 102 */               duplicate = true;
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/* 107 */           if (!duplicate) {
/* 108 */             this.timers.add(new RestockData(item, item.posX, item.posY, item.posZ, ChatColor.RED.toString()));
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void shouldRenderNativeLabel(RenderWorldLastEvent event) {
/* 117 */     long time = System.currentTimeMillis();
/* 118 */     RenderManager renderManager = this.mc.getRenderManager();
/*     */     
/* 120 */     for (RestockData data : this.timers) {
/* 121 */       int timeLeft = Math.round((float)(time - data.getStart()) / 1000.0F);
/*     */       
/* 123 */       if (timeLeft > 60 || (data.getItem() != null && !(data.getItem()).isDead)) {
/*     */         try {
/* 125 */           String text = ChatColor.RED + "UNKNOWN";
/* 126 */           int id = Item.getIdFromItem(data.getItem().getItem().getItem());
/*     */           
/* 128 */           if (id == 54) {
/* 129 */             text = ChatColor.YELLOW + "Restock";
/* 130 */           } else if (id == 388) {
/* 131 */             text = ChatColor.GREEN + "Gem";
/*     */           } 
/*     */           
/* 134 */           renderString(text, data.getX() - this.renderPosX.getDouble(renderManager), data.getY() - this.renderPosY.getDouble(renderManager), data.getZ() - this.renderPosZ.getDouble(renderManager));
/* 135 */         } catch (IllegalArgumentException|IllegalAccessException e) {
/* 136 */           e.printStackTrace();
/*     */         } 
/*     */         
/*     */         continue;
/*     */       } 
/* 141 */       if (data.getStart() > 0L && timeLeft > 1800) {
/* 142 */         this.timers.remove(data);
/*     */         
/*     */         continue;
/*     */       } 
/* 146 */       if (timeLeft > 60) {
/*     */         continue;
/*     */       }
/*     */       
/*     */       try {
/* 151 */         renderString(data.getColor() + (60 - timeLeft), data.getX() - this.renderPosX.getDouble(renderManager), data.getY() - this.renderPosY.getDouble(renderManager), data.getZ() - this.renderPosZ.getDouble(renderManager));
/* 152 */       } catch (IllegalArgumentException|IllegalAccessException e) {
/* 153 */         e.printStackTrace();
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void renderString(String str, double x, double y, double z) {
/*     */     float distance;
/* 159 */     FontRenderer fontrenderer = this.mc.getRenderManager().getFontRenderer();
/* 160 */     RenderManager renderManager = this.mc.getRenderManager();
/*     */ 
/*     */     
/*     */     try {
/* 164 */       float xDiff = (float)(this.mc.player.posX - this.renderPosX.getDouble(renderManager) - x);
/* 165 */       float yDiff = (float)(this.mc.player.posY - this.renderPosY.getDouble(renderManager) - y);
/* 166 */       float zDiff = (float)(this.mc.player.posZ - this.renderPosZ.getDouble(renderManager) - z);
/* 167 */       distance = MathHelper.sqrt(xDiff * xDiff + yDiff * yDiff + zDiff * zDiff);
/* 168 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 169 */       e.printStackTrace();
/*     */       
/*     */       return;
/*     */     } 
/* 173 */     float f = Math.max(1.6F, Math.min(10.0F, distance / 4.0F));
/* 174 */     float f1 = 0.016666668F * f;
/* 175 */     GlStateManager.pushMatrix();
/* 176 */     GlStateManager.translate((float)x + 0.0F, (float)y + 0.5F, (float)z);
/* 177 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 178 */     GlStateManager.rotate(-(this.mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/* 179 */     GlStateManager.rotate((this.mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
/* 180 */     GlStateManager.scale(-f1, -f1, f1);
/* 181 */     GlStateManager.depthMask(false);
/* 182 */     GlStateManager.disableDepth();
/* 183 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 184 */     Tessellator tessellator = Tessellator.getInstance();
/* 185 */     WorldRenderer worldrenderer = tessellator.getBuffer();
/* 186 */     int i = 0;
/*     */     
/* 188 */     if (str.equals("deadmau5"))
/*     */     {
/* 190 */       i = -10;
/*     */     }
/*     */     
/* 193 */     int j = fontrenderer.getStringWidth(str) / 2;
/* 194 */     GlStateManager.disableTexture2D();
/* 195 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 196 */     worldrenderer.pos((-j - 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 197 */     worldrenderer.pos((-j - 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 198 */     worldrenderer.pos((j + 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 199 */     worldrenderer.pos((j + 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 200 */     tessellator.draw();
/* 201 */     GlStateManager.enableTexture2D();
/* 202 */     fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
/* 203 */     GlStateManager.enableDepth();
/* 204 */     GlStateManager.depthMask(true);
/* 205 */     fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
/* 206 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 207 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   class RestockData
/*     */   {
/*     */     EntityItem item;
/*     */     private double x;
/*     */     private double y;
/*     */     private double z;
/*     */     private String color;
/*     */     private long start;
/*     */     
/*     */     public RestockData(EntityItem item, double x, double y, double z, String color) {
/* 221 */       this.item = item;
/* 222 */       this.x = x;
/* 223 */       this.y = y;
/* 224 */       this.z = z;
/* 225 */       this.color = color;
/*     */     }
/*     */     
/*     */     public void setItem(EntityItem item) {
/* 229 */       this.item = item;
/*     */     }
/*     */     
/*     */     public void setDead() {
/* 233 */       this.start = 0L;
/*     */     }
/*     */     
/*     */     public void setReady() {
/* 237 */       this.start = System.currentTimeMillis();
/*     */     }
/*     */     
/*     */     public boolean isDuplicate(double posX, double posY, double posZ) {
/* 241 */       double d0 = posX - this.x;
/* 242 */       double d1 = posY - this.y;
/* 243 */       double d2 = posZ - this.z;
/* 244 */       return (MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2) < 5.0F);
/*     */     }
/*     */     
/*     */     public EntityItem getItem() {
/* 248 */       return this.item;
/*     */     }
/*     */     
/*     */     public long getStart() {
/* 252 */       return this.start;
/*     */     }
/*     */     
/*     */     public double getX() {
/* 256 */       return this.x;
/*     */     }
/*     */     
/*     */     public double getY() {
/* 260 */       return this.y + 1.0D;
/*     */     }
/*     */     
/*     */     public double getZ() {
/* 264 */       return this.z;
/*     */     }
/*     */     
/*     */     public String getColor() {
/* 268 */       return this.color;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\RestockTimer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */