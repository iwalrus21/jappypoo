/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.chat.ChatColor;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuComboBox;
/*     */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*     */ import me.onefightonewin.gui.framework.components.MenuSlider;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.WorldRenderer;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraftforge.client.event.RenderLivingEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.relauncher.ReflectionHelper;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Nametags
/*     */   extends Feature
/*     */ {
/*     */   MenuLabel comboBoxLabel;
/*     */   MenuComboBox comboBox;
/*     */   MenuLabel sizeLabel;
/*     */   MenuSlider size;
/*     */   Field renderPosX;
/*     */   Field renderPosY;
/*     */   Field renderPosZ;
/*     */   
/*     */   public Nametags(Minecraft minecraft) {
/*  41 */     super(minecraft, "nametags", 0, Category.PAGE_4);
/*  42 */     this.renderPosX = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosX", "field_78725_b" });
/*  43 */     this.renderPosY = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosY", "field_78726_c" });
/*  44 */     this.renderPosZ = ReflectionHelper.findField(RenderManager.class, new String[] { "renderPosZ", "field_78728_n" });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  49 */     this.comboBoxLabel = new MenuLabel("Nametags options", 0, 0);
/*  50 */     this.comboBox = new MenuComboBox(Modes.class, 0, 0);
/*  51 */     this.sizeLabel = new MenuLabel("Nametag size", 0, 0);
/*  52 */     this.size = new MenuSlider(1, 1, 10, 0, 0, 100, 6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  57 */     components.add(this.comboBoxLabel);
/*  58 */     components.add(this.comboBox);
/*  59 */     components.add(this.sizeLabel);
/*  60 */     components.add(this.size);
/*     */   }
/*     */   
/*     */   public boolean hasCondition(Modes cond) {
/*  64 */     for (String condition : this.comboBox.getValues()) {
/*  65 */       if (condition.equalsIgnoreCase(cond.toString()))
/*  66 */         return true; 
/*     */     } 
/*  68 */     return false;
/*     */   }
/*     */   
/*     */   enum Modes {
/*  72 */     DISTANCE, HEALTH, IGNORE_INVISIBLE;
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public <T extends EntityLivingBase> void shouldRenderNativeLabel(RenderLivingEvent.Specials.Pre<T> event) {
/*  77 */     if (!(event.entity instanceof net.minecraft.entity.player.EntityPlayer)) {
/*     */       return;
/*     */     }
/*     */     
/*  81 */     EntityLivingBase ent = event.entity;
/*     */     
/*  83 */     if (ent.isDead) {
/*     */       return;
/*     */     }
/*     */     
/*  87 */     if (ent == this.mc.player) {
/*     */       return;
/*     */     }
/*     */     
/*  91 */     event.setCanceled(true);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public <T extends EntityLivingBase> void renderOwnNative(RenderLivingEvent.Post<T> event) {
/*  96 */     if (!(event.entity instanceof net.minecraft.entity.player.EntityPlayer)) {
/*     */       return;
/*     */     }
/*     */     
/* 100 */     EntityLivingBase ent = event.entity;
/*     */     
/* 102 */     if (ent.isDead) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     if (ent == this.mc.player) {
/*     */       return;
/*     */     }
/*     */     
/* 110 */     if (hasCondition(Modes.IGNORE_INVISIBLE) && 
/* 111 */       ent.isInvisible()) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/* 116 */     String prefix = "";
/* 117 */     String suffix = "";
/*     */     
/* 119 */     if (hasCondition(Modes.DISTANCE)) {
/* 120 */       prefix = ChatColor.RED + Math.round(this.mc.player.getDistance((Entity)ent)) + "" + ChatColor.RESET + " | ";
/*     */     }
/*     */     
/* 123 */     if (hasCondition(Modes.HEALTH)) {
/* 124 */       suffix = ChatColor.RESET + " | " + ChatColor.GREEN + Math.round(ent.getHealth()) + "/" + Math.round(ent.getMaxHealth()) + "";
/*     */     }
/*     */     
/*     */     try {
/* 128 */       renderString(ent, prefix + ent.getDisplayName().getFormattedText() + suffix, ent.posX - this.renderPosX.getDouble(this.mc.getRenderManager()), ent.posY + ent.height - this.renderPosY.getDouble(this.mc.getRenderManager()), ent.posZ - this.renderPosZ.getDouble(this.mc.getRenderManager()));
/* 129 */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 130 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderString(EntityLivingBase ent, String str, double x, double y, double z) {
/* 136 */     FontRenderer fontrenderer = this.mc.getRenderManager().getFontRenderer();
/*     */     
/* 138 */     float f = 1.6F * this.size.getValue();
/* 139 */     float f1 = 0.016666668F * f;
/* 140 */     GlStateManager.pushMatrix();
/* 141 */     GlStateManager.translate((float)x + 0.0F, (float)y + 0.5F, (float)z);
/* 142 */     GL11.glNormal3f(0.0F, 1.0F, 0.0F);
/* 143 */     GlStateManager.rotate(-(this.mc.getRenderManager()).playerViewY, 0.0F, 1.0F, 0.0F);
/* 144 */     GlStateManager.rotate((this.mc.getRenderManager()).playerViewX, 1.0F, 0.0F, 0.0F);
/* 145 */     GlStateManager.scale(-f1, -f1, f1);
/* 146 */     GlStateManager.disableLighting();
/* 147 */     GlStateManager.depthMask(false);
/* 148 */     GlStateManager.disableDepth();
/* 149 */     GlStateManager.enableBlend();
/* 150 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/* 151 */     Tessellator tessellator = Tessellator.getInstance();
/* 152 */     WorldRenderer worldrenderer = tessellator.getBuffer();
/* 153 */     int i = 0;
/*     */     
/* 155 */     if (str.equals("deadmau5"))
/*     */     {
/* 157 */       i = -10;
/*     */     }
/*     */     
/* 160 */     int j = fontrenderer.getStringWidth(str) / 2;
/* 161 */     GlStateManager.disableTexture2D();
/* 162 */     worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
/* 163 */     worldrenderer.pos((-j - 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 164 */     worldrenderer.pos((-j - 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 165 */     worldrenderer.pos((j + 1), (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 166 */     worldrenderer.pos((j + 1), (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
/* 167 */     tessellator.draw();
/* 168 */     GlStateManager.enableTexture2D();
/* 169 */     fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
/* 170 */     GlStateManager.enableDepth();
/* 171 */     GlStateManager.depthMask(true);
/* 172 */     fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
/* 173 */     GlStateManager.enableLighting();
/* 174 */     GlStateManager.disableBlend();
/* 175 */     GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
/* 176 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\Nametags.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */