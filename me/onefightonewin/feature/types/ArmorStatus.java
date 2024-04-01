/*     */ package me.onefightonewin.feature.types;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.util.List;
/*     */ import me.onefightonewin.AntiAFK;
/*     */ import me.onefightonewin.feature.Category;
/*     */ import me.onefightonewin.feature.Feature;
/*     */ import me.onefightonewin.gui.framework.DrawImpl;
/*     */ import me.onefightonewin.gui.framework.MenuComponent;
/*     */ import me.onefightonewin.gui.framework.components.MenuDataObject;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class ArmorStatus
/*     */   extends Feature
/*     */   implements DrawImpl
/*     */ {
/*     */   MenuDataObject x;
/*     */   MenuDataObject y;
/*     */   boolean dragging;
/*     */   
/*     */   public ArmorStatus(Minecraft minecraft) {
/*  28 */     super(minecraft, "armorstatus", -1, Category.PAGE_6);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  33 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  34 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  39 */     components.add(this.x);
/*  40 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  45 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  49 */     GlStateManager.pushMatrix();
/*     */     
/*  51 */     ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
/*  52 */     int scaleFactor = sr.getScaleFactor();
/*  53 */     float value = 2.0F / scaleFactor;
/*  54 */     GlStateManager.scale(value, value, value);
/*  55 */     GlStateManager.enableBlend();
/*  56 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  57 */     GlStateManager.pushMatrix();
/*  58 */     GlStateManager.enableDepth();
/*  59 */     int height = 17;
/*  60 */     int orginalY = (this.y.getIntValue() != -1) ? this.y.getIntValue() : (sr.getScaledHeight() - height);
/*     */     
/*  62 */     if (this.x.getIntValue() == -1) {
/*  63 */       this.x.setValue(sr.getScaledWidth() / 2 + 95);
/*     */     }
/*     */     
/*  66 */     if (this.y.getIntValue() == -1) {
/*  67 */       this.y.setValue(orginalY);
/*     */     }
/*     */     
/*  70 */     int x = this.x.getIntValue();
/*  71 */     int y = this.y.getIntValue();
/*  72 */     int barOffset = 18;
/*  73 */     int barWidth = 1;
/*     */     
/*  75 */     if (scaleFactor == 1) {
/*  76 */       x -= 45;
/*  77 */     } else if (scaleFactor == 3) {
/*  78 */       x += 45;
/*  79 */     } else if (scaleFactor == 4) {
/*  80 */       x += 90;
/*     */     } 
/*     */     int i;
/*  83 */     for (i = 0; i < 5; i++) {
/*  84 */       ItemStack item = null;
/*     */       
/*  86 */       if (i == 4) {
/*  87 */         item = this.mc.player.func_70694_bm();
/*     */       } else {
/*  89 */         item = (Minecraft.getMinecraft()).player.func_82169_q(i);
/*     */       } 
/*     */       
/*  92 */       if (item != null && item.getItem() != null) {
/*     */         
/*  94 */         float percentageHurt = 1.0F - item.getItemDamage() / item.getMaxDamage();
/*     */         
/*  96 */         if (item.getMaxDamage() == 0) {
/*  97 */           percentageHurt = item.stackSize / item.getMaxStackSize();
/*     */         }
/*     */         
/* 100 */         if (percentageHurt < 1.0F) {
/* 101 */           drawRect(x - 1 + barOffset, y + 1, barWidth + 2, height - 2, Color.BLACK.getRGB());
/*     */         }
/*     */         
/* 104 */         y -= height;
/*     */       } 
/*     */     } 
/*     */     
/* 108 */     y = orginalY;
/*     */     
/* 110 */     for (i = 0; i < 5; i++) {
/* 111 */       ItemStack item = null;
/*     */       
/* 113 */       if (i == 4) {
/* 114 */         item = this.mc.player.func_70694_bm();
/*     */       } else {
/* 116 */         item = (Minecraft.getMinecraft()).player.func_82169_q(i);
/*     */       } 
/*     */       
/* 119 */       if (item != null && item.getItem() != null) {
/* 120 */         this.mc.getRenderItem().renderItemAndEffectIntoGUI(item, x, y);
/*     */         
/* 122 */         float percentageHurt = 1.0F - item.getItemDamage() / item.getMaxDamage();
/*     */         
/* 124 */         if (item.getMaxDamage() == 0) {
/* 125 */           percentageHurt = item.stackSize / item.getMaxStackSize();
/*     */         }
/*     */         
/* 128 */         if (percentageHurt < 1.0F) {
/* 129 */           if (percentageHurt > 1.0F) {
/* 130 */             percentageHurt = 1.0F;
/* 131 */           } else if (percentageHurt < 0.0F) {
/* 132 */             percentageHurt = 0.0F;
/*     */           } 
/*     */           
/* 135 */           int barHeight = (int)(percentageHurt * (height - 4));
/*     */           
/* 137 */           Color color = new Color((int)(255.0F - percentageHurt * 255.0F), (int)(percentageHurt * 255.0F), 0);
/* 138 */           drawRect(x + barOffset, y + height - barHeight - 2, barWidth, barHeight, color.getRGB());
/*     */         } 
/*     */         
/* 141 */         y -= height;
/*     */       } 
/*     */     } 
/*     */     
/* 145 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/* 146 */       this.mc.currentScreen != null) {
/* 147 */       if (Mouse.isButtonDown(0)) {
/* 148 */         int mouseX = Mouse.getX();
/* 149 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/* 151 */         mouseX = Math.round(mouseX / 2.0F);
/* 152 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/* 154 */         if (!this.dragging) {
/* 155 */           if (mouseX >= this.x.getIntValue() && mouseX <= x + barOffset + barWidth && 
/* 156 */             mouseY >= y && mouseY <= this.y.getIntValue() + height) {
/* 157 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 161 */           this.x.setValue(mouseX);
/* 162 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 165 */       } else if (this.dragging) {
/* 166 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 172 */     GlStateManager.disableDepth();
/* 173 */     GlStateManager.popMatrix();
/* 174 */     GlStateManager.disableBlend();
/* 175 */     GlStateManager.popMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\ArmorStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */