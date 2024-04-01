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
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.IChatComponent;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import org.lwjgl.input.Mouse;
/*     */ 
/*     */ public class FlashCounter
/*     */   extends Feature
/*     */   implements DrawImpl {
/*  24 */   final ItemStack stack = new ItemStack(Items.GOLDEN_AXE, 1);
/*  25 */   final String prefix = "Flash Charges: ";
/*  26 */   final int color = Color.white.getRGB();
/*     */   
/*     */   MenuDataObject x;
/*     */   
/*     */   MenuDataObject y;
/*  31 */   int charges = -1;
/*     */   
/*     */   boolean dragging;
/*     */   
/*     */   public FlashCounter(Minecraft minecraft) {
/*  36 */     super(minecraft, "flashcounter", -1, Category.PAGE_7);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuInit() {
/*  41 */     this.x = new MenuDataObject(getName() + "-x", "-1");
/*  42 */     this.y = new MenuDataObject(getName() + "-y", "-1");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMenuAdd(List<MenuComponent> components) {
/*  47 */     components.add(this.x);
/*  48 */     components.add(this.y);
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onRenderGameOverlay(RenderGameOverlayEvent.Text event) {
/*  53 */     if (this.mc.world == null || this.mc.player == null) {
/*     */       return;
/*     */     }
/*     */     
/*  57 */     GlStateManager.pushMatrix();
/*     */     
/*  59 */     ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
/*  60 */     int scaleFactor = sr.getScaleFactor();
/*  61 */     float value = 2.0F / scaleFactor;
/*  62 */     GlStateManager.scale(value, value, value);
/*  63 */     GlStateManager.enableBlend();
/*  64 */     GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
/*  65 */     GlStateManager.pushMatrix();
/*  66 */     GlStateManager.enableDepth();
/*     */     
/*  68 */     String text = "Charges: " + ((this.charges == -1) ? "?" : (String)Integer.valueOf(this.charges)) + ".";
/*     */     
/*  70 */     int width = 17;
/*  71 */     int height = 17 + getStringHeight(text);
/*     */     
/*  73 */     int orginalY = (this.y.getIntValue() != -1) ? this.y.getIntValue() : (sr.getScaledHeight() - height);
/*     */     
/*  75 */     if (this.x.getIntValue() == -1) {
/*  76 */       this.x.setValue(sr.getScaledWidth() / 2 - 95);
/*     */     }
/*     */     
/*  79 */     if (this.y.getIntValue() == -1) {
/*  80 */       this.y.setValue(orginalY);
/*     */     }
/*     */     
/*  83 */     int x = this.x.getIntValue();
/*  84 */     int y = this.y.getIntValue();
/*     */     
/*  86 */     this.mc.getRenderItem().renderItemAndEffectIntoGUI(this.stack, x, y);
/*  87 */     drawText(text, x + width / 2 - getStringWidth(text) / 2, y + height - getStringHeight(text), this.color);
/*     */     
/*  89 */     if (!((LockHud)AntiAFK.getInstance().getFeature(LockHud.class)).isEnabled() && 
/*  90 */       this.mc.currentScreen != null) {
/*  91 */       if (Mouse.isButtonDown(0)) {
/*  92 */         int mouseX = Mouse.getX();
/*  93 */         int mouseY = this.mc.displayHeight - Mouse.getY();
/*     */         
/*  95 */         mouseX = Math.round(mouseX / 2.0F);
/*  96 */         mouseY = Math.round(mouseY / 2.0F);
/*     */         
/*  98 */         if (!this.dragging) {
/*  99 */           if (mouseX >= this.x.getIntValue() && mouseX <= x + width && 
/* 100 */             mouseY >= y && mouseY <= this.y.getIntValue() + height) {
/* 101 */             this.dragging = true;
/*     */           }
/*     */         } else {
/*     */           
/* 105 */           this.x.setValue(mouseX);
/* 106 */           this.y.setValue(mouseY);
/*     */         }
/*     */       
/* 109 */       } else if (this.dragging) {
/* 110 */         this.dragging = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 116 */     GlStateManager.disableDepth();
/* 117 */     GlStateManager.popMatrix();
/* 118 */     GlStateManager.disableBlend();
/* 119 */     GlStateManager.popMatrix();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onAppendMessage(IChatComponent packet) {
/* 125 */     String text = packet.getUnformattedText();
/*     */     
/* 127 */     int index = text.indexOf("Flash Charges: ");
/*     */     
/* 129 */     if (index == -1) {
/* 130 */       return true;
/*     */     }
/*     */     
/* 133 */     index += "Flash Charges: ".length();
/*     */     
/* 135 */     StringBuilder chargerBuilder = new StringBuilder();
/* 136 */     boolean found = false;
/*     */     
/* 138 */     for (char character : text.substring(index).toCharArray()) {
/* 139 */       if (found) {
/* 140 */         if (!Character.isDigit(character)) {
/*     */           break;
/*     */         }
/* 143 */         chargerBuilder.append(character);
/*     */       }
/* 145 */       else if (!found && 
/* 146 */         Character.isDigit(character)) {
/* 147 */         found = true;
/* 148 */         chargerBuilder.append(character);
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 153 */     if (isInteger(chargerBuilder.toString())) {
/* 154 */       this.charges = Integer.valueOf(chargerBuilder.toString()).intValue();
/*     */     }
/*     */     
/* 157 */     return true;
/*     */   }
/*     */   
/*     */   private boolean isInteger(String text) {
/*     */     try {
/* 162 */       Integer.valueOf(text);
/* 163 */       return true;
/* 164 */     } catch (NumberFormatException e) {
/* 165 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\FlashCounter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */