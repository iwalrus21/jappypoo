/*    */ package me.onefightonewin.feature.types;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import me.onefightonewin.feature.Category;
/*    */ import me.onefightonewin.feature.Feature;
/*    */ import me.onefightonewin.gui.framework.MenuComponent;
/*    */ import me.onefightonewin.gui.framework.components.MenuColorPicker;
/*    */ import me.onefightonewin.gui.framework.components.MenuLabel;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class HitColor
/*    */   extends Feature
/*    */ {
/*    */   MenuLabel hitLabel;
/*    */   public MenuColorPicker hitColor;
/*    */   Map<UUID, Long> hits;
/* 21 */   long duration = 500L;
/*    */   
/*    */   public HitColor(Minecraft minecraft) {
/* 24 */     super(minecraft, "hitcolor", -1, Category.PAGE_6);
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuInit() {
/* 29 */     this.hitLabel = new MenuLabel("Hit color", 0, 0);
/* 30 */     this.hitColor = new MenuColorPicker(0, 0, 20, 10, Color.RED.getRGB());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onMenuAdd(List<MenuComponent> components) {
/* 35 */     components.add(this.hitLabel);
/* 36 */     components.add(this.hitColor);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean shouldRenderHit(UUID user) {
/* 59 */     if (!isEnabled()) {
/* 60 */       return false;
/*    */     }
/*    */     
/* 63 */     EntityPlayer player = this.mc.world.getPlayerEntityByUUID(user);
/*    */     
/* 65 */     if (player != null) {
/* 66 */       return (player.hurtTime > 0);
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\goat\Desktop\jap upd\Japware (1)-upd.jar!\me\onefightonewin\feature\types\HitColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */